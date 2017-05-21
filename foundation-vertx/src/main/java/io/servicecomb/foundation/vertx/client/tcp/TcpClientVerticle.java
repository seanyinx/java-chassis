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

package io.servicecomb.foundation.vertx.client.tcp;

import io.servicecomb.foundation.vertx.client.AbstractClientVerticle;

import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.impl.NetClientImpl;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author   
 * @version  [版本号, 2016年12月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TcpClientVerticle extends AbstractClientVerticle<TcpClientPool> {
    private TcpClientConfig clientConfig;

    // 每线程一个实例即可
    private NetClient netClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws Exception {
        super.start();
        clientConfig = (TcpClientConfig) config().getValue(CLIENT_OPTIONS);

        // vertx.createNetClient()创建出来的netClient不支持跨线程调用
        netClient = new NetClientImpl((VertxInternal) vertx, clientConfig, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TcpClientPool createClientPool() {
        return new TcpClientPool(clientConfig, context, netClient);
    }
}
