/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.quickstarts.camel.service;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.milyn.payload.JavaResult;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
    config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
    mixins = CDIMixIn.class)
public class CamelServiceTest {

    private static final String TEST_MESSAGE = "\n"
            + "<message xmlns=\"urn:switchyard-quickstart:camel-service:0.1.0\">\n"
            + "  \t<subject>Test Subject</subject>\n"
            + "  \t<body>Test Body</body>\n"
            + "</message>\n";

    @ServiceOperation("CamelService.acceptMessage")
    private Invoker acceptMessage;

    @Test
    public void testCamelRoute() {
        org.switchyard.Message out = acceptMessage.sendInOut(TEST_MESSAGE);
        Object body = out.getContent();
        Assert.assertTrue("body is " + body.getClass(), body instanceof JavaResult.ResultMap);
        Assert.assertEquals(new Message().setSubject("Test Subject").setBody("Test Body").toString(),
                JavaResult.ResultMap.class.cast(body).get("message").toString());
    }
}
