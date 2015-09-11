package org.apache.camel.example.camel_activemq_cc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyProcessor implements Processor {

    private static Logger LOG = LoggerFactory.getLogger(MyProcessor.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.info(Thread.currentThread().getName() + "-" + exchange.getIn().getBody());
        Thread.sleep(500);
    }

}
