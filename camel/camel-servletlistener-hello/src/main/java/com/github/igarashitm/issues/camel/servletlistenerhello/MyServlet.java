package com.github.igarashitm.issues.camel.servletlistenerhello;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CamelContext camelContext;
    
    @Override
    public void init() {
        this.camelContext = (CamelContext) this.getServletContext().getAttribute("CamelContext");
    }
    
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        assert camelContext != null;
        assert getServletContext().getAttribute("CamelContext") != null;
        String answer = camelContext.createProducerTemplate().requestBody("direct:in", "foo", String.class);
        res.setContentType("text/plain");
        try {
            PrintWriter pw = res.getWriter();
            pw.append(answer);
        } catch (Exception e) {
            new ServletException(e);
        }
    }
}
