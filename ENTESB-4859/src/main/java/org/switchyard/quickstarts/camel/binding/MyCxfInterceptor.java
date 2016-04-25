package org.switchyard.quickstarts.camel.binding;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.jboss.logging.Logger;

public class MyCxfInterceptor extends AbstractSoapInterceptor {

    private static final Logger logger = Logger.getLogger(MyCxfInterceptor.class);
    
    public MyCxfInterceptor() {
        super(Phase.PRE_LOGICAL);
        this.addBefore("HolderOutInterceptor");
        logger.info("!!!!! Instantiated !!!!!");
    }
    
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        logger.info("!!!!! Creating a Fault !!!!!");
        throw new Fault(new Exception("Whoa!"));
    }
}
