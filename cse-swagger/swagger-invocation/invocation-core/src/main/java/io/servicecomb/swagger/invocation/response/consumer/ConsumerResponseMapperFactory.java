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
package io.servicecomb.swagger.invocation.response.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author
 * @version  [版本号, 2017年4月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
public class ConsumerResponseMapperFactory {
    private ConsumerResponseMapper defaultMapper;

    private Map<Class<?>, ConsumerResponseMapper> mappers = new HashMap<>();

    @Inject
    public void setMapperList(List<ConsumerResponseMapper> mapperList) {
        for (ConsumerResponseMapper mapper : mapperList) {
            if (mapper.getResponseClass() == null) {
                defaultMapper = mapper;
                continue;
            }

            mappers.put(mapper.getResponseClass(), mapper);
        }
    }

    public ConsumerResponseMapper createResponseMapper(Class<?> cls) {
        ConsumerResponseMapper mapper = mappers.get(cls);
        if (mapper == null) {
            return defaultMapper;
        }

        return mapper;
    }
}
