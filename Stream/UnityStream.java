package com.QYun.util.Stream;

import org.apache.commons.lang.ArrayUtils;

import javax.vecmath.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class UnityStream extends ByteStream {
    public UnityStream(int capacity) {
        super(capacity);
    }

    public UnityStream(Byte[] array) {
        super(array);
    }

    public UnityStream(byte[] array) {
        super(array);
    }

    public UnityStream(byte[] array, int offset, int length) {
        super(array, offset, length);
    }

    public UnityStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public UnityStream(ByteArrayOutputStream outputStream) {
        super(outputStream);
    }

    public UnityStream(File file) throws IOException {
        super(file);
    }

    public UnityStream(ByteBuffer buffer) {
        super(buffer);
    }

    public UnityStream(UnityStream reader) {
        super(reader.byteBuffer);
    }

    public Byte ReadByte() {
        return readByte();
    }

    public Boolean ReadBoolean() {
        return readBoolean();
    }

    public Short ReadInt16() {
        return readShort();
    }

    public Integer ReadInt32() {
        return readInt();
    }

    public Long ReadInt64() {
        return readLong();
    }

    public Short ReadUInt16() {
        return readShort();
    }

    public Integer ReadUInt32() {
        return readInt();
    }

    public Long ReadUInt64() {
        return readLong();
    }

    public Float ReadSingle() {
        return readFloat();
    }

    public Double ReadDouble() {
        return readDouble();
    }

    public void AlignStream() {
        AlignStream(4);
    }

    public void AlignStream(int alignment) {
        var mod = getPos() % alignment;
        if (mod != 0)
            setPos(getPos() + alignment - mod);
    }

    public String ReadAlignedString() {
        int length = readInt();
        if (length > 0 && length <= trueLen - getPos()) {
            var str = new String(ArrayUtils.toPrimitive(readBytes(length)), StandardCharsets.UTF_8);
            AlignStream();
            return str;
        }
        return "";
    }

    public String[] readStrings(int n) {
        return readArray(this::ReadAlignedString, new String[n]);
    }

    public String ReadStringToNull() {
        return ReadStringToNull(32767);
    }

    public String ReadStringToNull(int maxLength) {
        var bytes = new ByteArrayOutputStream();
        int i = 0;
        while (getPos() != capacity() && i < maxLength) {
            byte b = readByte();
            if (b == 0)
                break;
            bytes.write(b);
            i++;
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }

    public Quat4f ReadQuaternion() {
        return new Quat4f(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public Vector2f ReadVector2() {
        return new Vector2f(readFloat(), readFloat());
    }

    public Vector2f[] readVector2s(int n) {
        return readArray(this::ReadVector2, new Vector2f[n]);
    }

    public Vector3f ReadVector3() {
        return new Vector3f(readFloat(), readFloat(), readFloat());
    }

    public Vector4f ReadVector4() {
        return new Vector4f(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public Vector3f read4ToVector3() {
        var tmp = ReadVector4();
        return new Vector3f(tmp.x, tmp.y, tmp.z);
    }

    public Color4f ReadColor4() {
        return new Color4f(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public Matrix4f ReadMatrix() {
        return new Matrix4f(ArrayUtils.toPrimitive(readFloats(16)));
    }

    public Matrix4f[] readMatrices(int n) {
        return readArray(this::ReadMatrix, new Matrix4f[n]);
    }

    public Boolean[] ReadBooleanArray() {
        return readBooleans(readInt());
    }

    public Byte[] ReadUInt8Array() {
        return readBytes(readInt());
    }

    public Short[] ReadUInt16Array() {
        return readShorts(readInt());
    }

    public Integer[] ReadInt32Array() {
        return readInts(readInt());
    }

    public Integer[] ReadInt32Array(int n) {
        return readInts(n);
    }

    public Integer[] ReadUInt32Array() {
        return readInts(readInt());
    }

    public Integer[][] ReadUInt32ArrayArray() {
        var cap = readInt();
        return readArray(this::ReadUInt32Array, new Integer[cap][cap]);
    }

    public Integer[] ReadUInt32Array(int n) {
        return readArray(this::readInt, new Integer[n]);
    }

    public Float[] ReadSingleArray() {
        return readArray(this::readFloat, new Float[readInt()]);
    }

    public Float[] ReadSingleArray(int n) {
        return readArray(this::readFloat, new Float[n]);
    }

    public String[] ReadStringArray() {
        return readArray(this::ReadAlignedString, new String[readInt()]);
    }

    public Vector2f[] ReadVector2Array() {
        return readArray(this::ReadVector2, new Vector2f[readInt()]);
    }

    public Vector4f[] ReadVector4Array() {
        return readArray(this::ReadVector4, new Vector4f[readInt()]);
    }

    public Matrix4f[] ReadMatrixArray() {
        return readArray(this::ReadMatrix, new Matrix4f[readInt()]);
    }

    @Override
    public UnityStream setToReadOnly() {
        return new UnityStream(byteBuffer.asReadOnlyBuffer());
    }

    @Override
    public UnityStream setByteOrder(ByteOrder byteOrder) {
        super.setByteOrder(byteOrder);
        return this;
    }
}
