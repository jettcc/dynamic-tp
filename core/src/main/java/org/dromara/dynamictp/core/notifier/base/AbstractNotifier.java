/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.dynamictp.core.notifier.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dynamictp.common.entity.NotifyItem;
import org.dromara.dynamictp.common.entity.NotifyPlatform;
import org.dromara.dynamictp.core.notifier.context.BaseNotifyCtx;
import org.dromara.dynamictp.core.notifier.context.DtpNotifyCtxHolder;

import java.util.Optional;

/**
 * AbstractNotifier related
 *
 * @author kyao
 * @since 1.1.3
 */
@Slf4j
public abstract class AbstractNotifier implements Notifier {

    @Override
    public final void send(NotifyPlatform platform, String content) {
        try {
            send0(platform, content);
        } catch (Exception e) {
            log.error("DynamicTp notify, {} send failed.", platform(), e);
        }
    }

    /**
     * Send message.
     *
     * @param platform platform
     * @param content content
     */
    protected abstract void send0(NotifyPlatform platform, String content);

    /**
     * Get the notify receivers
     * @param platform platform
     * @return Receivers
     */
    protected String[] getReceivers(NotifyPlatform platform) {
        BaseNotifyCtx context = DtpNotifyCtxHolder.get();
        String receivers = Optional.ofNullable(context)
                .map(BaseNotifyCtx::getNotifyItem)
                .map(NotifyItem::getReceivers)
                .orElse(null);
        receivers = StringUtils.isBlank(receivers) ? platform.getReceivers() : receivers;
        return receivers.split(",");
    }

}
