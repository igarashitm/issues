import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sjms.SjmsComponent;
import org.apache.camel.component.sjms.jms.ConnectionFactoryResource;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelSjmsTxTest {

    private static final Logger LOG = LoggerFactory.getLogger(CamelSjmsTxTest.class);
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_IN = "test-in";
    private static final String QUEUE_OUT = "test-out";
    private static final int DELAY_KILL = 1900;
    private static final int NUM = 60;
    
    private BrokerService _broker;
    private ActiveMQConnectionFactory _factory;
    
    @Before
    public void before() throws Exception {
        _broker = new BrokerService();
        _broker.setPersistent(false);
        _broker.addConnector(BROKER_URL);
        _broker.start();
        _factory = new ActiveMQConnectionFactory(BROKER_URL);
        
    }
    
    @After
    public void after() throws Exception {
        _broker.stop();
        _broker = null;
    }
    
    @Test
    public void test() throws Exception {
        Connection conn = _factory.createConnection();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue(QUEUE_IN));
        for (int i=0; i<NUM; i++) {
            producer.send(session.createTextMessage(String.format("%04d", i)));
        }
        conn.close();
        
        Process proc = new ProcessBuilder("java", "-cp", System.getProperty("java.class.path"), "CamelSjmsTxTest")
                            .inheritIO()
                            .start();
        LOG.info(String.format("=====> Started camel process. Waiting %s msecs before destroy...", DELAY_KILL));
        Thread.sleep(DELAY_KILL);
        LOG.info("=====> Detroying camel process");
        proc.destroyForcibly();
        
        conn = _factory.createConnection();
        conn.start();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        List<String> msgin = new ArrayList<String>();
        MessageConsumer consumerin = session.createConsumer(session.createQueue(QUEUE_IN));
        Message msg = null;
        while ((msg = consumerin.receive(1000)) != null) {
            msgin.add(((TextMessage)msg).getText());
        }
        List<String> msgout = new ArrayList<String>();
        MessageConsumer consumerout = session.createConsumer(session.createQueue(QUEUE_OUT));
        while ((msg = consumerout.receive(1000)) != null) {
            msgout.add(((TextMessage)msg).getText());
        }
        conn.close();
        
        LOG.info("======================================================");
        LOG.info("Initial, count={}", NUM);
        LOG.info("'{}', count={}\t:{}", QUEUE_IN, msgin.size(), msgin);
        LOG.info("'{}', count={}\t:{}", QUEUE_OUT, msgout.size(), msgout);
        LOG.info("======================================================");
        Assert.assertEquals(NUM, msgin.size() + msgout.size());
    }
    
    public static void main(String args[]) throws Exception {
        RouteBuilder rb = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(Exception.class)
                    .log("Exception!!");
                    
                from(String.format("sjms:queue:%s?transacted=true&consumerCount=5", QUEUE_IN))
                    .log(String.format("=====> Forwarding a message:[${body}] from '%s' queue to '%s' queue...", QUEUE_IN, QUEUE_OUT))
                    .to(String.format("sjms:queue:%s?transacted=true", QUEUE_OUT))
                    .log(String.format("=====> Done."));
            }
        };
            
        try {
            CamelContext context = new DefaultCamelContext();
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            ConnectionFactoryResource connResource = new ConnectionFactoryResource(5, factory);
            SjmsComponent comp = new SjmsComponent();
            comp.setConnectionResource(connResource);
            context.addComponent("sjms", comp);
            context.addRoutes(rb);

            LOG.info("=====> Starting context");
            context.start();
            // Now the context will run and consume messages, when I kill application by force in any time
            // I expect this to be true: <#messagesInInputAtBeginning> == <#messagesInInputNow> + <#messagesInOutputNow>
            // What happens is that there is always < (e.g. I submitted 1000 messages, out has 500, in has 501)
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
