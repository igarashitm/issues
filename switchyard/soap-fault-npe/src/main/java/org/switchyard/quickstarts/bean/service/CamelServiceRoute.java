package org.switchyard.quickstarts.bean.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.DefaultTraceFormatter;
import org.apache.camel.processor.interceptor.Tracer;

/**
 * @author z8a006 date 17/04/2015
 * 
 */
public class CamelServiceRoute extends RouteBuilder {

    private static final String CTRL_TRACE = "abs.allianz.ctrl.trace";

    /**
     * The Camel route is configured via this method. The from endpoint is
     * required to be a SwitchYard service.
     */
    @Override
    public void configure() {
        onException(Exception.class)
                .setFaultBody(method(ExceptionProcessor.class, "process"))
                .handled(true).end();

        Tracer tracer = new Tracer();

        DefaultTraceFormatter formatter = new DefaultTraceFormatter();
        formatter.setShowBodyType(false);
        formatter.setShowBreadCrumb(false);
        formatter.setShowBody(false);
        formatter.setShowBodyType(false);
        formatter.setShowBreadCrumb(false);
        formatter.setShowRouteId(true);
        formatter.setShowBody(false);
        tracer.setLogStackTrace(true);
        tracer.setTraceOutExchanges(false);
        tracer.setTraceInterceptors(false);
        tracer.setFormatter(formatter);

        tracer.setFormatter(formatter);

        getContext().addInterceptStrategy(tracer);

        getContext().setUseMDCLogging(true);

        getContext()
                .setTracing(Boolean.valueOf(System.getProperty(CTRL_TRACE)));

        from("switchyard://OrderService").routeId("Dispath service")
                .process(new Processor() {
                    @Override
                    public void process(Exchange arg0) throws Exception {
                        throw new Exception("dummy");
                    }
                });

    }

}
