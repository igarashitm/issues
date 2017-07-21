import org.apache.camel.impl.transformer.TransformerKey;
import org.apache.camel.spi.DataType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContractTest {

    private static final Logger LOG = LoggerFactory.getLogger(ContractTest.class);

    @Test
    public void testNullType() throws Exception {
        LOG.info(new TransformerKey(null, new DataType("java:foo.bar")).toString());
        
    }
}
