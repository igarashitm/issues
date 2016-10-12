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
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public final class WorkServiceMainSCA {

    private static final Logger LOGGER = Logger.getLogger(WorkServiceMainSCA.class);
    private static final String URL = "https://localhost:8443/switchyard-remote";
    private static final QName SERVICE = new QName(
            "urn:switchyard-quickstart-demo:policy-security-wss-username:0.1.0",
            "WorkService");
    private static final String WS_SECURITY_HEADER = 
              "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n"
            + "  <wsse:UsernameToken>\n"
            + "    <wsse:Username>kermit</wsse:Username>\n"
            + "    <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">the-frog-1</wsse:Password>\n"
            + "  </wsse:UsernameToken>\n"
            + "</wsse:Security>";
    private static final String REQUEST_BODY =
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
            + "  xmlns:policy-security-wss-username=\"urn:switchyard-quickstart-demo:policy-security-wss-username:0.1.0\">"
            + "<soap:Body>"
            + "    <policy-security-wss-username:doWork>"
            + "        <work>"
            + "            <command>WORK_CMD</command>"
            + "        </work>"
            + "    </policy-security-wss-username:doWork>"
            + "</soap:Body>"
            + "</soap:Envelope>";
    private static final QName WS_SECURITY_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");

    public static void main(String... args) throws Exception {
        WorkServiceMainSCA myself = new WorkServiceMainSCA();
        myself.invoke(HttpInvoker.WS_SECURITY, true);
        myself.invoke(WS_SECURITY_QNAME, true);
        myself.invoke(WS_SECURITY_QNAME.toString(), true);
        myself.invoke(WS_SECURITY_QNAME, false);
    }

    public void invoke(Object seckey, boolean useHeader) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document wsseDoc = docBuilder.parse(new InputSource(new StringReader(WS_SECURITY_HEADER)));
        Document bodyDoc = docBuilder.parse(new InputSource(new StringReader(REQUEST_BODY.replaceAll("WORK_CMD", "CMD-" + System.currentTimeMillis()))));
        RemoteMessage request = new RemoteMessage()
                                        .setService(SERVICE)
                                        .setOperation("doWork")
                                        .setContent(bodyDoc.getDocumentElement());
        LOGGER.info("Invoking remote SCA service with '"+ seckey + "' as a WS-Security property key, carrying as a " + (useHeader ? "HTTP header" : "Context Property"));
        HttpInvoker invoker = new HttpInvoker(URL);
        if (useHeader) {
            invoker.setProperty(seckey, wsseDoc.getDocumentElement());
        } else {
            request.getContext().setProperty(seckey.toString(), WS_SECURITY_HEADER);
        }
        RemoteMessage reply = invoker.invoke(request);
        if (reply.isFault()) {
            if (reply.getContent() instanceof Throwable) {
                LOGGER.error(null, Throwable.class.cast(reply.getContent()));
            } else {
                LOGGER.error(reply.getContent());
            }
        } else {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter output = new StringWriter();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource((Node)reply.getContent()), new StreamResult(output));
            LOGGER.info(output.toString());
        }
        
    }
}
