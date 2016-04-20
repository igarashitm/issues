
package com.github.igarashitm.issues.soap_message_charset;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;

@WebServiceProvider
@ServiceMode(Mode.MESSAGE)
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class HelloWorld implements Provider<SOAPMessage> {

    private final Logger _logger = Logger.getLogger(HelloWorld.class.getSimpleName());
    
    @Resource
    private WebServiceContext _wsContext;
    
    @Override
    public SOAPMessage invoke(SOAPMessage request) {
        try {
            Map<String, List<String>> httpHeaders =
                    (Map<String, List<String>>) _wsContext.getMessageContext()
                                                         .get(MessageContext.HTTP_REQUEST_HEADERS);
            for (String key : httpHeaders.keySet()) {
                _logger.info(String.format("HTTP HEADER : %s=[%s]", key, httpHeaders.get(key)));
            }
            _logger.info("");
            HttpServletRequest servletRequest = (HttpServletRequest)_wsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
            _logger.info(String.format("ServletRequest.getCharacterEncoding()=[%s]", servletRequest.getCharacterEncoding()));
            _logger.info("");
            _logger.info(String.format("CRARACTER_SET_ENCODING=[%s]", request.getProperty(SOAPMessage.CHARACTER_SET_ENCODING)));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}

