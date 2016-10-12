/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.quickstarts.camel.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.jboss.logging.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.Message;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.naming.NamingMixIn;
import org.switchyard.quickstarts.camel.sql.binding.Greeting;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

/**
 * Base class for SQL binding tests.
 * 
 * @author Lukasz Dywicki
 */
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
        mixins = CDIMixIn.class)
@RunWith(SwitchYardRunner.class)
public class CamelSqlBindingTest {

    private static Logger _logger = Logger.getLogger(CamelSqlBindingTest.class);
    private static JdbcDataSource dataSource;
    private static Connection connection;

    protected final static String RECEIVER = "Keith";
    protected final static String SENDER = "David";
    private static NamingMixIn namingMixIn;

    @ServiceOperation("GreetingService")
    private Invoker invoker;

    @BeforeClass
    public static void startUp() throws Exception {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        connection = dataSource.getConnection();

        String createStatement = "CREATE TABLE greetings ("
            + "id INT PRIMARY KEY AUTO_INCREMENT, "
            + "receiver VARCHAR(255), "
            + "sender VARCHAR(255) "
            + ");";

        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS greetings");
        connection.createStatement().executeUpdate(createStatement);

        namingMixIn = new NamingMixIn();
        namingMixIn.initialize();
        bindDataSource(namingMixIn.getInitialContext(), "java:jboss/myDS", dataSource);
    }

    private static void bindDataSource(InitialContext context, String name, DataSource ds) throws Exception {
        try {
            context.bind(name, ds);
        } catch (NameAlreadyBoundException e) {
            e.getMessage(); // ignore
        }
    }

    @AfterClass
    public static void shutDown() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
        namingMixIn.uninitialize();
    }

    @Test
    public void testSqlBinding() throws Exception {
        invoker.operation("generate").sendInOnly(null);
        invoker.operation("generate").sendInOnly(null);
        invoker.operation("generate").sendInOnly(null);
        _logger.debug("********** Created 3 records");

        // 1. testing 'select * from greetings' via sql reference binding
        _logger.debug("********** Retrieving all records");
        Message msg = invoker.operation("selectAll").sendInOut(null);
        Object out = msg.getContent();
        _logger.debug("********** Retrieved : " + out);
        Assert.assertNotNull("selectAll returned null", out);
        Assert.assertTrue("selectAll returned " + out.getClass().getName(), out instanceof Greeting[]);
        Greeting[] outGreetings = Greeting[].class.cast(out);
        _logger.debug("********** Converted to Greeting objects : " + Arrays.deepToString(outGreetings));
        Assert.assertEquals(3, outGreetings.length);

        // 2. testing 'select * from greetings where id = #' via sql reference binding
        Greeting expectedGreeting = outGreetings[2];
        _logger.debug("********** Retrieving 1 record using id='" + expectedGreeting.getId() + "'");
        msg = invoker.operation("selectOne").sendInOut(expectedGreeting.getId());
        out = msg.getContent();
        _logger.debug("********** Retrieved : " + out);
        Assert.assertNotNull("selectOne returned null with id='" + expectedGreeting.getId() + "'", out);
        Assert.assertTrue("selectOne returned " + out.getClass().getName(), out instanceof Greeting);
        Greeting actualGreeting = Greeting.class.cast(out);
        _logger.debug("********** Converted to Greeting objects : " + actualGreeting);
        Assert.assertEquals(expectedGreeting.getReceiver(), actualGreeting.getReceiver());
        Assert.assertEquals(expectedGreeting.getSender(), actualGreeting.getSender());
        Assert.assertEquals(expectedGreeting.getId(), actualGreeting.getId());
    }

}
