/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.windwant.dubbo;

import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.core.exception.TransactionException;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Please add the follow VM arguments:
 * <pre>
 *     -Djava.net.preferIPv4Stack=true
 * </pre>
 */
public class DubboTXBusiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboTXBusiService.class);

    @GlobalTransactional(name = "dubbo-demo-tx")
    public void calGrade() {
        LOGGER.info("calGrade begin ... xid: " + RootContext.getXID());
        System.out.println("down score result: " + DubboTXSvr.dubboTXServicex.downScore(1, 1, 10));
        System.out.println("modify level result: " + DubboTXSvr.dubboTXServicey.modifyLevel(1));
        throw new RuntimeException("xxx");
    }

    public static void main(String[] args) throws TransactionException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] {"dubbo-business.xml"});
        final DubboTXBusiService business = (DubboTXBusiService)context.getBean("dubbotx");
        DubboTXSvr.main(null);
        business.calGrade();
    }
}
