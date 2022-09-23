package com.github.mrzhqiang.helper.uppc;

/**
 * 提供用于处理原始字节序列的静态方法。
 * <p>
 * 来自网络的 079 版本源码，此处仅添加注释，未进行代码层面的改动。
 *
 * @author Frz
 * @version 1.0
 * @since Revision 206
 */
public final class BitTools {
    /**
     * 工具类的私有构造函数。
     * <p>
     * 作为工具类：
     * <p>
     * 1. 首先类名称使用复数形式，表示对相关类的辅助增强。
     * 对于已经是复数形式的相关类，则采用 More 前缀修饰，表示更强大的辅助。
     * 也可以用 Standard 前缀修饰，表示标准的辅助。
     * 其他前缀推荐如下：
     * <p>
     * 1.1 Normal 表示普通的辅助
     * <p>
     * 1.2 Simple 表示简单的辅助
     * <p>
     * 1.3 General 表示常规的辅助
     * <p>
     * 1.4 其他形容词
     * <p>
     * 2. 然后类本身用 final 修饰，表示不再有任何的子类继承，类中也只有静态方法，没有实例方法。
     * <p>
     * 3. 最后将构造函数声明为私有的访问修饰符，以防止工具类被实例化。
     * <p>
     * 4. 另外，根据 《Effective Java》 中文第二版中所描述的那样，为防止通过反射实例化工具类，
     * 还应该在构造方法中加入抛 Error 异常的代码。
     * <p>
     * 但我们自己使用的工具类，无需这样做。
     */
    private BitTools() {
        // no instances
    }

    /**
     * 读取短整数。
     * <p>
     * 从 byte 数组中读取指定下标位置的两个值组成 integer 类型的 short 值。
     * <p>
     * 返回值不会超过 16 位字节 == 65535
     * <p>
     * 运算过程：
     * <p>
     * i0 == 0000 0000 0000 0001 == 1
     * <p>
     * 0000 0000 0000 0001 & 0xFF == 1 -- 需要注意负整数转正整数
     * <p>
     * i1 == 0000 0000 0000 0010 == 2
     * <p>
     * 0000 0000 0000 0010 << 8 == 0000 0010 0000 0000 == 512
     * <p>
     * 0000 0010 0000 0000 & 0xFF00 == 512 -- 需要注意负整数转正整数
     * <p>
     * 0000 0000 0000 0001 | 0000 0010 0000 0000 == 0000 0010 0000 0001 == 513
     * <p>
     * 基本逻辑：
     * <p>
     * & 0xFF 表示将负整数转换为正整数--针对低位
     * <p>
     * 比如 -128 & 0xFF == 128  -1 & 0xFF == 255，实际上相当于转换 byte 为 [0--255] 的无符号整数
     * <p>
     * << 8 表示将 byte 放在 short 的 high 位
     * <p>
     * & 0xFF00 表示将负整数转为正整数--针对高位
     * <p>
     * 比如 -128 << 8 & 0xFF00 == 32768  -1 << 8 & 0xFF00 == 65280
     * <p>
     * |= 表示将两个值合并，由于第一个值 & 0x00FF，第二个值 & 0xFF00，所以实际上表示两个数相加
     * <p>
     * 而 [0,255] + [256,65280] 实际上是 [0,65535] 的取值范围，恰好是一个 short 值
     */
    public static int readShort(byte[] array, int index) {
        int ret = array[index];
        ret &= 0xFF;
        ret |= ((int) (array[index + 1]) << 8) & 0xFF00;
        return ret;
    }

    /**
     * 读取字符串。
     * <p>
     * 从 byte 数组中读取指定下标位置的指定长度的值组成 char 数组并包装为 String 值。
     * <p>
     * 返回的字符串内容将仅包含 Ascii 编码字符，这是因为 byte 取值范围为 [-128,127]
     * <p>
     * 基本逻辑：
     * <p>
     * i0 == 90 == A
     */
    public static String readString(byte[] array, int index, int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = (char) array[i + index];
        }
        return String.valueOf(chars);
    }

    /**
     * 读取冒险岛字符串。
     * <p>
     * 从数组中读取指定下标位置的冒险岛字符串。
     */
    public static String readMapleString(byte[] array, int index) {
//         int length = ((int) (array[index]) & 0xFF) | ((int) (array[index + 1] << 8) & 0xFF00);
        int length = readShort(array, index);
        return BitTools.readString(array, index + 2, length);
    }

    /**
     * 向左滚动。
     * <p>
     * 将 in 左起第 count+1 位移到最前面，并将移出的位数拼接到右侧尾部。
     * <p>
     * 比如：
     * <p>
     * in: 1100 1101 count: 3 == 0110 1110
     * <p>
     * 注意：不管 count 多大，左移始终以 8 位为基准，则 count % 8 为 [0,7]
     */
    public static byte rollLeft(byte in, int count) {
        int tmp = (int) in & 0xFF;
        tmp = tmp << (count % 8);
        return (byte) ((tmp & 0xFF) | (tmp >> 8));
    }

    /**
     * 将 in 右起第 count+1 位移到最后面，并将移出的位数拼接到左侧头部。
     * <p>
     * in: 1100 1101 count: 3 >>> 1011 1001
     */
    public static byte rollRight(byte in, int count) {
        int tmp = (int) in & 0xFF;
        tmp = (tmp << 8) >>> (count % 8);
        return (byte) ((tmp & 0xFF) | (tmp >>> 8));
    }

    /**
     * 将 in 数组根据 count 数量重复 mul 倍数内容。
     */
    public static byte[] multiplyBytes(byte[] in, int count, int mul) {
        byte[] ret = new byte[count * mul];
        for (int x = 0; x < count * mul; x++) {
            ret[x] = in[x % count];
        }
        return ret;
    }

    /**
     * 将双精度浮点整数转换为整数。
     */
    public static int doubleToShortBits(double d) {
        long longBits = Double.doubleToLongBits(d);
        return (int) (longBits >> 48);
    }
}
