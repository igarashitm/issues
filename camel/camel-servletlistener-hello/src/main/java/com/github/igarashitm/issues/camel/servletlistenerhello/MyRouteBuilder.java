package com.github.igarashitm.issues.camel.servletlistenerhello;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
            .setBody(simple("hello ${body}"))
            .log("${body}");
    }

}
