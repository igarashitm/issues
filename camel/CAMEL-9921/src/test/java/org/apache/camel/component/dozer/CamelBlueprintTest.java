/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.dozer;

import java.util.Dictionary;
import java.util.Map;

import org.apache.camel.component.dozer.example.abc.ABCOrder;
import org.apache.camel.component.dozer.example.abc.ABCOrder.Header;
import org.apache.camel.component.dozer.example.xyz.XYZOrder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultComponentResolver;
import org.apache.camel.spi.ComponentResolver;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class CamelBlueprintTest extends CamelBlueprintTestSupport {
    
    private static final Logger logger = Logger.getLogger(CamelBlueprintTest.class);
    
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camel-context.xml";
    }
    
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        ComponentResolver testResolver = new DefaultComponentResolver();

        services.put(ComponentResolver.class.getName(), asService(testResolver, "component", "dozer"));
    }
    
    @Test
    public void testLiteralMapping() throws Exception {
        ABCOrder abcOrder = new ABCOrder();
        abcOrder.setHeader(new Header());
        abcOrder.getHeader().setStatus("GOLD");
        Object answer = template.requestBody("direct:start", abcOrder);
        // check results
        Assert.assertEquals(XYZOrder.class.getName(), answer.getClass().getName());
        XYZOrder xyz = (XYZOrder)answer;
        logger.info(String.format("Received XYZOrder:[priority=%s, custID=%s, orderID=%s]", xyz.getPriority(), xyz.getCustId(), xyz.getOrderId()));
        Assert.assertEquals(xyz.getPriority(), "GOLD");
        Assert.assertEquals("ACME-SALES", xyz.getCustId());
        Assert.assertEquals("W123-EG", xyz.getOrderId());
    }
}
