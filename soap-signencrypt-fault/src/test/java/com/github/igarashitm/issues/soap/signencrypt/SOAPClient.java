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
package com.github.igarashitm.issues.soap.signencrypt;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.igarashitm.issues.soap.signencrypt.ws.FaultServiceService;

public final class SOAPClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOAPClient.class);
    private static final String CXF_CLIENT_CONFIG = "CxfClientConfig.xml";
    
    public static void main(String... args) throws Exception {
        SpringBusFactory bf = new SpringBusFactory();
        Bus bus = bf.createBus(SOAPClient.class.getClassLoader().getResource(CXF_CLIENT_CONFIG).toURI().toString());
        BusFactory.setDefaultBus(bus);

        FaultServiceService ws = new FaultServiceService();
        com.github.igarashitm.issues.soap.signencrypt.ws.FaultService port = ws.getFaultServicePort();
        port.invoke("foobar");
    }

}
