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

package io.servicecomb.swagger.invocation.arguments.producer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.servicecomb.swagger.invocation.arguments.ArgumentsMapperConfig;
import io.servicecomb.swagger.invocation.arguments.ArgumentsMapperFactory;
import io.servicecomb.swagger.invocation.arguments.ContextArgumentMapperFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.servicecomb.swagger.invocation.arguments.ArgumentMapper;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author   
 * @version  [版本号, 2017年4月5日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
public class ProducerArgumentsMapperFactory extends ArgumentsMapperFactory {
    @Inject
    @Qualifier("producer")
    public void setFactoryList(List<ContextArgumentMapperFactory> factoryList) {
        createFactoryMap(factoryList);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T> T createArgumentsMapper(ArgumentsMapperConfig config) {
        return (T) new ProducerArgumentsMapper(config.getArgumentMapperList(), config.getProviderParameters().size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArgumentMapper createArgumentSame(int swaggerIdx, int producerIdx) {
        return new ProducerArgumentSame(swaggerIdx, producerIdx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArgumentMapper createBodyFieldArgMapper(ArgumentsMapperConfig config, int swaggerIdx,
            Map<Integer, Field> fieldMap) {
        return new SwaggerArgumentToProducerBodyField(swaggerIdx, fieldMap);
    }
}
