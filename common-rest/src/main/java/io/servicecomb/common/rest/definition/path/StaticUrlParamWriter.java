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

package io.servicecomb.common.rest.definition.path;

/**
 * 处理静态path
 * @author   
 * @version  [版本号, 2017年1月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class StaticUrlParamWriter implements UrlParamWriter {

    private String staticPath;

    public StaticUrlParamWriter(String staticPath) {
        this.staticPath = staticPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(StringBuilder builder, Object[] args) {
        builder.append(staticPath);
    }
}
