
package com.github.igarashitm.issues.soap.signencrypt.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.github.igarashitm.issues.soap.signencrypt.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Invoke_QNAME = new QName("http://signencrypt.soap.issues.igarashitm.github.com/", "invoke");
    private final static QName _InvokeResponse_QNAME = new QName("http://signencrypt.soap.issues.igarashitm.github.com/", "invokeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.github.igarashitm.issues.soap.signencrypt.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signencrypt.soap.issues.igarashitm.github.com/", name = "invoke")
    public JAXBElement<Object> createInvoke(Object value) {
        return new JAXBElement<Object>(_Invoke_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signencrypt.soap.issues.igarashitm.github.com/", name = "invokeResponse")
    public JAXBElement<Object> createInvokeResponse(Object value) {
        return new JAXBElement<Object>(_InvokeResponse_QNAME, Object.class, null, value);
    }

}
