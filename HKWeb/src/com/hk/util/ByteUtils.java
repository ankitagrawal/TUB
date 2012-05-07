package com.hk.util;


/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 2/7/12
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class ByteUtils {

    public static long convertToLong(byte[] data) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value <<= 8;
            value ^= (long) data[i] & 0xFF;
        }
        return value;
    }

    public static int convertToInt(byte[] data) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value <<= 8;
            value ^= (long) data[i] & 0xFF;
        }
        return value;
    }

    public static byte[] reverseBytes(byte[] input) {
        byte[] output = new byte[input.length];
        int index = 1;
        for (byte in : input) {
            output[input.length - index] = in;
            index++;
        }
        return output;
    }
}
