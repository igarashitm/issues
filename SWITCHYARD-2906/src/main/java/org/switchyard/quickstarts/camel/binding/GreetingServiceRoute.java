package org.switchyard.quickstarts.camel.binding;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class GreetingServiceRoute extends RouteBuilder {

    /**
     * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
     */
    public void configure() {
        from("switchyard://GreetingService")
            .setBody(body().regexReplaceAll("\n", "").regexReplaceAll("\r", ""))
            .setHeader("PrefixFileName", body().regexReplaceAll(" ", "_").append("-processed"))
            .setProperty("PrefixFileName", body().regexReplaceAll(" ", "_").append("-processed"))
            
            .choice()
                .when(body().isEqualTo("Checked Exception"))
                    .throwException(new Exception("Throwing checked Exception"))
                .when(body().isEqualTo("Unchecked Exception"))
                    .throwException(new RuntimeException("Throwing unchecked Exception"))
                .otherwise()
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            exchange.getIn().setBody(new Exception("Sending checked Exception as a content"));
                        }
                    });
    }

}
