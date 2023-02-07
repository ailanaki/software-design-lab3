package refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class GetProductsServletTest extends BaseServletTest {

    private final AddProductServlet addServlet = new AddProductServlet();
    private final GetProductsServlet getServlet = new GetProductsServlet();

    @Test
    void testEmptyGet() {
        String expected = start + end;
        assertServlet(getServlet, expected, Collections.emptyMap());
    }

    @Test
    void testOneGet() {
        String name = "candy", price = "50";
        String expected = start + createHTMLLine(name, price) + end;
        assertServlet(addServlet, addExpected, Map.of("name", name, "price", price));
        assertServlet(getServlet, expected, Collections.emptyMap());
    }
    @Test
    void testMultipleGet() {
        StringBuilder expected = new StringBuilder(start);
        int n = 100;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            String name = String.valueOf(rand.nextInt()), price = String.valueOf(rand.nextInt());
            expected.append(createHTMLLine(name, price));
            assertServlet(addServlet, addExpected, Map.of("name", name, "price", price));
        }
        expected.append(end);
        assertServlet(getServlet, expected.toString(), Collections.emptyMap());
    }
}
