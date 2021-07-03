package com.QYun.util.Stream;

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
            var str = new String(readBytes(length), StandardCharsets.UTF_8);
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
        return new Matrix4f(readFloats(16));
    }

    public Matrix4f[] readMatrices(int n) {
        return readArray(this::ReadMatrix, new Matrix4f[n]);
    }

    public boolean[] ReadBooleanArray() {
        return readBooleans(readInt());
    }

    public byte[] ReadUint8Array() {
        return readBytes(readInt());
    }

    public short[] ReadUint16Array() {
        return readShorts(readInt());
    }

    public int[] ReadInt32Array() {
        return readInts(readInt());
    }

    public int[] ReadInt32Array(int n) {
        return readInts(n);
    }

    public int[] ReadUint32Array() {
        return readInts(readInt());
    }

    public int[][] ReadUint32ArrayArray() {
        return readArray(this::ReadUint32Array, new Integer[readInt()][]);
    }

    public int[] ReadUint32Array(, int length) {
        return ReadArray(reader.Readint32, length);
    }

    public float[] ReadSingleArray() {
        return ReadArray(reader.ReadSingle, reader.ReadInt32());
    }

    public float[] ReadSingleArray(, int length) {
        return ReadArray(reader.ReadSingle, length);
    }

    public String[] ReadStringArray() {
        return ReadArray(reader.ReadAlignedString, reader.ReadInt32());
    }

    public Vector2f[] ReadVector2Array() {
        return ReadArray(reader.ReadVector2, reader.ReadInt32());
    }

    public Vector4f[] ReadVector4Array() {
        return ReadArray(reader.ReadVector4, reader.ReadInt32());
    }

    public Matrix4f[] ReadMatrixArray() {
        return ReadArray(reader.ReadMatrix, reader.ReadInt32());
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
