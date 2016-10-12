
package com.github.igarashitm.issues.soap.signencrypt;

import java.io.StringWriter;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServiceProvider(wsdlLocation = "FaultService.wsdl")
@ServiceMode(Mode.MESSAGE)
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public class FaultService implements Provider<SOAPMessage> {

    private final Logger _logger = LoggerFactory.getLogger(FaultService.class.getSimpleName());
    
    @Resource
    private WebServiceContext _wsContext;
    
    @Override
    public SOAPMessage invoke(SOAPMessage request) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage msg = messageFactory.createMessage();
            msg.getSOAPBody().addFault(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"), "Reason: cos I'm hungry");
            _logger.info(String.format("Returninig SOAP Fault [%s]", messageToString(msg)));
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String messageToString(SOAPMessage message) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            StringWriter writer = new StringWriter();
            t.transform(new DOMSource(message.getSOAPPart().getDocumentElement()), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}

