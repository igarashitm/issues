package com.github.igarashitm.issues.jms.artemis_invm_multiple;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.invm.InVMConnectorFactory;
import org.apache.activemq.artemis.core.remoting.impl.invm.TransportConstants;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.server.config.ConnectionFactoryConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MyTest {

    private static final Logger LOG = Logger.getLogger(MyTest.class);
    private static final String QUEUE = "TestQueue";
    private static final int MESSAGES_PER_THREAD = 100;
    private static final int THREADS = 25;
    private static final String JVM_ID = ManagementFactory.getRuntimeMXBean().getName();
    private static final String DATA_DIR = "target" + File.separator + "artemis-data-" + JVM_ID;

    private Set<ArtemisRunner> runners = new HashSet<>();

    @Test
    public void testNonPersistent() throws Throwable {
        doTest(false);
    }

    @Test
    public void testPersistent() throws Throwable {
        doTest(true);
    }

    protected void doTest(boolean persistent) throws Throwable {
        for (int i=0; i<THREADS; i++) {
            ArtemisRunner r = new ArtemisRunner(i, persistent);
            runners.add(r);
        }

        runners.forEach(r -> r.start());
        for (ArtemisRunner r : runners) {
            try {
                r.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (r.getError() != null) {
                throw r.getError();
            }
        }

        runners.forEach(r -> r.cleanup());
        new File(DATA_DIR).delete();
    }

    class ArtemisRunner extends Thread {
        private EmbeddedJMS broker;
        private ConnectionFactory connectionFactory;
        private String myDir;
        private Throwable error = null;

        public ArtemisRunner(int id, boolean persistent) {
            this.setName(String.format("%s-%05d", JVM_ID, id));
            myDir = DATA_DIR + File.separator + id;
            final Configuration configuration;
            try {
                configuration = new ConfigurationImpl()
                    .setPersistenceEnabled(persistent)
                    .setSecurityEnabled(false)
                    .setBindingsDirectory(myDir + File.separator + "bindings")
                    .setJournalDirectory(myDir + File.separator + "journal")
                    .setPagingDirectory(myDir + File.separator + "paging")
                    .setLargeMessagesDirectory(myDir + File.separator + "largemessages")
                    .addAcceptorConfiguration("invm", "vm://" + id)
                    .addConnectorConfiguration("invm", "vm://" + id);
            } catch (final Exception e) {
                throw new ExceptionInInitializerError(e);
            }

            final ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl().setName("cf")
                .setConnectorNames("invm").setBindings("cf");

            final JMSConfiguration jmsConfig = new JMSConfigurationImpl()
                .setConnectionFactoryConfigurations(Arrays.asList(cfConfig));

            broker = new EmbeddedJMS().setConfiguration(configuration).setJmsConfiguration(jmsConfig);

            try {
                broker.start();
            } catch (final Exception e) {
                throw new ExceptionInInitializerError(e);
            }

            final AddressSettings addressSettings = new AddressSettings()
                                                            .setDeadLetterAddress(new SimpleString("jms.queue.deadletter"))
                                                            .setExpiryAddress(new SimpleString("jms.queue.expired"));
            broker.getActiveMQServer().getAddressSettingsRepository().addMatch("#", addressSettings);

            TransportConfiguration transportConfigs = new TransportConfiguration(InVMConnectorFactory.class.getName());
            transportConfigs.getParams().put(TransportConstants.SERVER_ID_PROP_NAME, id);
            ServerLocator serviceLocator = ActiveMQClient.createServerLocator(false, transportConfigs);

            connectionFactory = new ActiveMQConnectionFactory(serviceLocator);
        }

        @Override
        public void run() {

            try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
                context.start();
                Destination queue = context.createQueue(QUEUE);
                JMSProducer producer = context.createProducer();

                for (int i=0; i<MESSAGES_PER_THREAD; i++) {
                    String body = String.format("%s-%05d", getName(), i);
                    producer.send(queue,  context.createTextMessage(body));
                    LOG.debug("SEND >>> " + body);
                }

                JMSConsumer consumer = context.createConsumer(queue);
                int received = 0;
                TextMessage msg;
                while ((msg = (TextMessage) consumer.receive(1000)) != null) {
                    Assert.assertEquals(String.format("%s-%05d", getName(), received++), msg.getText());
                    LOG.debug("RECV >>> " + msg.getText());
                }
                if (received != MESSAGES_PER_THREAD) {
                    error = new AssertionError(String.format("Wrong number of messages have been received, %d/%d", received, MESSAGES_PER_THREAD));
                }
            } catch (Throwable t) {
                error = t;
            }

            if (error instanceof RuntimeException) {
                throw (RuntimeException)error;
            } else if (error instanceof Error) {
                throw (Error)error;
            } else if (error != null){
                throw new RuntimeException(error);
            }
        }

        public Throwable getError() {
            return error;
        }

        public void cleanup() {
            try {
                broker.stop();
            } catch (Exception e) {
                LOG.warn("Failed to shutdown Artemis server", e);
            }
            try {
                Files.walk(Paths.get(myDir)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } catch (Exception e) {
                LOG.warn("Failed to remove artemis data file/dir: " + e.getMessage());
            }
        }
    }
}
