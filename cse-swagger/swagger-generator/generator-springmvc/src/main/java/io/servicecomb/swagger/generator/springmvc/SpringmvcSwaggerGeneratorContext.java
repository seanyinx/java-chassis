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

package io.servicecomb.swagger.generator.springmvc;

import io.servicecomb.swagger.generator.core.OperationGenerator;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.PathVariableAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.response.ResponseEntityProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.servicecomb.swagger.generator.core.DefaultSwaggerGeneratorContext;
import io.servicecomb.swagger.generator.core.utils.ClassUtils;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.CookieValueAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.RequestAttributeAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.RequestBodyAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.RequestHeaderAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.RequestMappingClassAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.RequestMappingMethodAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.annotation.RequestParamAnnotationProcessor;
import io.servicecomb.swagger.generator.springmvc.processor.parameter.SpringmvcDefaultParameterProcessor;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author
 * @version  [版本号, 2017年3月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SpringmvcSwaggerGeneratorContext extends DefaultSwaggerGeneratorContext {
    private static final int ORDER = 1000;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return ORDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canProcess(Class<?> cls) {
        return ClassUtils.hasAnnotation(cls, RequestMapping.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initClassAnnotationMgr() {
        super.initClassAnnotationMgr();

        classAnnotationMgr.register(RequestMapping.class, new RequestMappingClassAnnotationProcessor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initMethodAnnotationMgr() {
        super.initMethodAnnotationMgr();

        methodAnnotationMgr.register(RequestMapping.class, new RequestMappingMethodAnnotationProcessor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initParameterAnnotationMgr() {
        super.initParameterAnnotationMgr();

        parameterAnnotationMgr.register(CookieValue.class, new CookieValueAnnotationProcessor());
        parameterAnnotationMgr.register(PathVariable.class, new PathVariableAnnotationProcessor());
        parameterAnnotationMgr.register(RequestBody.class, new RequestBodyAnnotationProcessor());
        parameterAnnotationMgr.register(RequestHeader.class, new RequestHeaderAnnotationProcessor());
        parameterAnnotationMgr.register(RequestParam.class, new RequestParamAnnotationProcessor());
        parameterAnnotationMgr.register(RequestAttribute.class, new RequestAttributeAnnotationProcessor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initParameterTypeProcessorMgr() {
        super.initParameterTypeProcessorMgr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDefaultParameterProcessor() {
        defaultParameterProcessor = new SpringmvcDefaultParameterProcessor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initResponseTypeProcessorMgr() {
        super.initResponseTypeProcessorMgr();

        responseTypeProcessorMgr.register(ResponseEntity.class, new ResponseEntityProcessor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void correctPath(OperationGenerator operationGenerator) {
        String path = operationGenerator.getPath();
        if (StringUtils.isEmpty(path)) {
            path = "/";
        }
        operationGenerator.setPath(path);
    }
}
