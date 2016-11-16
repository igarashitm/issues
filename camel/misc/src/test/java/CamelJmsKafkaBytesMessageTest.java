import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kafka.metrics.KafkaMetricsReporter;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.server.KafkaServerStartable;
import kafka.utils.SystemTime$;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.language.Bean;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.collection.mutable.Buffer;

public class CamelJmsKafkaBytesMessageTest extends CamelTestSupport {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_IN = "test-in";
    private static final String QUEUE_OUT = "test-out";
    private static final String TEST_MESSAGE = "test message foo bar hoge fuga";
    private TestingServer _zk;
    private KafkaServerStartable _kafka;
    private BrokerService _broker;
    private ActiveMQConnectionFactory _factory;

    @Before
    public void before() throws Exception {
        _broker = new BrokerService();
        _broker.setPersistent(false);
        _broker.addConnector(BROKER_URL);
        _broker.start();
        _factory = new ActiveMQConnectionFactory(BROKER_URL);
        
        _zk = new TestingServer();
        Properties props = new Properties();
        props.getProperty("host.name", "127.0.0.1");
        props.setProperty("port", "9092");
        props.setProperty("broker.id", "1");
        props.setProperty("log.dir", "target/kafkalog");
        props.setProperty("zookeeper.connect", _zk.getConnectString());
        props.setProperty("auto.create.topics.enable", "true");
        _kafka = new KafkaServerStartable(new KafkaConfig(props));
        _kafka.startup();
    }
    
    @Test
    public void test() throws Exception {
        Connection conn = _factory.createConnection();
        conn.start();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue(QUEUE_IN));
        BytesMessage msg = session.createBytesMessage();
        msg.writeBytes(TEST_MESSAGE.getBytes());
        producer.send(msg);
        session.close();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(session.createQueue(QUEUE_OUT));
        Message answer = consumer.receive(5000);
        Assert.assertNotNull(answer);
        Assert.assertTrue(answer.getClass().getName(), BytesMessage.class.isAssignableFrom(answer.getClass()));
        byte[] body = new byte[TEST_MESSAGE.length()];
        ((BytesMessage)answer).readBytes(body);
        Assert.assertEquals(TEST_MESSAGE, new String(body));
        conn.close();
    }
    
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                JmsComponent jms = getContext().getComponent("jms", JmsComponent.class);
                jms.setConnectionFactory(new ActiveMQConnectionFactory(BROKER_URL));
                from("jms:queue:" + QUEUE_IN)
                    .log(">>>>> 1 >>>>> ${body.getClass.getName}:${body}")
                    .to("kafka:localhost:9092?topic=jpa-cache" + "&requestRequiredAcks=-1"
                        + "&producerBatchSize=1"
                        + "&serializerClass=org.apache.kafka.common.serialization.ByteArraySerializer"
                        + "&keySerializerClass=org.apache.kafka.common.serialization.ByteArraySerializer")
                    .log(">>>>> 2 >>>>> ${body.getClass.getName}:${body}");

                from("kafka:localhost:9092?topic=jpa-cache&groupId=cache&autoOffsetReset=earliest&consumersCount=1"
                     + "&valueDeserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer"
                     + "&keyDeserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer"
                     + "&autoCommitIntervalMs=1000&autoCommitEnable=true")
                    .log(">>>>> 3 >>>>> ${body.getClass.getName}:${body}")
                    .to("jms:queue:" + QUEUE_OUT)
                    .log(">>>>> 4 >>>>> ${body.getClass.getName}:${body}");
            }
        };
    }

}
