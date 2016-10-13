import java.io.File;
import java.util.Arrays;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveUrlTest extends CamelTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(SaveUrlTest.class);
    private static final String OUTPUT_DIR = "output";

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                .to("http://camel.apache.org/index.html")
                .to("file:" + OUTPUT_DIR);
            }
        };
    }

    @Before
    public void before() throws Exception {
        for (File f : new File(OUTPUT_DIR).listFiles()) {
            LOG.info("Deleting {}", f.getAbsolutePath());
            f.delete();
        };
    }

    @Test
    public void testSaveUrl() throws Exception {
        template.sendBody("direct:start", null);
        File dir = new File(OUTPUT_DIR);
        Assert.assertTrue(Arrays.toString(dir.list()), dir.list().length > 0);
    }
}
