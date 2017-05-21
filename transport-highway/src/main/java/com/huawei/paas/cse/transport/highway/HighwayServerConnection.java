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
package com.huawei.paas.cse.transport.highway;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.paas.cse.transport.highway.message.LoginRequest;
import com.huawei.paas.cse.transport.highway.message.LoginResponse;
import com.huawei.paas.cse.transport.highway.message.RequestHeader;
import com.huawei.paas.cse.transport.highway.message.ResponseHeader;
import io.servicecomb.foundation.vertx.server.TcpBufferHandler;
import io.servicecomb.foundation.vertx.server.TcpParser;
import io.servicecomb.foundation.vertx.server.TcpServerConnection;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author
 * @version  [版本号, 2017年5月8日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HighwayServerConnection extends TcpServerConnection implements TcpBufferHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HighwayServerConnection.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(NetSocket netSocket) {
        splitter = new TcpParser(this);
        super.init(netSocket);
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void handle(long msgId, Buffer headerBuffer, Buffer bodyBuffer) {
        RequestHeader requestHeader = decodeRequestHeader(msgId, headerBuffer);
        if (requestHeader == null) {
            return;
        }

        switch (requestHeader.getMsgType()) {
            case MsgType.REQUEST:
                onRequest(msgId, requestHeader, bodyBuffer);
                break;
            case MsgType.LOGIN:
                onLogin(msgId, requestHeader, bodyBuffer);
                break;

            default:
                throw new Error("Unknown tcp msgType " + requestHeader.getMsgType());
        }
    }

    protected RequestHeader decodeRequestHeader(long msgId, Buffer headerBuffer) {
        RequestHeader requestHeader = null;
        try {
            requestHeader = HighwayCodec.readRequestHeader(headerBuffer);
        } catch (Exception e) {
            String msg = String.format("decode request header error, msgId=%d",
                    msgId);
            LOGGER.error(msg, e);

            netSocket.close();
            return null;
        }

        return requestHeader;
    }

    protected void onLogin(long msgId, RequestHeader header, Buffer bodyBuffer) {
        LoginRequest request = null;
        try {
            request = LoginRequest.readObject(bodyBuffer);
        } catch (Exception e) {
            String msg = String.format("decode setParameter error, msgId=%d",
                    msgId);
            LOGGER.error(msg, e);
            netSocket.close();
            return;
        }

        if (request != null) {
            this.setProtocol(request.getProtocol());
            this.setZipName(request.getZipName());
        }

        try (HighwayOutputStream os = new HighwayOutputStream(msgId)) {
            ResponseHeader responseHeader = new ResponseHeader();
            responseHeader.setStatusCode(Status.OK.getStatusCode());

            LoginResponse response = new LoginResponse();
            os.write(ResponseHeader.getResponseHeaderSchema(),
                    responseHeader,
                    LoginResponse.getLoginResponseSchema(),
                    response);
            netSocket.write(os.getBuffer());
        } catch (Exception e) {
            throw new Error("impossible.", e);
        }
    }

    protected void onRequest(long msgId, RequestHeader header, Buffer bodyBuffer) {
        HighwayServerInvoke invoke = new HighwayServerInvoke();
        if (invoke.init(netSocket, msgId, header, bodyBuffer)) {
            invoke.execute();
        }
    }
}
