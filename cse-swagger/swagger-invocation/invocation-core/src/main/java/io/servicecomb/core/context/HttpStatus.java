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
package io.servicecomb.core.context;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author
 * @version  [版本号, 2017年4月19日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HttpStatus implements StatusType {
    public static boolean isSuccess(int code) {
        return Status.Family.SUCCESSFUL.equals(Status.Family.familyOf(code));
    }

    public static boolean isSuccess(StatusType status) {
        return Status.Family.SUCCESSFUL.equals(status.getFamily());
    }

    private final int statusCode;

    private final String reason;

    public HttpStatus(final int statusCode, final String reasonPhrase) {
        this.statusCode = statusCode;
        this.reason = reasonPhrase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Family getFamily() {
        return Family.familyOf(statusCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReasonPhrase() {
        return reason;
    }
}
