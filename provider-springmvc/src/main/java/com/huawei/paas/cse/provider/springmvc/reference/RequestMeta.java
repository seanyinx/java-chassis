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

package com.huawei.paas.cse.provider.springmvc.reference;

import java.util.Map;

import io.servicecomb.common.rest.definition.RestOperationMeta;
import com.huawei.paas.cse.core.definition.OperationMeta;
import com.huawei.paas.cse.core.provider.consumer.ReferenceConfig;

/**
 * 封装每一次调用的元数据
 * @author   
 * @version  [版本号, 2017年1月6日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RequestMeta {
    private ReferenceConfig referenceConfig;

    private OperationMeta operationMeta;

    private RestOperationMeta swaggerRestOperation;

    private Map<String, String> pathParams;

    /**
     * <构造函数>
     * @param referenceConfig
     * @param swaggerRestOperation
     * @param pathParams [参数说明]
     */
    public RequestMeta(ReferenceConfig referenceConfig, RestOperationMeta swaggerRestOperation,
            Map<String, String> pathParams) {
        this.referenceConfig = referenceConfig;
        this.operationMeta = swaggerRestOperation.getOperationMeta();
        this.swaggerRestOperation = swaggerRestOperation;
        this.pathParams = pathParams;
    }

    /**
     * 获取referenceConfig的值
     * @return 返回 referenceConfig
     */
    public ReferenceConfig getReferenceConfig() {
        return referenceConfig;
    }

    /**
     * 获取pathParams的值
     * @return 返回 pathParams
     */
    public Map<String, String> getPathParams() {
        return pathParams;
    }

    /**
     * 获取swaggerRestOperation的值
     * @return 返回 swaggerRestOperation
     */
    public RestOperationMeta getSwaggerRestOperation() {
        return swaggerRestOperation;
    }

    /**
     * 获取operationMeta的值
     * @return 返回 operationMeta
     */
    public OperationMeta getOperationMeta() {
        return operationMeta;
    }

    public String getOperationQualifiedName() {
        return operationMeta.getSchemaQualifiedName();
    }
}
