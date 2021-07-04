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

    public void alignStream() {
        alignStream(4);
    }

    public void alignStream(int alignment) {
        var mod = getPos() % alignment;
        if (mod != 0)
            setPos(getPos() + alignment - mod);
    }

    public String readAlignedString() {
        int length = readInt();
        if (length > 0 && length <= trueLen - getPos()) {
            var str = new String(ArrayUtils.toPrimitive(readBytes(length)), StandardCharsets.UTF_8);
            alignStream();
            return str;
        }
        return "";
    }

    public String[] readStringArray() {
        return readArray(this::readAlignedString, new String[readInt()]);
    }

    public String[] readStringArray(int n) {
        return readArray(this::readAlignedString, new String[n]);
    }

    public String readStringToNull() {
        return readStringToNull(32767);
    }

    public String readStringToNull(int maxLength) {
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

    public Quat4f readQuaternion() {
        return new Quat4f(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public Vector2f readVector2() {
        return new Vector2f(readFloat(), readFloat());
    }

    public Vector2f[] readVector2Array() {
        return readArray(this::readVector2, new Vector2f[readInt()]);
    }

    public Vector3f readVector3() {
        return new Vector3f(readFloat(), readFloat(), readFloat());
    }

    public Vector4f readVector4() {
        return new Vector4f(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public Vector4f[] readVector4Array() {
        return readArray(this::readVector4, new Vector4f[readInt()]);
    }

    public Vector3f read4ToVector3() {
        var tmp = readVector4();
        return new Vector3f(tmp.x, tmp.y, tmp.z);
    }

    public Color4f readColor4() {
        return new Color4f(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public Matrix4f readMatrix() {
        return new Matrix4f(ArrayUtils.toPrimitive(readFloats(16)));
    }

    public Matrix4f[] readMatrixArray() {
        return readArray(this::readMatrix, new Matrix4f[readInt()]);
    }

    public Boolean[] readBooleanArray() {
        return readBooleans(readInt());
    }

    public Byte[] readByteArray() {
        return readBytes(readInt());
    }

    public Short[] readShortArray() {
        return readShorts(readInt());
    }

    public Integer[] readIntArray() {
        return readInts(readInt());
    }

    public Integer[] readIntArray(int n) {
        return readInts(n);
    }

    public Integer[][] readIntArrayArray() {
        var cap = readInt();
        return readArray(this::readIntArray, new Integer[cap][cap]);
    }

    public Float[] readFloatArray() {
        return readFloats(readInt());
    }

    public Float[] readFloatArray(int n) {
        return readFloats(n);
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
