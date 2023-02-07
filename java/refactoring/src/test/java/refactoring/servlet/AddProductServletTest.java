package refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;

import java.util.Map;
import java.util.Random;

public class AddProductServletTest extends BaseServletTest{
    private final AddProductServlet servlet = new AddProductServlet();

    @Test
    void testAdd() {
        assertServlet(servlet, addExpected, Map.of("name", "candy", "price", "50"));
    }

    @Test
    void testAddMultiple() {
        int n = 100;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            assertServlet(servlet, addExpected, Map.of("name", String.valueOf(rand.nextInt()), "price", String.valueOf(rand.nextInt())));
        }
    }
}
