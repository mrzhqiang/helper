package com.github.mrzhqiang.helper.third;

import java.util.function.Consumer;

/**
 * IP 地理位置消费者。
 * <p>
 * 通过 IP 地址获取对应的地理位置。
 */
public interface IpLocationConsumer {

    /**
     * 接收 IP 地址，通过耗时操作转为地理位置数据。
     *
     * @param ip       ip 地址。
     * @param consumer 消费者。主要查找可能比较耗时，所以需要跳过回调来获取数据。
     */
    void accept(String ip, Consumer<IpLocationData> consumer);
}
