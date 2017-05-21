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

package io.servicecomb.springboot.starter.discovery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huawei.paas.cse.loadbalance.CseServerList;
import com.huawei.paas.cse.loadbalance.filter.IsolationServerListFilter;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;

@Configuration
public class CseRibbonClientConfiguration {

    private String serviceId = "client";
    
    public CseRibbonClientConfiguration() {
    }

    public CseRibbonClientConfiguration(String serviceId) {
        this.serviceId = serviceId;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public ServerList<?> ribbonServerList(IClientConfig config) {
        
        CseServerList serverList = new CseServerList("springmvctest","springmvc","0.0.1","rest");
//        serverList.initWithNiwsConfig(config);
        return serverList;
    }
    
    @Bean
    public ServerListFilter<Server> ribbonServerListFilter() {
        return new IsolationServerListFilter();
    }
    
}
