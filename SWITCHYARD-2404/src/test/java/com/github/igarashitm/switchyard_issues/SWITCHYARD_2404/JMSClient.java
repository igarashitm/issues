package com.github.igarashitm.switchyard_issues.SWITCHYARD_2404;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;

public class JMSClient {
    public static void main(String[] args) throws Exception {
        new JMSClient().perform();
    }
    
    public void perform() throws Exception {
        String user = System.getProperty("hornetq.user");
        String password = System.getProperty("hornetq.password");
        String host = System.getProperty("hornetq.host");
        String port = System.getProperty("hornetq.port");
        String httpUpgradeEnabled = System.getProperty("hornetq.http.upgrade.enabled");
        Map<String,Object> transportParams = new HashMap<String,Object>();
        if (host != null) {
            transportParams.put(TransportConstants.HOST_PROP_NAME, host);
        }
        if (port != null) {
            transportParams.put(TransportConstants.PORT_PROP_NAME, port);
        }
        if (httpUpgradeEnabled != null) {
            transportParams.put(TransportConstants.HTTP_UPGRADE_ENABLED_PROP_NAME, httpUpgradeEnabled);
        }

        Connection connection = null;
        try {
            ConnectionFactory cf = new HornetQConnectionFactory(false,
                new TransportConfiguration(NettyConnectorFactory.class.getName(), transportParams));
            connection = cf.createConnection(user, password);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
                System.out.println(java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
                Thread.sleep(10000);
            }
        }
    }
}
