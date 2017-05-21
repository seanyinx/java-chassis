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

import java.util.concurrent.Executor;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import io.servicecomb.codec.protobuf.definition.OperationProtobuf;
import io.servicecomb.codec.protobuf.definition.ProtobufManager;
import com.huawei.paas.cse.core.AsyncResponse;
import com.huawei.paas.cse.core.Endpoint;
import com.huawei.paas.cse.core.Invocation;
import com.huawei.paas.cse.core.definition.OperationMeta;
import com.huawei.paas.cse.transport.highway.message.LoginRequest;
import com.huawei.paas.cse.transport.highway.message.RequestHeader;
import com.huawei.paas.foundation.vertx.VertxUtils;
import com.huawei.paas.foundation.vertx.client.ClientPoolManager;
import com.huawei.paas.foundation.vertx.client.tcp.TcpClientConfig;
import com.huawei.paas.foundation.vertx.client.tcp.TcpClientPool;
import com.huawei.paas.foundation.vertx.server.TcpParser;
import com.huawei.paas.foundation.vertx.tcp.TcpOutputStream;

import io.netty.buffer.ByteBuf;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class TestHighwayClient {
    private TcpClientConfig options;

    HighwayClient client = new HighwayClient(true);

    Invocation invocation = Mockito.mock(Invocation.class);

    AsyncResponse asyncResp = Mockito.mock(AsyncResponse.class);

    OperationProtobuf operationProtobuf = Mockito.mock(OperationProtobuf.class);

    OperationMeta operationMeta = Mockito.mock(OperationMeta.class);

    Endpoint endpoint = Mockito.mock(Endpoint.class);

    Executor excutor = Mockito.mock(Executor.class);

    @Test
    public void testHighwayClientSSL(@Mocked Vertx vertx) throws Exception {
        new MockUp<VertxUtils>() {
            @Mock
            <CLIENT_POOL, CLIENT_OPTIONS> DeploymentOptions createClientDeployOptions(
                    ClientPoolManager<CLIENT_POOL> clientMgr,
                    int instanceCount,
                    int poolCountPerVerticle, CLIENT_OPTIONS clientOptions) {
                options = (TcpClientConfig) clientOptions;
                return null;
            }

            @Mock
            <VERTICLE extends AbstractVerticle> boolean blockDeploy(Vertx vertx,
                    Class<VERTICLE> cls,
                    DeploymentOptions options) throws InterruptedException {
                return true;
            }
        };

        client.init(vertx);
        Assert.assertEquals(options.isSsl(), true);
    }

    @Test
    public void testSend() {
        boolean status = true;
        mockUps();

        Mockito.when(invocation.getOperationMeta()).thenReturn(operationMeta);
        Mockito.when(invocation.getEndpoint()).thenReturn(endpoint);
        Mockito.when(invocation.getEndpoint().getEndpoint()).thenReturn("endpoint");
        Mockito.when(invocation.getResponseExecutor()).thenReturn(excutor);

        try {
            client.send(invocation, asyncResp);
        } catch (Exception e) {
            status = false;
        }
        Assert.assertTrue(status);

    }

    @Test
    public void testCreateLogin() throws Exception {
        HighwayClient client = new HighwayClient(false);
        TcpOutputStream os = client.createLogin();
        ByteBuf buf = os.getBuffer().getByteBuf();

        byte[] magic = new byte[TcpParser.TCP_MAGIC.length];
        buf.readBytes(magic);
        Assert.assertArrayEquals(TcpParser.TCP_MAGIC, magic);
        Assert.assertEquals(os.getMsgId(), buf.readLong());

        int start = TcpParser.TCP_HEADER_LENGTH;
        int totalLen = buf.readInt();
        int headerLen = buf.readInt();
        Buffer headerBuffer =
            os.getBuffer().slice(start, start + headerLen);
        int end = start + totalLen;
        start += headerLen;
        Buffer bodyBuffer = os.getBuffer().slice(start, end);

        RequestHeader header = RequestHeader.readObject(headerBuffer);
        Assert.assertEquals(MsgType.LOGIN, header.getMsgType());

        LoginRequest login = LoginRequest.readObject(bodyBuffer);
        Assert.assertEquals(HighwayTransport.NAME, login.getProtocol());
    }

    private void mockUps() {

        new MockUp<ClientPoolManager<TcpClientPool>>() {

            @Mock
            public TcpClientPool findThreadBindClientPool() {
                return Mockito.mock(TcpClientPool.class);
            }

        };

        new MockUp<ProtobufManager>() {
            @Mock
            public OperationProtobuf getOrCreateOperation(OperationMeta operationMeta) throws Exception {
                return operationProtobuf;
            }

        };

        new MockUp<HighwayCodec>() {
            @Mock
            public Buffer encodeRequest(Invocation invocation, OperationProtobuf operationProtobuf,
                    long msgId) throws Exception {
                return null;
            }

        };

    }
}
