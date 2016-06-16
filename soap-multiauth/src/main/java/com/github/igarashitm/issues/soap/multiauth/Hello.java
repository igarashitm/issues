package com.github.igarashitm.issues.soap.multiauth;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import org.switchyard.common.codec.Base64;

@WebService(targetNamespace = "urn:foo.bar", serviceName = "HelloService", portName = "HelloPort")
public class Hello {

    @Resource
    private WebServiceContext _context;
    
    @WebMethod
    public String sayHello(String input) {
        HttpServletRequest req = (HttpServletRequest)_context.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        String authz = req.getHeader("Authorization");
        String encoded = authz.substring(6, authz.length());
        String decoded = Base64.decodeToString(encoded);
        String[] split = decoded.split(":", 2);
        return input + "/" + split[0];
    }

}
