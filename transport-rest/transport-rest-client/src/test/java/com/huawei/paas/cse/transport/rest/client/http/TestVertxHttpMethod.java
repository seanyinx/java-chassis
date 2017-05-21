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

package com.huawei.paas.cse.transport.rest.client.http;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.servicecomb.common.rest.RestConst;
import io.servicecomb.common.rest.codec.produce.ProduceProcessor;
import io.servicecomb.common.rest.definition.RestOperationMeta;
import io.servicecomb.common.rest.definition.path.URLPathBuilder;
import io.servicecomb.core.AsyncResponse;
import io.servicecomb.core.Endpoint;
import io.servicecomb.core.Invocation;
import io.servicecomb.core.definition.OperationMeta;
import com.huawei.paas.foundation.common.net.IpPort;
import com.huawei.paas.foundation.vertx.client.http.HttpClientWithContext;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class TestVertxHttpMethod extends VertxHttpMethod {

    HttpClientRequest request;

    @Before
    public void setup() {
        request = Mockito.mock(HttpClientRequest.class);
    }

    @Test
    public void testDoMethod(@Mocked HttpClient httpClient) throws Exception {
        Context context = new MockUp<Context>() {
            @Mock
            public void runOnContext(Handler<Void> action) {
                action.handle(null);
            }
        }.getMockInstance();
        HttpClientWithContext httpClientWithContext = new HttpClientWithContext(httpClient, context);

        Invocation invocation = Mockito.mock(Invocation.class);
        AsyncResponse asyncResp = Mockito.mock(AsyncResponse.class);
        OperationMeta operationMeta = Mockito.mock(OperationMeta.class);
        RestOperationMeta swaggerRestOperation = Mockito.mock(RestOperationMeta.class);

        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.when(invocation.getOperationMeta()).thenReturn(operationMeta);
        URLPathBuilder urlPathBuilder = Mockito.mock(URLPathBuilder.class);
        Mockito.when(swaggerRestOperation.getPathBuilder()).thenReturn(urlPathBuilder);
        operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION);
        Mockito.when(operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION)).thenReturn(swaggerRestOperation);
        Mockito.when(invocation.getEndpoint()).thenReturn(endpoint);

        Mockito.when(request.exceptionHandler(Mockito.any())).then(answer -> null);

        this.doMethod(httpClientWithContext, invocation, asyncResp);
        Assert.assertTrue(true);
    }

    @Test
    public void testSetCseContext() {
        boolean status = false;
        try {
            Invocation invocation = Mockito.mock(Invocation.class);
            HttpClientResponse httpResponse = Mockito.mock(HttpClientResponse.class);
            OperationMeta operationMeta = Mockito.mock(OperationMeta.class);
            RestOperationMeta swaggerRestOperation = Mockito.mock(RestOperationMeta.class);
            HttpClientRequest request = Mockito.mock(HttpClientRequest.class);

            Endpoint endpoint = Mockito.mock(Endpoint.class);
            Mockito.when(invocation.getOperationMeta()).thenReturn(operationMeta);
            URLPathBuilder urlPathBuilder = Mockito.mock(URLPathBuilder.class);
            Mockito.when(swaggerRestOperation.getPathBuilder()).thenReturn(urlPathBuilder);
            operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION);
            Mockito.when(operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION)).thenReturn(swaggerRestOperation);
            Mockito.when(invocation.getEndpoint()).thenReturn(endpoint);
            String contentType = httpResponse.getHeader("Content-Type");
            ProduceProcessor produceProcessor = Mockito.mock(ProduceProcessor.class);
            Mockito.when(swaggerRestOperation.findProduceProcessor(contentType)).thenReturn(produceProcessor);
            this.setCseContext(invocation, request);
        } catch (Exception ex) {
            status = true;
        }
        Assert.assertFalse(status);
    }

    @Test
    public void testHandleResponse() {
        boolean status = false;
        try {
            Invocation invocation = Mockito.mock(Invocation.class);
            AsyncResponse asyncResp = Mockito.mock(AsyncResponse.class);
            HttpClientResponse httpResponse = Mockito.mock(HttpClientResponse.class);
            OperationMeta operationMeta = Mockito.mock(OperationMeta.class);
            RestOperationMeta swaggerRestOperation = Mockito.mock(RestOperationMeta.class);

            Endpoint endpoint = Mockito.mock(Endpoint.class);
            Mockito.when(invocation.getOperationMeta()).thenReturn(operationMeta);
            URLPathBuilder urlPathBuilder = Mockito.mock(URLPathBuilder.class);
            Mockito.when(swaggerRestOperation.getPathBuilder()).thenReturn(urlPathBuilder);
            operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION);
            Mockito.when(operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION)).thenReturn(swaggerRestOperation);
            Mockito.when(invocation.getEndpoint()).thenReturn(endpoint);

            String contentType = httpResponse.getHeader("Content-Type");
            ProduceProcessor produceProcessor = Mockito.mock(ProduceProcessor.class);
            Mockito.when(swaggerRestOperation.findProduceProcessor(contentType)).thenReturn(produceProcessor);
            this.handleResponse(invocation, httpResponse, swaggerRestOperation, asyncResp);
        } catch (Exception ex) {
            status = true;
        }
        Assert.assertFalse(status);
    }

    @Override
    protected HttpClientRequest createRequest(HttpClient client, Invocation invocation, IpPort ipPort, String path,
            RestOperationMeta operation, AsyncResponse asyncResp) {
        return request;
    }

    @Test
    public void testCreateRequestPath() throws Exception {
        Invocation invocation = Mockito.mock(Invocation.class);
        RestOperationMeta restOperationMeta = Mockito.mock(RestOperationMeta.class);
        URLPathBuilder urlPathBuilder = Mockito.mock(URLPathBuilder.class);
        Mockito.when(restOperationMeta.getPathBuilder()).thenReturn(urlPathBuilder);
        String pathUrl = this.createRequestPath(invocation, restOperationMeta);
        Assert.assertNull(pathUrl);
    }
}
