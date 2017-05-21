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

package io.servicecomb.transport.rest.servlet;

import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.servicecomb.common.rest.AbstractRestServer;
import io.servicecomb.common.rest.RestConst;
import io.servicecomb.common.rest.codec.RestServerRequestInternal;
import io.servicecomb.common.rest.codec.produce.ProduceProcessor;
import io.servicecomb.core.Invocation;
import io.servicecomb.core.Response;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author   
 * @version  [版本号, 2017年1月9日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ServletRestServer extends AbstractRestServer<HttpServletResponse> {
    protected RestAsyncListener restAsyncListener = new RestAsyncListener();

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param request
     * @param response
     * @throws Exception
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
        // 异步场景
        final AsyncContext asyncCtx = request.startAsync();
        asyncCtx.addListener(restAsyncListener);
        asyncCtx.setTimeout(ServletConfig.getServerTimeout());

        RestServerRequestInternal restRequest = new RestServletHttpRequest(request, asyncCtx);
        handleRequest(restRequest, response);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void doSendResponse(HttpServletResponse httpServerResponse, ProduceProcessor produceProcessor,
            Response response) throws Exception {
        httpServerResponse.setStatus(response.getStatusCode(), response.getReasonPhrase());
        httpServerResponse.setContentType(produceProcessor.getName());

        if (response.getHeaders().getHeaderMap() != null) {
            for (Entry<String, List<Object>> entry : response.getHeaders().getHeaderMap().entrySet()) {
                for (Object value : entry.getValue()) {
                    httpServerResponse.addHeader(entry.getKey(), String.valueOf(value));
                }
            }
        }

        // 直接写到stream中去，避免重复分配内存，这是chunk模式，不必设置contentLength
        // TODO:设置buffer大小，这很影响性能
        if (response.getResult() != null) {
            OutputStream output = httpServerResponse.getOutputStream();
            produceProcessor.encodeResponse(output, response.getResult());
        }
        httpServerResponse.flushBuffer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setHttpRequestContext(Invocation invocation, RestServerRequestInternal restRequest) {
        invocation.getHandlerContext().put(RestConst.HTTP_REQUEST_CREATOR,
                new ProducerServletHttpRequestArgMapper(restRequest.getHttpRequest()));
    }
}
