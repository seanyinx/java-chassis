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

package com.huawei.paas.cse.transport.rest.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.servicecomb.core.Const;
import io.servicecomb.core.CseContext;
import io.servicecomb.core.Endpoint;
import io.servicecomb.core.transport.AbstractTransport;
import com.huawei.paas.foundation.common.net.URIEndpointObject;
import com.huawei.paas.foundation.ssl.SSLCustom;
import com.huawei.paas.foundation.ssl.SSLOption;
import com.huawei.paas.foundation.ssl.SSLOptionFactory;
import com.huawei.paas.foundation.vertx.VertxTLSBuilder;
import com.netflix.config.DynamicPropertyFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

/**
 * 设置rest server监听端口
 * @author   
 * @version  [版本号, 2017年1月2日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RestServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServerVerticle.class);

    private static final String SSL_KEY = "rest.provider";

    private static final int ACCEPT_BACKLOG = 2048;

    private static final int SEND_BUFFER_SIZE = 4096;

    private static final int RECEIVE_BUFFER_SIZE = 4096;

    private Endpoint endpoint;

    private URIEndpointObject endpointObject;

    private RestBodyHandler bodyHandler = new RestBodyHandler();

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.endpoint = (Endpoint) context.config().getValue(AbstractTransport.ENDPOINT_KEY);
        this.endpointObject = (URIEndpointObject) endpoint.getAddress();

        String uploadsDirectory =
            DynamicPropertyFactory.getInstance().getStringProperty("cse.uploads.directory", null).get();
        bodyHandler.setUploadsDirectory(uploadsDirectory);
        bodyHandler.setDeleteUploadedFilesOnEnd(true);
        LOGGER.info("set uploads directory to " + uploadsDirectory);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start();

        // 如果本地未配置地址，则表示不必监听，只需要作为客户端使用即可
        if (endpointObject == null) {
            LOGGER.warn("rest listen address is not configured, will not start.");
            startFuture.complete();
            return;
        }

        Router mainRouter = Router.router(vertx);
        mainRouter.route().handler(bodyHandler);

        VertxRestServer vertxRestServer = new VertxRestServer(mainRouter);
        vertxRestServer.setTransport(CseContext.getInstance().getTransportManager().findTransport(Const.RESTFUL));

        HttpServer httpServer = createHttpServer();
        httpServer.requestHandler(mainRouter::accept);

        startListen(httpServer, startFuture);
    }

    private void startListen(HttpServer server, Future<Void> startFuture) {
        server.listen(endpointObject.getPort(), endpointObject.getHostOrIp(), ar -> {
            if (ar.succeeded()) {
                LOGGER.info("rest listen success. address={}:{}",
                        endpointObject.getHostOrIp(),
                        ar.result().actualPort());
                startFuture.complete();
                return;
            }

            String msg = String.format("rest listen failed, address=%s:%d",
                    endpointObject.getHostOrIp(),
                    endpointObject.getPort());
            LOGGER.error(msg, ar.cause());
            startFuture.fail(ar.cause());
        });
    }

    private HttpServer createHttpServer() {
        HttpServerOptions serverOptions = createDefaultHttpServerOptions();
        return vertx.createHttpServer(serverOptions);
    }

    private HttpServerOptions createDefaultHttpServerOptions() {
        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setAcceptBacklog(ACCEPT_BACKLOG);
        serverOptions.setSendBufferSize(SEND_BUFFER_SIZE);
        serverOptions.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
        serverOptions.setUsePooledBuffers(true);

        if (endpointObject.isSslEnabled()) {
            SSLOptionFactory factory =
                SSLOptionFactory.createSSLOptionFactory(SSL_KEY, null);
            SSLOption sslOption;
            if (factory == null) {
                sslOption = SSLOption.buildFromYaml(SSL_KEY);
            } else {
                sslOption = factory.createSSLOption();
            }
            SSLCustom sslCustom = SSLCustom.createSSLCustom(sslOption.getSslCustomClass());
            VertxTLSBuilder.buildNetServerOptions(sslOption, sslCustom, serverOptions);
        }

        return serverOptions;
    }
}
