package com.github.igarashitm.issues.soap.multiauth;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.DispatchImpl;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

import com.github.igarashitm.issues.soap.multiauth.Hello;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = CDIMixIn.class)
public class MultiAuthTest {

    private static final Logger _log = Logger.getLogger(MultiAuthTest.class);
    
    private static final String NAMESPACE = "urn:foo.bar";
    private static final QName SERVICE = new QName(NAMESPACE, "HelloService");
    private static final QName PORT = new QName(NAMESPACE, "HelloPort");
    private static final String ADDR = "http://127.0.0.1:18001/Hello";
    private static final QName PAYLOAD_TYPE = new QName(NAMESPACE, "sayHello");
    private static final String PAYLOAD_1 = "<foo:sayHello xmlns:foo=\"urn:foo.bar\"><arg0>user1</arg0></foo:sayHello>";
    private static final String PAYLOAD_2 = "<foo:sayHello xmlns:foo=\"urn:foo.bar\"><arg0>user2</arg0></foo:sayHello>";
    
    private final URL WSDL_URL;
    
    private Endpoint _endpoint;
    
    @ServiceOperation("WSInvokerService.invokeHello1")
    private Invoker _hello1;
    
    @ServiceOperation("WSInvokerService.invokeHello2")
    private Invoker _hello2;
    
    public MultiAuthTest() throws Exception{
        WSDL_URL = new URL(ADDR + "?wsdl");
    }
    
    @Before
    public void before() {
        _endpoint = Endpoint.publish(ADDR, new Hello());
    }
    
    @After
    public void after() {
        _endpoint.stop();
    }
    
    @Test
    public void testPlainClient() throws Exception {
        Service service1 = Service.create(WSDL_URL, SERVICE);
        Dispatch<Source> dispatch1 = service1.createDispatch(PORT, Source.class, Service.Mode.PAYLOAD);
        dispatch1.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ADDR);
        Client client1 = ((DispatchImpl)dispatch1).getClient();
        HTTPConduit conduit1 = (HTTPConduit)client1.getConduit();
        AuthorizationPolicy policy1 = new AuthorizationPolicy();
        policy1.setUserName("user1");
        policy1.setPassword("pass1");
        policy1.setAuthorizationType("Basic");
        conduit1.setAuthorization(policy1);
        
        Service service2 = Service.create(WSDL_URL, SERVICE);
        Dispatch<Source> dispatch2 = service2.createDispatch(PORT, Source.class, Service.Mode.PAYLOAD);
        dispatch2.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ADDR);
        Client client2 = ((DispatchImpl)dispatch2).getClient();
        HTTPConduit conduit2 = (HTTPConduit)client2.getConduit();
        AuthorizationPolicy policy2 = new AuthorizationPolicy();
        policy2.setUserName("user2");
        policy2.setPassword("pass2");
        policy2.setAuthorizationType("Basic");
        conduit2.setAuthorization(policy2);
        
        Transformer trfm = TransformerFactory.newInstance().newTransformer();
        Source source1 = dispatch1.invoke(new StreamSource(new StringReader(PAYLOAD_1)));
        StringWriter writer1 = new StringWriter();
        trfm.transform(source1, new StreamResult(writer1));
        String res1 = writer1.toString();
        _log.info(String.format("1-Received:[%s]", res1));
        Source source2 = dispatch2.invoke(new StreamSource(new StringReader(PAYLOAD_2)));
        StringWriter writer2 = new StringWriter();
        trfm.transform(source2, new StreamResult(writer2));
        String res2 = writer2.toString();
        _log.info(String.format("2-Received:[%s]", res2));
        assertTrue(res1, res1.contains("user1/user1"));
        assertTrue(res2, res2.contains("user2/user2"));
    }
    
    @Test
    public void testSwitchYard() throws Exception {
        String res1 = _hello1.sendInOut("user1").getContent(String.class);
        _log.info(String.format("1-Received:[%s]", res1));
        String res2 = _hello2.sendInOut("user2").getContent(String.class);
        _log.info(String.format("2-Received:[%s]", res2));
        assertTrue(res1, res1.contains("user1/user1"));
        assertTrue(res2, res2.contains("user2/user2"));
    }
}
