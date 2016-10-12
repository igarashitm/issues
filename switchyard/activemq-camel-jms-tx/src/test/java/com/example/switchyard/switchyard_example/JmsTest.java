package com.example.switchyard.switchyard_example;

import java.io.File;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JmsTest {

    // connection factories
    @Resource(mappedName = "java:/${jmsImpl}/JmsXAIn")
    ConnectionFactory cfIn;

    @Resource(mappedName = "java:/${jmsImpl}/JmsXAInJca")
    ConnectionFactory cfInJca;

    @Resource(mappedName = "java:/${jmsImpl}/JmsXAOut")
    ConnectionFactory cfOut;

    @Resource(mappedName = "java:/${jmsImpl}/JmsXAOutJca")
    ConnectionFactory cfOutJca;

    // queues
    @Resource(mappedName = "java:/${jmsImpl}/InboundQueue")
    Queue inboundQueue;

    @Resource(mappedName = "java:/${jmsImpl}/InboundQueueJca")
    Queue inboundQueueJca;

    @Resource(mappedName = "java:/${jmsImpl}/OutboundQueue")
    Queue outboundQueue;

    @Resource(mappedName = "java:/${jmsImpl}/OutboundQueueJca")
    Queue outboundQueueJca;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(ZipImporter.class, "activemq-camel-jms-tx.jar")
                .importFrom(new File("target/activemq-camel-jms-tx.jar")).as(JavaArchive.class);
    }

    @Before
    public void clearBefore() {
        receiveUntilEmpty(cfOut, outboundQueue);
        receiveUntilEmpty(cfOutJca, outboundQueueJca);
    }

    private int receiveUntilEmpty(ConnectionFactory cf, Queue queue) {
        Message message = null;
        int count = 0;
        do {
            message = receiveMessage(cf, queue);
            if (message != null) {
                try {
                    // JCA queue returns a message of the ObjectMessage type
                    Object messageText = (message instanceof TextMessage ? ((TextMessage) message).getText()
                            : ((ObjectMessage) message).getObject());
                    System.out.println("===== Received message: " + messageText + " =====");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                count++;
            }
        } while (message != null);

        if (count != 0) {
            try {
                System.out.println("===== Received " + count + " messages from queue " + queue.getQueueName()
                        + " =====");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    @Test
    public void testJmsJca() throws Exception {
        sendMessage("there", cfInJca, inboundQueueJca);

        Thread.sleep(10000);

        verifyOutboundQueues();
    }

    @Test
    public void testJmsCamel() throws Exception {
        sendMessage("there", cfIn, inboundQueue);

        Thread.sleep(10000);

        verifyOutboundQueues();
    }

    private void verifyOutboundQueues() {
        int countJca = receiveUntilEmpty(cfOutJca, outboundQueueJca);
        Assert.assertEquals("JCA queue should contains 3 messages", 3, countJca);

        int countCamel = receiveUntilEmpty(cfOut, outboundQueue);
        Assert.assertEquals("Camel queue should contains 3 messages", 3, countCamel);
    }

    private void sendMessage(String textMessage, ConnectionFactory cf, Destination destination) {
        Connection connection = null;
        Session session = null;
        try {
            connection = cf.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage(textMessage);
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private Message receiveMessage(ConnectionFactory cf, Destination destination) {
        Message reply = null;
        Connection connection = null;
        Session session = null;
        try {
            connection = cf.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);
            reply = consumer.receive(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        return reply;
    }
}
