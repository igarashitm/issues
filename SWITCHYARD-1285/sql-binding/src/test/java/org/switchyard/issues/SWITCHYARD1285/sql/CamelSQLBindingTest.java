/*
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat Middleware
 * LLC, and individual contributors by the @authors tag. See the copyright.txt
 * in the distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.switchyard.issues.SWITCHYARD1285.sql;

import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.zip.ZipFile;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.h2.tools.Server;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public final class CamelSQLBindingTest {
    private static final String QUEUE_FILE = "target/test-classes/SWITCHYARD-1285-hornetq-jms.xml";
    private static final String GREET_DS_FILE = "target/test-classes/greet-xa-ds.xml";
    private static final String GREET2_DS_FILE = "target/test-classes/greet2-xa-ds.xml";
    private static final String JAR_FILE = "target/SWITCHYARD-1285-sql-binding.jar";
    private static final String JMS_USER = "guest";
    private static final String JMS_PASSWD = "guestp.1";
    private static final String H2_URL = "jdbc:h2:tcp://127.0.0.1/h2test";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWD = "sa";

    @Resource(mappedName = "ConnectionFactory")
    private ConnectionFactory _connectionFactory;
    
    @Resource(mappedName = "GreetingServiceQueue")
    private Destination _greetingServiceQueue;
    
    @Deployment
    public static Archive<?> createTestArchive() throws Exception {
        Server.createTcpServer().start();
        
        File artifact = new File(JAR_FILE);
        try {
            return ShrinkWrap.create(ZipImporter.class, artifact.getName())
                             .importFrom(new ZipFile(artifact))
                             .as(JavaArchive.class)
                             .addAsManifestResource(new File(GREET_DS_FILE))
                             .addAsManifestResource(new File(GREET2_DS_FILE))
                             .addAsManifestResource(new File(QUEUE_FILE));
        } catch (Exception e) {
            throw new RuntimeException(JAR_FILE + " not found. Do \"mvn package\" before the test", e);
        }
    }

    @Before
    public void before() throws Exception {
        java.sql.Connection dbconn = null;
        try {
            dbconn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWD);
            try {
                dbconn.createStatement().execute("drop table events;");
            } catch (Exception e) {}
            try {
                dbconn.createStatement().execute("drop table events2;");
            } catch (Exception e) {}
            
            dbconn.createStatement().execute("create table events (id identity primary key, sender varchar(255), receiver varchar(255), createdAt timestamp);");
            dbconn.createStatement().execute("create table events2 (id identity primary key, sender varchar(255), receiver varchar(255), createdAt timestamp);");
        } finally {
            dbconn.close();
        }
    }

    @Test
    public void testJPABinding() throws Exception {
        javax.jms.Connection jmsconn = _connectionFactory.createConnection(JMS_USER, JMS_PASSWD);
        jmsconn.start();
        java.sql.Connection dbconn = null;

        try {
            Session session = jmsconn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(_greetingServiceQueue);
            TextMessage message = session.createTextMessage();
            String payload = "foo";
            System.out.println(payload);
            message.setText(payload);
            producer.send(message);
            session.close();
            Thread.sleep(20000);

            dbconn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWD);
            ResultSet rs = dbconn.createStatement().executeQuery("select count(*) from events");
            rs.next();
            Assert.assertEquals(1, rs.getInt(1));
            rs.close();
            rs = dbconn.createStatement().executeQuery("select count(*) from events2");
            rs.next();
            Assert.assertEquals(4, rs.getInt(1));
        } finally {
            jmsconn.close();
            if (dbconn != null) {
                dbconn.close();
            }
        }
    }
}
