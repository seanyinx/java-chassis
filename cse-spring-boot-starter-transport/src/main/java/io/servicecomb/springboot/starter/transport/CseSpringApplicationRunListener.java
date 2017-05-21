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
package io.servicecomb.springboot.starter.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import io.servicecomb.foundation.common.utils.BeanUtils;
import io.servicecomb.foundation.common.utils.Log4jUtils;

/**
 * Cse bean initialization class.
 *
 * @author
 *
 * @version
 */
public class CseSpringApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CseSpringApplicationRunListener.class);

    public CseSpringApplicationRunListener(SpringApplication app, String[] args) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void started() {
        try {
            Log4jUtils.init();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        // ������spring cloud context�ĳ����л�ʵ�������springApplication����Ҫ���һ��
        if (context.getParent() != null) {
            return;
        }

        LOGGER.info("Initializing the CSE...");

        try {
            XmlBeanDefinitionReader beanDefinitionReader =
                new XmlBeanDefinitionReader((BeanDefinitionRegistry) context.getBeanFactory());
            beanDefinitionReader.loadBeanDefinitions(BeanUtils.DEFAULT_BEAN_RESOURCE);
        } catch (Throwable e) {
            LOGGER.error("Error to initialize CSE...", e);

            throw new Error(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }

}
