package refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class GetProductsServletTest extends BaseServletTest {

    private final AddProductServlet addServlet = new AddProductServlet(database);
    private final GetProductsServlet getServlet = new GetProductsServlet(database);
    @Test
    void testEmptyGet() {
        assertServlet(getServlet, writer.writeBody(), Collections.emptyMap());
    }

    @Test
    void testOneGet() {
        String name = "candy", price = "50";
        String expected = writer.writeBody(writer.createHTMLProductLine(name, price));
        assertServlet(addServlet, writer.addExpected, Map.of("name", name, "price", price));
        assertServlet(getServlet, expected, Collections.emptyMap());
    }
    @Test
    void testMultipleGet() {
        StringBuilder expected = new StringBuilder();
        int n = 100;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            String name = String.valueOf(rand.nextInt()), price = String.valueOf(rand.nextInt());
            expected.append(writer.createHTMLProductLine(name, price));
            if(i!=n-1)expected.append(writer.endl);
            assertServlet(addServlet, writer.addExpected, Map.of("name", name, "price", price));
        }
        assertServlet(getServlet, writer.writeBody(expected.toString()), Collections.emptyMap());
    }
}
