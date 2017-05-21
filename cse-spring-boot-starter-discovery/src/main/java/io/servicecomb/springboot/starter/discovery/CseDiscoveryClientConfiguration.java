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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 */
@Configuration
@EnableConfigurationProperties
public class CseDiscoveryClientConfiguration {

	@Bean
	public CseDiscoveryProperties cseDiscoveryProperties() {
		return new CseDiscoveryProperties();
	}

   @Bean
    public ServiceRouteMapper cseServiceRouteMapper() {
        return new CseServiceRouteMapper();
    }
   
	@Bean
	@ConditionalOnMissingBean
	public CseDiscoveryClient cseDiscoveryClient() {
		CseDiscoveryClient discoveryClient = new CseDiscoveryClient();
		return discoveryClient;
	}

}
