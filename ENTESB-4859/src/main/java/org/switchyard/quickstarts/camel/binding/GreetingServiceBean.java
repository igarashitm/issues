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
package org.switchyard.quickstarts.camel.binding;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Locale;

import javax.inject.Inject;
import javax.xml.soap.SOAPFault;

import org.apache.cxf.interceptor.Fault;
import org.jboss.logging.Logger;
import org.switchyard.Exchange;
import org.switchyard.component.bean.BeanComponentException;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(GreetingService.class)
public class GreetingServiceBean implements org.switchyard.quickstarts.camel.binding.GreetingService {
    
    private static final Logger logger = Logger.getLogger(GreetingServiceBean.class);
    
    @Inject @Reference
    private GreetingReference _reference;
    
    @Override
    public void greet(String name) {
        logger.info(String.format("Received a request from '%s'", name));
        try {
            _reference.greet(SOAP_PAYLOAD);
        } catch (UndeclaredThrowableException t) {
            if (t.getCause() != null && t.getCause() instanceof BeanComponentException) {
                Exchange exchange = ((BeanComponentException) t.getCause()).getFaultExchange();
                SOAPFault fault = exchange.getMessage().getContent(SOAPFault.class);
                try {
                    String reason = fault.getFaultReasonText(Locale.getDefault());
                    if (reason.contains("Whoa!")) {
                        logger.info(String.format("Expected fault was received, '%s'", reason));
                        return;
                    } else {
                        throw t;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            throw t;
        }
    }
    
    private static final String SOAP_PAYLOAD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">"
            + "<soap:Header/>"
            + "<soap:Body>"
            + "<orders:submitOrder xmlns:orders=\"urn:switchyard-quickstart:bean-service:1.0\">"
            + "<order>"
            + "<orderId>PO-19838-XYZ</orderId>"
            + "<itemId>BUTTER</itemId>"
            + "<quantity>200</quantity>"
            + "</order>"
            + "</orders:submitOrder>"
            + "</soap:Body>"
            + "</soap:Envelope>";
}
