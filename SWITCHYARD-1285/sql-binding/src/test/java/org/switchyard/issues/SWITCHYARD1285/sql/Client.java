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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;


/**
 * .
 */
public final class Client {
    
    private static final String QUEUE_IN = "GreetingServiceQueue";
    private static final String USER = "guest";
    private static final String PASSWD = "guestp";
    
    /**
     * Private no-args constructor.
     */
    private Client() {
    }
    
    /**
     * Only execution point for this application.
     * @param ignored not used.
     * @throws Exception if something goes wrong.
     */
    public static void main(final String[] args) throws Exception {
        HornetQMixIn hqMixIn = new HornetQMixIn(false)
                                    .setUser(USER)
                                    .setPassword(PASSWD);
        hqMixIn.initialize();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:tcp://127.0.0.1/h2test", "sa", "sa");
            try {
                conn.createStatement().execute("drop table events;");
            } catch (Exception e) {}
            try {
                conn.createStatement().execute("drop table events2;");
            } catch (Exception e) {}
            
            conn.createStatement().execute("create table events (id identity primary key, sender varchar(255), receiver varchar(255), createdAt timestamp);");
            conn.createStatement().execute("create table events2 (id identity primary key, sender varchar(255), receiver varchar(255), createdAt timestamp);");
            conn.close();

            Session session = hqMixIn.createJMSSession();
            MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(QUEUE_IN));
            TextMessage message = session.createTextMessage();
            String payload = "foo";
            System.out.println(payload);
            message.setText(payload);
            producer.send(message);
            Thread.sleep(20000);

            conn = DriverManager.getConnection("jdbc:h2:tcp://127.0.0.1/h2test", "sa", "sa");
            ResultSet rs = conn.createStatement().executeQuery("select count(*) from events");
            rs.next();
            Assert.assertEquals(1, rs.getInt(1));
            rs.close();
            rs = conn.createStatement().executeQuery("select count(*) from events2");
            rs.next();
            Assert.assertEquals(4, rs.getInt(1));
            
        } finally {
            hqMixIn.uninitialize();
            conn.close();
        }
    }
    
}
