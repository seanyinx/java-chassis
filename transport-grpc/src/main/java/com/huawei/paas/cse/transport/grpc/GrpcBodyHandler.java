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

package com.huawei.paas.cse.transport.grpc;

import io.servicecomb.foundation.vertx.SimpleBodyHandler;

/**
 * Grpc通道body只支持自定义的application/grpc格式数据
 * @author   
 * @version  [版本号, 2017年3月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GrpcBodyHandler extends SimpleBodyHandler {
    private static final String GRPC_TYPE = "application/grpc";

    @Override
    protected boolean contentTypeSupported(String contentType) {
        return contentType == null || contentType.equals(GRPC_TYPE);
    }

}
