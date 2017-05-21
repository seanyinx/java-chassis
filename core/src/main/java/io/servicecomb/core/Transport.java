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

package io.servicecomb.core;

// TODO:感觉要拆成显式的client、server才好些
public interface Transport {
    String getName();

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @throws Exception
     */
    boolean init() throws Exception;

    /**
     * endpoint的格式为 URI，比如rest://192.168.1.1:8080
     * @param endpoint endpoint URI representation
     * @return tranport specific address.
     */
    Object parseAddress(String endpoint);

    /**
     * 本tranport的监听地址
     * @return
     * @throws Exception
     */
    Endpoint getEndpoint() throws Exception;

    
    /**
     * 用于上报到服务中心，要求是其他节点可访问的地址
     * @return
     * @throws Exception
     */
    Endpoint getPublishEndpoint() throws Exception;
    
    /**
    * <一句话功能简述>
    * <功能详细描述>
    * @param invocation
    * @param asyncResp
    * @throws Exception
    */
    void send(Invocation invocation, AsyncResponse asyncResp) throws Exception;
}
