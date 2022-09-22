package com.github.mrzhqiang.helper;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 分布式自增长 ID 生成器。
 * <p>
 * 基于 Twitter 的 Snowflake 雪花算法实现。
 * <pre>
 * 0    || 00000000000000000000000000000000000000000 || 00000        || 00000    || 000000000000
 * sign || System.currentTimeMillis()                || datacenterId || workerId || append number
 * </pre>
 * 位数解析：
 * <p>
 * 1. 第 1 位未使用（或作为符号位）
 * <p>
 * 2. 接下来的 41 位为毫秒级时间
 * <p>
 * 3. 然后 5 位是 datacenter 编号
 * <p>
 * 4. 再 5 位机器序号
 * <p>
 * 5. 最后 12 位是当前毫秒内的计数器
 * <p>
 * 将以上 1--5 的位数加起来，刚好是 64 比特位的 Long 型整数。
 * <p>
 * 此 ID 在毫秒级时间中自增排序，保证在整个分布式系统内不会产生 ID 碰撞（已由数据中心编号和工作机器序号隔离）。
 * <p>
 * 并且效率较高，经测试，此 ID 生成器每秒可产生约 26 万个 ID 数值，基本满足需要。
 */
public final class IdGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerator.class);

    // 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
    private final static long TWEPOCH = 1288834974657L;
    // 工作机器序号标识位数
    private final static long WORKER_ID_BITS = 5L;
    // 工作机器序号最大值 31L
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 数据中心编号标识位数
    private final static long DATACENTER_ID_BITS = 5L;
    // 数据中心编号最大值 31L
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    // 毫秒内自增序列号标识位数
    private final static long SEQUENCE_BITS = 12L;
    // 序列号最大值 4095L
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    // 工作机器序号向左偏移 12 位
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据中心编号向左偏移 17 位
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间毫秒向左偏移 22 位
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    /* 上次生产 ID 时间戳 */
    private static long lastTimestamp = -1L;

    private final long workerId;
    private final long datacenterId;

    // 0，并发控制
    private long sequence = 0L;

    public IdGenerator() {
        this.datacenterId = getDatacenterId();
        this.workerId = getMaxWorkerId(datacenterId);
    }

    /**
     * @param workerId     工作机器序号
     * @param datacenterId 数据中心编号
     */
    public IdGenerator(long workerId, long datacenterId) {
        Preconditions.checkArgument((workerId >= 0 && workerId <= MAX_WORKER_ID),
                "The value of workerId must be [%s, %s]", 0, MAX_WORKER_ID);
        Preconditions.checkArgument((datacenterId >= 0 && datacenterId <= MAX_DATACENTER_ID),
                "The value of datacenterId must be [%s, %s]", 0, MAX_DATACENTER_ID);
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 同步获取下一个 ID 值。
     */
    public synchronized long nextId() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - currentTimeMillis));
        }

        if (currentTimeMillis == lastTimestamp) {
            // 当前毫秒内，则序列号 +1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一毫秒
                currentTimeMillis = tilNextMillis(lastTimestamp);
            }
        } else {
            // 已是下一个毫秒，重置序列号
            sequence = 0L;
        }

        lastTimestamp = currentTimeMillis;

        // 通过偏移位标记，生成最终 ID
        long timeBit = ((currentTimeMillis - TWEPOCH) << TIMESTAMP_LEFT_SHIFT);
        long datacenterBit = datacenterId << DATACENTER_ID_SHIFT;
        long workBit = workerId << WORKER_ID_SHIFT;
        long seqBit = sequence;
        return timeBit | datacenterBit | workBit | seqBit;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    private static long getDatacenterId() {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (IdGenerator.MAX_DATACENTER_ID + 1);
            }
        } catch (Exception e) {
            LOGGER.error("无法从本机网络地址或 MAC 地址生成数据中心编号！", e);
        }
        return id;
    }

    private static long getMaxWorkerId(long datacenterId) {
        StringBuilder builder = new StringBuilder(String.valueOf(datacenterId));
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            /*
             * GET jvmPid
             */
            builder.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (builder.toString().hashCode() & 0xffff) % (IdGenerator.MAX_WORKER_ID + 1);
    }
}
