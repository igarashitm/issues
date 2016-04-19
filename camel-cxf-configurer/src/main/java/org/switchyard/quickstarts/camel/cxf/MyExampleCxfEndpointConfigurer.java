package org.switchyard.quickstarts.camel.cxf;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.camel.component.cxf.CxfEndpointConfigurer;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.log4j.Logger;

@Named("myExampleCxfEndpointConfigurer")
public class MyExampleCxfEndpointConfigurer implements CxfEndpointConfigurer {

    private final Logger logger = Logger.getLogger(MyExampleCxfEndpointConfigurer.class);
    
    @Override
    public void configure(AbstractWSDLBasedEndpointFactory factoryBean) {
        factoryBean.getOutInterceptors().add(new MyExampleOutInterceptor());
        for (Interceptor<? extends Message> interceptor : factoryBean.getOutInterceptors()) {
            logger.info(String.format(">>>>> OutInterceptor [%s]", interceptor.getClass().getName()));
        }
        
        factoryBean.getFeatures().add(new MyExampleFeature());
        for (Feature feature : factoryBean.getFeatures()) {
            logger.info(String.format(">>>>> Feature [%s]", feature.getClass().getName()));
        }
    }

    class MyExampleOutInterceptor implements PhaseInterceptor<Message> {
        @Override
        public void handleMessage(Message message) throws Fault {
        }
        @Override
        public void handleFault(Message message) {
        }
        @Override
        public Set<String> getAfter() {
            return new HashSet<String>();
        }
        @Override
        public Set<String> getBefore() {
            return new HashSet<String>();
        }
        @Override
        public String getId() {
            return MyExampleOutInterceptor.class.getName();
        }
        @Override
        public String getPhase() {
            return Phase.SETUP;
        }
        @Override
        public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
            return null;
        }
    }
    
    class MyExampleFeature implements Feature {
        @Override
        public void initialize(Server server, Bus bus) {
        }
        @Override
        public void initialize(Client client, Bus bus) {
        }
        @Override
        public void initialize(InterceptorProvider interceptorProvider, Bus bus) {
        }
        @Override
        public void initialize(Bus bus) {
        }
        
    }
    
    @Override
    public void configureClient(Client client) {
        logger.info(">>>>> Configuring CXF client: " + client.toString());
        // This takes no effect as camel-cxf specifies its own RequestContext through a InvocationContext.
        // To set RequestContext properties, the Map object needs to be set as a camel exchange property
        // or a message header.
        Map<String,Object> requestContext = client.getRequestContext();
        requestContext.put("mtom-enabled", true);
        requestContext.put("security.callback-handler", new CallbackHandler() {
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                throw new UnsupportedCallbackException(null, "Succeeded to registering custom security CallbackHandler");
            }
        });
        requestContext.put("security.signature.properties", "not-existing-keystore.properties");
    }
    
    @Override
    public void configureServer(Server server) {
    }
    
}
