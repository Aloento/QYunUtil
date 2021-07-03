package com.QYun.util.Stream;

import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Supplier;

public class ByteStream extends ByteBufferWrapper {
    public int trueLen;

    public ByteStream(int capacity) {
        super(capacity);
    }

    public ByteStream(byte[] array) {
        super(array);
        trueLen = array.length;
    }

    public ByteStream(Byte[] array) {
        super(array);
        trueLen = array.length;
    }

    public ByteStream(byte[] array, int offset, int length) {
        super(array, offset, length);
        trueLen = offset + length;
    }

    public ByteStream(InputStream inputStream) throws IOException {
        super(inputStream);
        trueLen = inputStream.available();
    }

    public ByteStream(ByteArrayOutputStream outputStream) {
        super(outputStream);
        trueLen = outputStream.size();
    }

    public ByteStream(ByteBuffer buffer) {
        super(buffer);
        trueLen = buffer.capacity();
    }

    public ByteStream(File file) throws IOException {
        super(file);
        trueLen = Math.toIntExact(file.length());
    }

    public void readFully(byte[] b) {
        readFully(b, 0, b.length);
    }

    public void readFully(byte[] b, int off, int len) {
        byteBuffer.get(b, off, len);
    }

    public int skipBytes(int n) {
        if (n <= 0)
            return 0;

        int avail = byteBuffer.capacity() - byteBuffer.position();
        if (avail <= 0)
            return 0;

        int skipped = Math.min(avail, n);
        byteBuffer.position(byteBuffer.position() + skipped);
        return skipped;
    }

    public Boolean readBoolean() {
        return 0 != byteBuffer.get();
    }

    public Boolean[] readBooleans(int n) {
        return readArray(this::readBoolean, new Boolean[n]);
    }

    public Byte readByte() {
        return byteBuffer.get();
    }

    public Byte[] readBytes(int n) {
        return readArray(this::readByte, new Byte[n]);
    }

    public Integer readUnsignedByte() {
        return (int) byteBuffer.get();
    }

    public Short readShort() {
        return byteBuffer.getShort();
    }

    public Short[] readShorts(int n) {
        return readArray(this::readShort, new Short[n]);
    }

    public Integer readUnsignedShort() {
        return (int) byteBuffer.getShort();
    }

    public Character readChar() {
        return byteBuffer.getChar();
    }

    public Integer readInt() {
        return byteBuffer.getInt();
    }

    public Integer[] readInts(int n) {
        return readArray(this::readInt, new Integer[n]);
    }

    public Long readLong() {
        return byteBuffer.getLong();
    }

    public Float readFloat() {
        return byteBuffer.getFloat();
    }

    public Float[] readFloats(int n) {
        return readArray(this::readFloat, new Float[n]);
    }

    public Double readDouble() {
        return byteBuffer.getDouble();
    }

    @Deprecated
    public String readLine() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public String readUTF() {
        throw new UnsupportedOperationException();
    }

    public void write(int b) {
        byteBuffer.put((byte) b);
        trueLen++;
    }

    public void write(Integer b) {
        byteBuffer.put(b.byteValue());
        trueLen++;
    }

    public void write(byte[] b) {
        byteBuffer.put(b);
        trueLen += b.length;
    }

    public void write(Byte[] b) {
        byteBuffer.put(ArrayUtils.toPrimitive(b));
        trueLen += b.length;
    }

    public void write(byte[] b, int off, int len) {
        byteBuffer.put(b, off, len);
        trueLen += off + len;
    }

    public void write(Byte[] b, int off, int len) {
        byteBuffer.put(ArrayUtils.toPrimitive(b), off, len);
        trueLen += off + len;
    }

    public void writeBoolean(Boolean v) {
        byteBuffer.put((byte) (v ? 1 : 0));
        trueLen++;
    }

    public void writeByte(Integer v) {
        byteBuffer.put(v.byteValue());
        trueLen++;
    }

    public void writeShort(Integer v) {
        byteBuffer.putShort(v.shortValue());
        trueLen += 2;
    }

    public void writeChar(Integer v) {
        byteBuffer.putChar((char) v.shortValue());
        trueLen += 2;
    }

    public void writeInt(Integer v) {
        byteBuffer.putInt(v);
        trueLen += 4;
    }

    public void writeLong(Long v) {
        byteBuffer.putLong(v);
        trueLen += 8;
    }

    public void writeFloat(Float v) {
        byteBuffer.putFloat(v);
        trueLen += 4;
    }

    public void writeDouble(Double v) {
        byteBuffer.putDouble(v);
        trueLen += 8;
    }

    public void writeBytes(String s) {
        for (int i = 0; i < s.length(); i++)
            byteBuffer.put((byte) s.charAt(i));
        trueLen += s.length();
    }

    public void writeChars(String s) {
        for (int i = 0; i < s.length(); i++)
            byteBuffer.putChar(s.charAt(i));
        trueLen += s.length();
    }

    public void writeUTF(String s) {
        throw new UnsupportedOperationException();
    }

    public ByteStream setToReadOnly() {
        return new ByteStream(byteBuffer.asReadOnlyBuffer());
    }

    public ByteStream setByteOrder(ByteOrder byteOrder) {
        super.setByteOrder(byteOrder);
        return this;
    }

    public <T> T[] readArray(Supplier<T> supplier, T[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
        return array;
    }

    public void copyTo(@NotNull ByteStream dest, long size) {
        dest.write(readBytes(Math.min(trueLen, Math.toIntExact(size))));
    }
}
