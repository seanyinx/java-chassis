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

package io.servicecomb.foundation.common;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author   
 * @version  [版本号, 2016年11月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommonThread extends Thread {
    protected volatile boolean shutdown;

    /**
     * <构造函数> [参数说明]
     */
    public CommonThread() {
        super();
    }

    /**
     * <构造函数>
     * @param name name
     * @param stackSize stackSize
     */
    public CommonThread(String name, long stackSize) {
        super(null, null, name, stackSize);
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public boolean isRunning() {
        return !shutdown;
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     */
    public void shutdown() {
        shutdown = true;
        interrupt();
    }
}
