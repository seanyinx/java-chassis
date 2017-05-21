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

package io.servicecomb.swagger.generator.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huawei.paas.foundation.common.utils.SPIServiceUtils;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author
 * @version  [版本号, 2017年3月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
public class CompositeSwaggerGeneratorContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeSwaggerGeneratorContext.class);

    private List<SwaggerGeneratorContext> contextList;

    /**
     * <构造函数> [参数说明]
     */
    public CompositeSwaggerGeneratorContext() {
        contextList = SPIServiceUtils.getAllService(SwaggerGeneratorContext.class);

        contextList.sort((context1, context2) -> {
            return context2.getOrder() - context1.getOrder();
        });

        for (SwaggerGeneratorContext context : contextList) {
            LOGGER.info("Found swagger generator context: {}", context.getClass().getName());
        }
    }

    /**
     * 获取contextList的值
     * @return 返回 contextList
     */
    public List<SwaggerGeneratorContext> getContextList() {
        return contextList;
    }

    public SwaggerGeneratorContext selectContext(Class<?> cls) {
        for (SwaggerGeneratorContext context : contextList) {
            if (context.canProcess(cls)) {
                return context;
            }
        }

        throw new Error("impossible, must be bug.");
    }
}
