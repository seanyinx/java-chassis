/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.servicecomb.foundation.vertx.stream;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

/**
 *  本stream不必close
 * @author  
 *
 */
public class BufferInputStream extends InputStream {
    private ByteBuf byteBuf;

    /**
     * 构造
     * @param buffer  buffer
     */
    public BufferInputStream(ByteBuf buffer) {
        this.byteBuf = buffer;
    }

    @Override
    public long skip(long len) {
        int skipLen = Math.min((int) len, available());
        byteBuf.skipBytes(skipLen);
        return skipLen;
    }

    /**
     * readByte
     * @return  byte
     */
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public int read() {
        return byteBuf.readByte();
    }

    /**
     * readBoolean
     * @return   boolean
     */
    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    /**
     * readShort
     * @return   short
     */
    public short readShort() {
        return byteBuf.readShort();
    }

    /**
     * readInt
     * @return   int
     */
    public int readInt() {
        return byteBuf.readInt();
    }

    /**
     * readLong
     * @return   long
     */
    public long readLong() {
        return byteBuf.readLong();
    }

    public int getIndex() {
        return byteBuf.readerIndex();
    }

    /**
     * readString
     * @return   string
     */
    public String readString() {
        int length = readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public int read(byte[] b) {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        int avail = available();
        if (len > avail) {
            len = avail;
        }

        if (len == 0) {
            return -1;
        }

        byteBuf.readBytes(b, off, len);
        return len;
    }

    @Override
    public int available() {
        return byteBuf.readableBytes();
    }

    @Override
    public void close() {
    }
}
