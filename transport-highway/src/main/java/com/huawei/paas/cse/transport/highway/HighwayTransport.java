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

import java.util.Collections;

import org.springframework.stereotype.Component;

import io.servicecomb.core.AsyncResponse;
import io.servicecomb.core.Invocation;
import io.servicecomb.core.transport.AbstractTransport;
import com.huawei.paas.foundation.common.net.URIEndpointObject;
import com.huawei.paas.foundation.vertx.SimpleJsonObject;
import com.huawei.paas.foundation.vertx.VertxUtils;
import com.huawei.paas.foundation.vertx.tcp.TcpConst;

import io.vertx.core.DeploymentOptions;

@Component
public class HighwayTransport extends AbstractTransport {
    public static final String NAME = "highway";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }

    public boolean init() throws Exception {
        HighwayCodec.setHighwayTransport(this);

        DeploymentOptions deployOptions = new DeploymentOptions().setInstances(HighwayConfig.getServerThreadCount());
        setListenAddressWithoutSchema(HighwayConfig.getAddress(), Collections.singletonMap(TcpConst.LOGIN, "true"));
        SimpleJsonObject json = new SimpleJsonObject();
        json.put(ENDPOINT_KEY, getEndpoint());
        deployOptions.setConfig(json);
        return VertxUtils.blockDeploy(transportVertx, HighwayServerVerticle.class, deployOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Invocation invocation, AsyncResponse asyncResp) throws Exception {
        URIEndpointObject endpoint = (URIEndpointObject) invocation.getEndpoint().getAddress();
        HighwayClient client =
            HighwayClientManager.INSTANCE.getHighwayClient(endpoint.isSslEnabled());
        client.send(invocation, asyncResp);
    }
}
