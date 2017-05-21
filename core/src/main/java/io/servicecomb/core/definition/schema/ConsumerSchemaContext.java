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

package io.servicecomb.core.definition.schema;

import java.util.Map;

import io.servicecomb.core.provider.consumer.ConsumerOperationMeta;
import io.servicecomb.serviceregistry.api.registry.Microservice;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author   
 * @version  [版本号, 2017年4月7日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ConsumerSchemaContext extends SchemaContext {
    protected Microservice microservice;

    protected Map<String, ConsumerOperationMeta> consumerOperationMap;

    public Microservice getMicroservice() {
        return microservice;
    }

    public void setMicroservice(Microservice microservice) {
        this.microservice = microservice;
    }

    public Map<String, ConsumerOperationMeta> getConsumerOperationMap() {
        return consumerOperationMap;
    }

    public void setConsumerOperationMap(Map<String, ConsumerOperationMeta> consumerOperationMap) {
        this.consumerOperationMap = consumerOperationMap;
    }
}
