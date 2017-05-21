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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.servicecomb.core.Response;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author
 * @version  [版本号, 2017年5月4日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TestConsumerResponseMapper {
    @Test
    public void test() {
        ConsumerResponseMapperFactory factory = new ConsumerResponseMapperFactory();

        ConsumerResponseMapper m1 = new ConsumerResponseCseSame();

        ConsumerResponseMapper m2 = new ConsumerResponseSame();
        factory.setMapperList(Arrays.asList(m1, m2));

        Assert.assertEquals(m1, factory.createResponseMapper(Response.class));
        Assert.assertEquals(m2, factory.createResponseMapper(String.class));

        Response r = Response.ok(1);
        Response r1 = (Response) m1.mapResponse(r);
        Assert.assertEquals(r, r1);

        Object result = m2.mapResponse(r);
        Assert.assertEquals(1, (int) result);
    }
}
