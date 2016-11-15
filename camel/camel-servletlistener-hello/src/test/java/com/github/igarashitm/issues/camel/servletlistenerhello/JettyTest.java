package com.github.igarashitm.issues.camel.servletlistenerhello;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyTest {
    private static final Logger LOG = LoggerFactory.getLogger(JettyTest.class);
    
    @Test
    public void test() throws Exception {
        String urlString = System.getProperty("url") + "MyServlet";
        LOG.info("Connecting to {}", urlString);
        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
        String line = null;
        StringBuilder buf = new StringBuilder();
        while((line = reader.readLine()) != null) {
            buf.append(line);
        }
        String answer = buf.toString();
        LOG.info("Got a response '{}'", answer);
        assert answer.contains("hello foo");
    }

}
