package com.ewing.order.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @since 2016年3月2日
 *
 */
public class AutoExpandByteBuffer {

    private int capacity = 4096;
    private float factor = 0.75f;
    private int position = 0;
    private byte[] bucket;

    public AutoExpandByteBuffer() {
        bucket = new byte[capacity];
    }

    public AutoExpandByteBuffer(int capacity) {
        this.capacity = capacity;
        this.bucket = new byte[capacity];
    }

    public AutoExpandByteBuffer(int capacity, float factor) {
        this.capacity = capacity;
        this.factor = factor;
        this.bucket = new byte[capacity];
    }

    public void put(byte b) {
        checkCapacity(1);

        bucket[position++] = b;
    }

    public void put(byte[] bte) {
        checkCapacity(bte.length);

        System.arraycopy(bte, 0, bucket, position, bte.length);
        position += bte.length;
    }

    public void put(byte[] bte, int offset, int length) {
        checkCapacity(length);

        System.arraycopy(bte, offset, bucket, position, length);
        position += length;
    }

    private void checkCapacity(int len) {
        if ((capacity - position) < len) {
            byte[] ext = new byte[(capacity + (int) (capacity * factor))];
            System.arraycopy(bucket, 0, ext, 0, bucket.length);
            bucket = ext;
            capacity = ext.length;

            checkCapacity(len);
        }
    }

    public int avaliable() {
        return position;
    }

    public String asString(String charsetName) {
        try {
            return new String(bucket, 0, position, charsetName);
        } catch (UnsupportedEncodingException e) {

            return null;
        }
    }

    public String asString(Charset charset) {
        return new String(bucket, 0, position, charset);
    }
}
