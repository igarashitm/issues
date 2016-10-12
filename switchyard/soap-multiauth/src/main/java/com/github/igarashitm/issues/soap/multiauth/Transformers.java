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
package com.github.igarashitm.issues.soap.multiauth;

import javax.inject.Named;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.StringReader;

@Named("Transformers")
public class Transformers {

    @Transformer(from = "{urn:foo.bar}sayHelloResponse")
    public String transform(Element from) {
        return getElementValue(from, "return");
    }

    @Transformer(to = "{urn:foo.bar}sayHello")
    public Element transform(String input) {
        StringBuffer ackXml = new StringBuffer()
            .append("<foo:sayHello xmlns:foo=\"urn:foo.bar\">")
            .append("<arg0>")
            .append(input)
            .append("</arg0>")
            .append("</foo:sayHello>");

        return toElement(ackXml.toString());
    }

    /**
     * Returns the text value of a child node of parent.
     */
    private String getElementValue(Element parent, String elementName) {
        String value = null;
        NodeList nodes = parent.getElementsByTagName(elementName);
        if (nodes.getLength() > 0) {
            value = nodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return value;
    }

    private Element toElement(String xml) {
        DOMResult dom = new DOMResult();
        try {
            TransformerFactory.newInstance().newTransformer().transform(
                new StreamSource(new StringReader(xml)), dom);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ((Document) dom.getNode()).getDocumentElement();
    }
}
