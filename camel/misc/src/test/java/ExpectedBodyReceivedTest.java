import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ExpectedBodyReceivedTest extends CamelTestSupport {

    @Test
    public void testBodyIsEqualTo() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:end");
        
        mock.expectedMessageCount(1);
        mock.expectedBodyReceived().body().isEqualTo("wrong input");
        
        template.requestBody("direct:start", "actual input");
        
        mock.assertIsNotSatisfied();
    }
    
    @Test
    public void testConstant() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:end");
        
        mock.expectedMessageCount(1);
        mock.expectedBodyReceived().constant("wrong input");
        
        template.requestBody("direct:start", "actual input");
        
        mock.assertIsNotSatisfied();
    }
    
    @Test
    public void testExpectedBodies() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:end");
        
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("wrong input");
        
        template.requestBody("direct:start", "actual input");
        
        mock.assertIsNotSatisfied();
    }
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("mock:end");
            }
        };
    }
}
