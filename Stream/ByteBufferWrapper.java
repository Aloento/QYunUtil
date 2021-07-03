package com.QYun.util.Stream;

import org.apache.commons.lang.ArrayUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class ByteBufferWrapper {
    ByteBuffer byteBuffer;

    public ByteBufferWrapper(int capacity) {
        byteBuffer = ByteBuffer.allocate(capacity);
    }

    public ByteBufferWrapper(byte[] array) {
        byteBuffer = ByteBuffer.wrap(array);
    }

    public ByteBufferWrapper(Byte[] array) {
        byteBuffer = ByteBuffer.wrap(ArrayUtils.toPrimitive(array));
    }

    public ByteBufferWrapper(byte[] array, int offset, int length) {
        byteBuffer = ByteBuffer.wrap(array, offset, length);
    }

    public ByteBufferWrapper(InputStream inputStream) throws IOException {
        this(inputStream.readAllBytes());
    }

    public ByteBufferWrapper(ByteArrayOutputStream outputStream) {
        this(outputStream.toByteArray());
    }

    public ByteBufferWrapper(ByteBuffer buffer) {
        byteBuffer = buffer;
    }

    public ByteBufferWrapper(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public void rewind() {
        byteBuffer.rewind();
    }

    public void mark() {
        byteBuffer.mark();
    }

    public void reset() {
        byteBuffer.reset();
    }

    public int getPos() {
        return byteBuffer.position();
    }

    public void setPos(int pos) {
        byteBuffer.position(pos);
    }

    public int capacity() {
        return byteBuffer.capacity();
    }

    public abstract ByteBufferWrapper setToReadOnly();

    public ByteBufferWrapper setByteOrder(ByteOrder byteOrder) {
        byteBuffer.order(byteOrder);
        return this;
    }

    public ByteArrayInputStream toInputStreamWithPos() {
        return new ByteArrayInputStream(byteBuffer.array(), getPos(), byteBuffer.capacity());
    }

    public ByteArrayOutputStream toOutputStream() {
        var tmp = new ByteArrayOutputStream(byteBuffer.capacity());
        tmp.writeBytes(byteBuffer.array());
        return tmp;
    }
}
