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
package io.servicecomb.foundation.vertx.server;

import java.io.UnsupportedEncodingException;

import io.servicecomb.foundation.vertx.tcp.TcpOutputStream;
import org.junit.Assert;
import org.junit.Test;

import io.vertx.core.buffer.Buffer;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author
 * @version  [版本号, 2017年5月10日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TestTcpParser {
    long msgId;

    Buffer headerBuffer;

    Buffer bodyBuffer;

    @Test
    public void test() throws UnsupportedEncodingException {
        TcpBufferHandler output = new TcpBufferHandler() {
            @Override
            public void handle(long _msgId, Buffer _headerBuffer, Buffer _bodyBuffer) {
                msgId = _msgId;
                headerBuffer = _headerBuffer;
                bodyBuffer = _bodyBuffer;
            }
        };

        byte[] header = new byte[] {1, 2, 3};
        byte[] body = new byte[] {1, 2, 3, 4};
        TcpOutputStream os = new TcpOutputStream(1);
        os.writeInt(header.length + body.length);
        os.writeInt(header.length);
        os.write(header);
        os.write(body);

        TcpParser parser = new TcpParser(output);
        parser.handle(os.getBuffer());
        os.close();

        Assert.assertEquals(1, msgId);
        Assert.assertArrayEquals(header, headerBuffer.getBytes());
        Assert.assertArrayEquals(body, bodyBuffer.getBytes());
    }
}
