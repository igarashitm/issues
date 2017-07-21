import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.util.CaseInsensitiveMap;
import org.junit.Assert;
import org.junit.Test;

public class MiscTest {

    @Test
    public void testConcurrentHashMap() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        fill(map);
        System.out.println("ConcurrentHashMap: " + map.toString());
    }

    @Test
    public void testCaseInsensitiveMap() {
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        fill(map);
        System.out.println("CaseInsensitiveMap: " + map.toString());
    }

    @Test
    public void testNullBoolean() {
        Boolean b = null;
        boolean b2 = b;
        if (b2) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }
    private void fill(Map map) {
        for (int i=0; i<10; i++) {
            map.put("key" + i, "value" + i);
        }
    }
}
