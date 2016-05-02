package org.apache.camel.component.dozer;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

import java.io.File;

import javax.inject.Inject;
import javax.naming.spi.ObjectFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.dozer.example.CustomMapperOperations;
import org.apache.camel.component.dozer.example.abc.ABCOrder;
import org.apache.camel.component.dozer.example.abc.ABCOrder.Header;
import org.apache.camel.component.dozer.example.xyz.LineItem;
import org.apache.camel.component.dozer.example.xyz.XYZOrder;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Constants;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class PaxExamTest {

    private static final Logger logger = Logger.getLogger(PaxExamTest.class);
    
    @Inject
    CamelContext camelContext;
    
    @Configuration
    public Option[] config() throws Throwable {
        try {
            return new Option[] {
                    karafDistributionConfiguration().unpackDirectory(new File("target/paxexam/unpack")).frameworkUrl(
                            maven().groupId("org.apache.karaf").artifactId("apache-karaf").type("zip").versionAsInProject()),
                    keepRuntimeFolder(),
                    features(maven().groupId("org.apache.camel.karaf").artifactId("apache-camel").type("xml")
                            .classifier("features").versionAsInProject(), "camel-blueprint", "camel-dozer", "camel-jaxb", "camel-jackson"),
                    streamBundle(bundle().add(CustomMapper.class)
                            .add(CustomMapperOperations.class)
                            .add(ABCOrder.class)
                            .add(ObjectFactory.class)
                            .add(LineItem.class)
                            .add(XYZOrder.class)
                            .add("OSGI-INF/blueprint/camel-context.xml"
                                    , new File("src/main/resources/OSGI-INF/blueprint/camel-context.xml").toURL())
                            .add("variableMapping.xml"
                                    , new File("src/main/resources/variableMapping.xml").toURL())
                            .set(Constants.BUNDLE_SYMBOLICNAME, "org.apache.camel.component.dozer.example.camel9921")
                            .set(Constants.BUNDLE_MANIFESTVERSION, "2")
                            .set(Constants.DYNAMICIMPORT_PACKAGE, "*")
                            .set(Constants.EXPORT_PACKAGE,
                                    "org.apache.camel.component.dozer.example, "
                                    + "org.apache.camel.component.dozer.example.abc, "
                                    + "org.apache.camel.component.dozer.example.xyz")
                            .build()).start()
                    //features(maven().groupId("com.github.igarashitm.issues").artifactId("CAMEL-9921")
                    //        .type("xml").classifier("features").versionAsInProject(), "CAMEL-9921")
            };
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }
    
    @Test
    public void test() {
        Assert.assertNotNull(camelContext);
        ABCOrder abcOrder = new ABCOrder();
        abcOrder.setHeader(new Header());
        abcOrder.getHeader().setStatus("GOLD");
        ProducerTemplate template = camelContext.createProducerTemplate();
        Object answer = template.requestBody("direct:start", abcOrder);
        // check results
        Assert.assertEquals(XYZOrder.class.getName(), answer.getClass().getName());
        XYZOrder xyz = (XYZOrder)answer;
        logger.info(String.format("Received XYZOrder:[priority=%s, custID=%s, orderID=%s]", xyz.getPriority(), xyz.getCustId(), xyz.getOrderId()));
        Assert.assertEquals(xyz.getPriority(), "GOLD");
        Assert.assertEquals("ACME-SALES", xyz.getCustId());
        Assert.assertEquals("W123-EG", xyz.getOrderId());
    }
}
