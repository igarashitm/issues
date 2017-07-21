import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class JsonPathTransformTest extends CamelTestSupport {

    @Test
    public void test() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:end");
        mock.expectedBodyReceived().body().isEqualTo("b");
        template.requestBody("direct:start", "{ \"a\":\"b\" }");
        mock.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                .log("before >>> ${body}")
                .transform().jsonpath("$.a")
                .log("after >>> ${body}")
                .to("mock:end");
            }
        };
    }

}
