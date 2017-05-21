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

package io.servicecomb.codec.protobuf.utils;

import java.nio.ByteBuffer;

import io.protostuff.ByteBufferInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.vertx.core.buffer.Buffer;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author   
 * @version  [版本号, 2016年12月16日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface WrapSchema {
    @SuppressWarnings("unchecked")
    default <T> T readObject(Buffer buffer) throws Exception {
        if (buffer == null || buffer.length() == 0) {
            // void以及函数入参为null的场景
            // 空串时,protobuf至少为编码为1字节
            return (T) readFromEmpty();
        }

        ByteBuffer nioBuffer = buffer.getByteBuf().nioBuffer();
        Input input = new ByteBufferInput(nioBuffer, false);

        return (T) readObject(input);
    }

    Object readFromEmpty();

    Object readObject(Input input) throws Exception;

    void writeObject(Output output, Object value) throws Exception;
}
