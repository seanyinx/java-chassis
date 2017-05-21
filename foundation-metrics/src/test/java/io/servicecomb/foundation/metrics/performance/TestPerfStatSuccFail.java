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

package io.servicecomb.foundation.metrics.performance;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author  
 * @since Mar 14, 2017
 * @see 
 */
public class TestPerfStatSuccFail {

    PerfStatSuccFail oPerfStatSuccFail = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        oPerfStatSuccFail = new PerfStatSuccFail("testSuccFail");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        oPerfStatSuccFail = null;
    }

    /**
     * Test add
     */
    @Test
    public void testAdd() {
        oPerfStatSuccFail.add(true, new PerfStatContext());
        Assert.assertEquals(2, oPerfStatSuccFail.getPerfStatDataList().size());

        //Test PerfStatSuccFail.add(boolean, int, long)
        oPerfStatSuccFail.add(false, 10, 100);;
        Assert.assertEquals(2, oPerfStatSuccFail.getPerfStatDataList().size());
    }

}
