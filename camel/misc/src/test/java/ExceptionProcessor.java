import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ExceptionProcessor implements Processor {
    public void process(Exchange ex) throws Exception {
        throw new Exception("throwing");
    }
}
