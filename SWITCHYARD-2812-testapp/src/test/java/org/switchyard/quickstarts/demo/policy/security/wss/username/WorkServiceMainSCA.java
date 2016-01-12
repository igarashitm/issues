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
package org.switchyard.quickstarts.demo.policy.security.wss.username;

import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public final class WorkServiceMainSCA {

    private static final Logger LOGGER = Logger.getLogger(WorkServiceMainSCA.class);
    private static final String URL = "https://localhost:8443/switchyard-remote";
    private static final QName SERVICE = new QName(
            "urn:switchyard-quickstart-demo:policy-security-wss-username:0.1.0",
            "WorkServiceSCA");
    private static final String WS_SECURITY_HEADER = 
              "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n"
            + "  <wsse:UsernameToken>\n"
            + "    <wsse:Username>kermit</wsse:Username>\n"
            + "    <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">the-frog-1</wsse:Password>\n"
            + "  </wsse:UsernameToken>\n"
            + "</wsse:Security>";
    private static final QName WS_SECURITY_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");

    public static void main(String... args) throws Exception {
        WorkServiceMainSCA myself = new WorkServiceMainSCA();
        myself.invoke(HttpInvoker.WS_SECURITY);
        myself.invoke(WS_SECURITY_QNAME);
        myself.invoke(WS_SECURITY_QNAME.toString());
    }

    public void invoke(Object seckey) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(WS_SECURITY_HEADER)));
        HttpInvoker invoker = new HttpInvoker(URL);
        invoker.setProperty(seckey, doc.getDocumentElement());
        Work work = new Work();
        work.setCommand("WORK_CMD");
        RemoteMessage request = new RemoteMessage()
                                        .setService(SERVICE)
                                        .setOperation("doWork")
                                        .setContent(work);
        LOGGER.info("Invoking remote SCA service with '"+ seckey + "' as a WS-Security property key...");
        RemoteMessage reply = invoker.invoke(request);
        if (reply.isFault()) {
            if (reply.getContent() instanceof Throwable) {
                LOGGER.error(null, Throwable.class.cast(reply.getContent()));
            } else {
                LOGGER.error(reply.getContent());
            }
        } else {
            LOGGER.info(reply.getContent());
        }
        
    }
}
