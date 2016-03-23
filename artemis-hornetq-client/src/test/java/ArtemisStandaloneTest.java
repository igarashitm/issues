import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSQueueConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class ArtemisStandaloneTest {

    private static Logger _logger = Logger.getLogger(ArtemisStandaloneTest.class);
    private static final String USER = "guest";
    private static final String PASSWD = "guestp.1";
    private static final String QUEUE = "DLQ";
    private static final int NUM_MSG = 5;
    private static final int NUM_REPEAT = 10;
    private long start;
    private EmbeddedJMS _server;
    
    @Before
    public void before() throws Exception {
        start = System.currentTimeMillis();
        
        _server = new EmbeddedJMS();
        Configuration config = new ConfigurationImpl();
        HashSet<TransportConfiguration> transports = new HashSet<TransportConfiguration>();
        transports.add(new TransportConfiguration(NettyAcceptorFactory.class.getName()));
        config.setAcceptorConfigurations(transports);
        config.setSecurityEnabled(false);
        _server.setConfiguration(config);
        JMSConfiguration jmsConfig = new JMSConfigurationImpl();
        JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl().setName("DLQ");
        jmsConfig.getQueueConfigurations().add(queueConfig);
        _server.setJmsConfiguration(jmsConfig);
        _server.start();
    }
    
    @After
    public void after() throws Exception {
        _server.stop();
    }
    
    @Test
    public void repeat() throws Exception {
        for (int i=0; i<NUM_REPEAT; i++) {
            testArtemisClient();
        }
    }
    
    @Test
    public void testArtemisClient() throws Exception {
        Map<String,Object> transportParams = new HashMap<String,Object>();
        //transportParams.put(org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.HTTP_UPGRADE_ENABLED_PROP_NAME, true);
        //transportParams.put(org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME, 8080);
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
            for (int i=0; i<NUM_MSG; i++) {
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
            while (count < NUM_MSG) {
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
