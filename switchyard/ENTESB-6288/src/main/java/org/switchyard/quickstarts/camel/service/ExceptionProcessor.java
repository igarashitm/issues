package org.switchyard.quickstarts.camel.service;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Named("exceptionProcessor")
public class ExceptionProcessor implements Processor {
    public void process(Exchange ex) throws Exception {
        throw new Exception("throwing");
    }
}
