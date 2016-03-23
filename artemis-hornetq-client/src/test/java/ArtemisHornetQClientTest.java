import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class ArtemisHornetQClientTest {

    private static Logger _logger = Logger.getLogger(ArtemisHornetQClientTest.class);
    private static final String USER = "guest";
    private static final String PASSWD = "guestp.1";
    private static final String QUEUE = "DLQ";
    private static final int NUM = 5;
    private long start;
    
    @Before
    public void before() {
        start = System.currentTimeMillis();
    }
   
    @Test
    public void testHornetQClient() throws Exception {
        Map<String,Object> transportParams = new HashMap<String,Object>();
        transportParams.put(org.hornetq.core.remoting.impl.netty.TransportConstants.HTTP_UPGRADE_ENABLED_PROP_NAME, true);
        transportParams.put(org.hornetq.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME, 8080);
        org.hornetq.api.core.TransportConfiguration transportConfiguration =
                new org.hornetq.api.core.TransportConfiguration(
                        org.hornetq.core.remoting.impl.netty.NettyConnectorFactory.class.getName()
                        , transportParams);
        Destination dest = org.hornetq.api.jms.HornetQJMSClient.createQueue(QUEUE);
        
        print(String.format("Starting to send/receive using HornetQ client API - VMName=%s",ManagementFactory.getRuntimeMXBean().getName()));
        org.hornetq.jms.client.HornetQConnectionFactory cf = null;
        Connection conn = null;
        Session session = null;
        try {
            cf = org.hornetq.api.jms.HornetQJMSClient.createConnectionFactoryWithoutHA(
                            org.hornetq.api.jms.JMSFactoryType.CF,transportConfiguration);
            cf.setUseGlobalPools(false);
            conn = cf.createConnection(USER, PASSWD);
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(dest);
            for (int i=0; i<NUM; i++) {
                String text = "foobar-" + i;
                producer.send(session.createTextMessage(text));
                print(String.format("* * *  Sent a message '%s' to '%s'", text, dest.toString()));
            }
            producer.close();
        } finally {
            if (session != null) session.close();
            if (conn != null) conn.close();
            if (cf != null) cf.close();
        }
        
        try {
            cf = org.hornetq.api.jms.HornetQJMSClient.createConnectionFactoryWithoutHA(
                            org.hornetq.api.jms.JMSFactoryType.CF,transportConfiguration);
            cf.setUseGlobalPools(false);
            conn = cf.createConnection(USER, PASSWD);
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(dest);
            conn.start();
            int count = 0;
            while (count < NUM) {
                TextMessage msg = (TextMessage)consumer.receive(1000);
                if (msg != null) {
                    count++;
                    print(String.format("* * *  Received a message '%s' to '%s'",msg.getText(), dest.toString()));
                } else {
                    print(String.format("* * *  No message was received from '%s'", dest.toString()));
                }
            }
            consumer.close();
        } finally {
            if (session != null) session.close();
            if (conn != null) conn.close();
            if (cf != null) cf.close();
        }
        print("Finished (HornetQ client)");
    }
    
    @Test
    public void testArtemisClient() throws Exception {
        Map<String,Object> transportParams = new HashMap<String,Object>();
        transportParams.put(org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.HTTP_UPGRADE_ENABLED_PROP_NAME, true);
        transportParams.put(org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME, 8080);
        org.apache.activemq.artemis.api.core.TransportConfiguration transportConfiguration =
                new org.apache.activemq.artemis.api.core.TransportConfiguration(
                        org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory.class.getName()
                        , transportParams);
        Destination dest = org.apache.activemq.artemis.api.jms.ActiveMQJMSClient.createQueue(QUEUE);

        print("Starting to send/receive using Artemis client API");
        org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory cf = null;
        Connection conn = null;
        Session session = null;
        try {
            cf = org.apache.activemq.artemis.api.jms.ActiveMQJMSClient.createConnectionFactoryWithoutHA(
                            org.apache.activemq.artemis.api.jms.JMSFactoryType.CF,transportConfiguration);
            conn = cf.createConnection(USER, PASSWD);
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(dest);
            for (int i=0; i<NUM; i++) {
                String text = "foobar-" + i;
                producer.send(session.createTextMessage(text));
                print(String.format("* * *  Sent a message '%s' to '%s'", text, dest.toString()));
            }
            producer.close();
        } finally {
            if (session != null) session.close();
            if (conn != null) conn.close();
            if (cf != null) cf.close();
        }
        
        try {
            cf = org.apache.activemq.artemis.api.jms.ActiveMQJMSClient.createConnectionFactoryWithoutHA(
                    org.apache.activemq.artemis.api.jms.JMSFactoryType.CF,transportConfiguration);
            conn = cf.createConnection(USER, PASSWD);
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(dest);
            int count = 0;
            while (count < NUM) {
                TextMessage msg = (TextMessage)consumer.receive(1000);
                if (msg != null) {
                    count++;
                    print(String.format("* * *  Received a message '%s' to '%s'",msg.getText(), dest.toString()));
                } else {
                    print(String.format("* * *  No message was received from '%s'", dest.toString()));
                }
            }
            consumer.close();
        } finally {
            if (session != null) session.close();
            if (conn != null) conn.close();
            if (cf != null) cf.close();
        }
        
        print("Finished (Artemis client)");
    }

    private void print(String message) {
        _logger.info(String.format("[\t%s]: %s", (System.currentTimeMillis() - start), message));
    }
}
