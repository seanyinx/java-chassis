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

package com.huawei.paas.cse.qps;

import io.servicecomb.core.AsyncResponse;
import io.servicecomb.core.Const;
import io.servicecomb.core.Invocation;
import io.servicecomb.core.exception.CommonExceptionData;
import io.servicecomb.core.exception.InvocationException;
import io.servicecomb.core.handler.impl.AbstractHandler;

public class ProviderQpsFlowControlHandler extends AbstractHandler {
    private ProviderQpsControllerManager qpsControllerMgr = new ProviderQpsControllerManager();

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Invocation invocation, AsyncResponse asyncResp) throws Exception {
        if (!Config.INSTANCE.isProviderEnabled()) {
            invocation.next(asyncResp);
            return;
        }

        String microServiceName = (String) invocation.getContext(Const.SRC_MICROSERVICE);
        if (microServiceName != null && !microServiceName.isEmpty()) {
            QpsController qpsController = qpsControllerMgr.getOrCreate(microServiceName);
            if (qpsController.isLimitNewRequest()) {
                // 429
                CommonExceptionData errorData = new CommonExceptionData("rejected by qps flowcontrol");
                asyncResp.producerFail(
                        new InvocationException(QpsConst.TOO_MANY_REQUESTS_STATUS, errorData));
                return;
            }
        }

        invocation.next(asyncResp);
    }
}
