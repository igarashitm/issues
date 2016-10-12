package com.example.switchyard.switchyard_example;

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

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Assert;
import org.junit.Test;

public class ActiveMQClient {

    private ConnectionFactory _connectionFactory;
    private Queue _inboundQueue;
    private Queue _inboundQueueJca;
    private Queue _outboundQueue;
    private Queue _outboundQueueJca;
    
    public static void main(String[] args) throws Exception {
        ActiveMQClient c = new ActiveMQClient();
        c.testJmsJca();
        c.testJmsCamel();
    }

    public ActiveMQClient() throws Exception {
        _connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_BIND_URL);
        Session session = createSession();
        try {
            _inboundQueue = session.createQueue("inboundQueue");
            _inboundQueueJca = session.createQueue("inboundQueueJca");
            _outboundQueue = session.createQueue("outboundQueue");
            _outboundQueueJca = session.createQueue("outboundQueueJca");
            receiveUntilEmpty(session, _outboundQueue);
            receiveUntilEmpty(session, _outboundQueueJca);
        } finally {
            session.close();
        }
    }
    
    private  Session createSession() throws Exception {
        Connection conn = _connectionFactory.createConnection();
        conn.start();
        return conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
    private int receiveUntilEmpty(Session session, Queue queue) {
        Message message = null;
        int count = 0;
        do {
            try {
                message = receiveMessage(session, queue);
                if (message != null) {
                    // JCA queue returns a message of the ObjectMessage type
                    Object messageText = (message instanceof TextMessage ? ((TextMessage) message).getText()
                            : ((ObjectMessage) message).getObject());
                    System.out.println("===== Received message: " + messageText + " =====");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
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

    public void testJmsJca() throws Exception {
        Session session = createSession();
        try {
            sendMessage("there", session, _inboundQueueJca);
        } finally {
            session.close();
        }
        
        Thread.sleep(10000);
        
        session = createSession();
        try {
            verifyOutboundQueues(session);
        } finally {
            session.close();
        }
    }

    @Test
    public void testJmsCamel() throws Exception {
        Session session = createSession();
        try {
            sendMessage("there", session, _inboundQueue);
        } finally {
            session.close();
        }
        
        Thread.sleep(10000);
        
        session = createSession();
        try {
            verifyOutboundQueues(session);
        } finally {
            session.close();
        }
    }

    private void verifyOutboundQueues(Session session) throws Exception {
        int countJca = receiveUntilEmpty(session, _outboundQueueJca);
        Assert.assertEquals("JCA queue should contains 3 messages", 3, countJca);

        int countCamel = receiveUntilEmpty(session, _outboundQueue);
        Assert.assertEquals("Camel queue should contains 3 messages", 3, countCamel);
    }

    private void sendMessage(String textMessage, Session session, Destination destination) throws Exception {
        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage(textMessage);
        producer.send(message);
    }

    private Message receiveMessage(Session session, Destination destination) throws Exception {
        MessageConsumer consumer = session.createConsumer(destination);
        return consumer.receive(2000);
    }
}
