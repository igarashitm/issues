import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringRunner.class)
@ContextConfiguration(value = "/TryCatchTest.xml", loader = CamelSpringDelegatingTestContextLoader.class)
public class TryCatchTest {
    private static final Logger LOG = LoggerFactory.getLogger(TryCatchTest.class);

    @Produce(uri = "direct:start")
    private ProducerTemplate template;

    @Test
    public void testTryCatch() throws Exception {
        template.sendBody("direct:start", null);
    }

}
