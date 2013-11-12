package org.switchyard.issues.SWITCHYARD1285.jms;

import java.io.File;
import java.util.zip.ZipFile;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */

@RunWith(Arquillian.class)
public class CamelJMSBindingTest {
    private static final String QUEUE_FILE = "target/test-classes/SWITCHYARD-1285-hornetq-jms.xml";
    private static final String USER = "guest";
    private static final String PASSWD = "guestp.1";
    private static final String JAR_FILE = "target/SWITCHYARD-1285-jms-binding.jar";

    @Resource(mappedName = "ConnectionFactory")
    private ConnectionFactory _connectionFactory;

    @Resource(mappedName = "GreetingServiceQueue")
    private Destination _greetingServiceQueue;

    @Resource(mappedName = "GreetingStoreQueue")
    private Destination _greetingStoreQueue;

    @Resource(mappedName = "AnotherTransactionStoreQueue")
    private Destination _anotherTransactionStoreQueue;

    @Deployment
    public static Archive<?> createTestArchive() {
        File artifact = new File(JAR_FILE);
        try {
            return ShrinkWrap.create(ZipImporter.class, artifact.getName())
                             .importFrom(new ZipFile(artifact))
                             .as(JavaArchive.class)
                             .addAsManifestResource(new File(QUEUE_FILE));
        } catch (Exception e) {
            throw new RuntimeException(JAR_FILE + " not found. Do \"mvn package\" before the test", e);
         }
    }
    

    @Test
    public void testJMSBinding() throws Exception {
        Connection conn = _connectionFactory.createConnection(USER, PASSWD);
        conn.start();
        
        try {
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(_greetingServiceQueue);
            TextMessage textMsg = session.createTextMessage();
            textMsg.setText("SwitchYard");
            producer.send(textMsg);
            session.close();
        
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(_greetingStoreQueue);
            assertStoredMessage(consumer.receive(30000));
            Assert.assertNull(consumer.receive(1000));
            consumer.close();

            consumer = session.createConsumer(_anotherTransactionStoreQueue);
            assertStoredMessage(consumer.receive(1000));
            assertStoredMessage(consumer.receive(1000));
            assertStoredMessage(consumer.receive(1000));
            assertStoredMessage(consumer.receive(1000));
            Assert.assertNull(consumer.receive(1000));
            consumer.close();
            
            session.close();
        } finally {
            conn.close();
        }
        Thread.sleep(1000);
    }
    
    private void assertStoredMessage(Message msg) throws Exception {
        Assert.assertNotNull(msg);
        String result = null;
        if (msg instanceof TextMessage) {
            result = TextMessage.class.cast(msg).getText();
        } else if (msg instanceof ObjectMessage) {
            result = ObjectMessage.class.cast(msg).getObject().toString();
        } else {
            Assert.fail("Message is not TextMessage nor ObjectMessage, but is " + msg);
        }
        Assert.assertEquals("Hello there, SwitchYard :-)", result);
    }
}
