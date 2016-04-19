/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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
package org.switchyard.quickstarts.camel.cxf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.endpoint.Client;
import org.jboss.logging.Logger;

@Named("requestContextProcessor")
public class RequestContextProcessor implements Processor {

    private final Logger logger = Logger.getLogger(RequestContextProcessor.class);
    
    /**
     * Creates new processor.
     */
    public RequestContextProcessor() {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info(">>>>> Setting RequestContext properties as a Camel exchange property to be propagated");
        // In this way camel-cxf producer would pick up this Map and set into RequestContext properties on
        // an actual invocation.
        Map<String,Object> requestContext = new HashMap<String,Object>();
        requestContext.put("mtom-enabled", true);
        requestContext.put("security.callback-handler", new CallbackHandler() {
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                throw new UnsupportedCallbackException(null, "Succeeded to registering custom security CallbackHandler");
            }
        });
        requestContext.put("security.signature.properties", "not-existing-keystore.properties");
        exchange.setProperty(Client.REQUEST_CONTEXT, requestContext);
    }

}
