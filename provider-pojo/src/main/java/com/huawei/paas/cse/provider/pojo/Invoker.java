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

package com.huawei.paas.cse.provider.pojo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import io.servicecomb.core.Invocation;
import io.servicecomb.core.Response;
import io.servicecomb.core.definition.SchemaMeta;
import io.servicecomb.core.exception.ExceptionFactory;
import io.servicecomb.core.invocation.InvocationFactory;
import io.servicecomb.core.provider.consumer.ConsumerOperationMeta;
import io.servicecomb.core.provider.consumer.InvokerUtils;
import io.servicecomb.core.provider.consumer.ReferenceConfig;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author   
 * @version  [版本号, 2016年11月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Invoker implements InvocationHandler {
    private SchemaMeta schemaMeta;

    private ReferenceConfig config;

    private Map<String, ConsumerOperationMeta> consumerOperationMap;

    public void init(ReferenceConfig config, SchemaMeta schemaMeta,
            Map<String, ConsumerOperationMeta> consumerOperationMap) {
        this.config = config;
        this.schemaMeta = schemaMeta;
        this.consumerOperationMap = consumerOperationMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation =
            InvocationFactory.forConsumer(config, schemaMeta, method.getName(), null);

        ConsumerOperationMeta consumerOperation = consumerOperationMap.get(method.getName());
        consumerOperation.getArgsMapper().toInvocation(args, invocation);

        Response response = InvokerUtils.innerSyncInvoke(invocation);
        if (response.isSuccessed()) {
            return consumerOperation.getResponseMapper().mapResponse(response);
        }

        throw ExceptionFactory.convertConsumerException((Throwable) response.getResult());
        //        return InvokerUtils.invoke(invocation);
    }
}
