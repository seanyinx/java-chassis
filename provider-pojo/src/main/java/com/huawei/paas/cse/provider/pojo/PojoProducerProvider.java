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

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import io.servicecomb.core.definition.schema.ProducerSchemaFactory;
import io.servicecomb.core.provider.producer.AbstractProducerProvider;
import com.huawei.paas.cse.provider.pojo.instance.PojoInstanceFactory;
import com.huawei.paas.cse.provider.pojo.instance.SpringInstanceFactory;
import com.huawei.paas.cse.provider.pojo.schema.PojoProducerMeta;
import com.huawei.paas.cse.provider.pojo.schema.PojoProducers;
import com.huawei.paas.cse.serviceregistry.RegistryUtils;
import io.servicecomb.foundation.common.RegisterManager;
import io.servicecomb.foundation.common.utils.BeanUtils;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author   
 * @version  [版本号, 2016年11月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
public class PojoProducerProvider extends AbstractProducerProvider {
    private RegisterManager<String, InstanceFactory> instanceFactoryMgr =
        new RegisterManager<>("pojo instance factory manager");

    @Inject
    private ProducerSchemaFactory producerSchemaFactory;

    @Inject
    private PojoProducers pojoProducers;

    public void regsiterInstanceFactory(InstanceFactory instanceFactory) {
        instanceFactoryMgr.register(instanceFactory.getImplName(), instanceFactory);
    }

    public PojoProducerProvider() {
        regsiterInstanceFactory(new PojoInstanceFactory());
        regsiterInstanceFactory(new SpringInstanceFactory());
    }

    /**
     * {@inheritDoc}
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        for (PojoProducerMeta pojoProducerMeta : pojoProducers.getProcucers()) {
            initPojoProducerMeta(pojoProducerMeta);

            producerSchemaFactory.getOrCreateProducerSchema(
                    RegistryUtils.getMicroservice().getServiceName(),
                    pojoProducerMeta.getSchemaId(),
                    pojoProducerMeta.getInstanceClass(),
                    pojoProducerMeta.getInstance());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return PojoConst.POJO;
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     */
    private void initPojoProducerMeta(PojoProducerMeta pojoProducerMeta) {
        if (pojoProducerMeta.getInstance() != null) {
            return;
        }

        String[] nameAndValue = parseImplementation(pojoProducerMeta.getImplementation());

        InstanceFactory factory = instanceFactoryMgr.ensureFindValue(nameAndValue[0]);
        Object instance = factory.create(nameAndValue[1]);
        Class<?> instanceClass = BeanUtils.getImplClassFromBean(instance);

        pojoProducerMeta.setInstance(instance);
        pojoProducerMeta.setInstanceClass(instanceClass);
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @return
     */
    private String[] parseImplementation(String implementation) {
        String implName = PojoConst.POJO;
        String implValue = implementation;
        int idx = implementation.indexOf(':');
        if (idx != -1) {
            implName = implementation.substring(0, idx);
            implValue = implementation.substring(idx + 1);
        }

        return new String[] {implName, implValue};
    }
}
