package refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.util.*;

public class QueryServletTest extends BaseServletTest {
    private final AddProductServlet addServlet = new AddProductServlet(database);
    private final QueryServlet queryServlet = new QueryServlet(database);

    public void createProducts() {
        assertServlet(addServlet, writer.addExpected, Map.of("name", "candy", "price", "20"));
        assertServlet(addServlet, writer.addExpected, Map.of("name", "pumpkin", "price", "100"));
        assertServlet(addServlet, writer.addExpected, Map.of("name", "pumpkin latte", "price", "350"));
        assertServlet(addServlet, writer.addExpected, Map.of("name", "cream", "price", "50"));
        assertServlet(addServlet, writer.addExpected, Map.of("name", "router", "price", "6000"));
    }

    @Test
    void testMax() {
        createProducts();
        String expected = writer.writeBody( "<h1>Product with max price: </h1>", writer.createHTMLProductLine("router", "6000"));
        assertServlet(queryServlet, expected, Map.of("command", "max"));
    }

    @Test
    void testMin() {
        createProducts();
        String expected = writer.writeBody("<h1>Product with min price: </h1>" , writer.createHTMLProductLine("candy", "20"));
        assertServlet(queryServlet, expected, Map.of("command", "min"));
    }

    @Test
    void testSum() {
        createProducts();
        String expected = writer.writeBody("Summary price: ", "6520");
        assertServlet(queryServlet, expected, Map.of("command", "sum"));
    }

    @Test
    void testCount() {
        createProducts();
        String expected =  writer.writeBody("Number of products: " , "5");
        assertServlet(queryServlet, expected, Map.of("command", "count"));
    }

    @Test
    void testUnknown() {
        createProducts();
        String expected = "Unknown command: blablabla" + writer.endl;
        assertServlet(queryServlet, expected, Map.of("command", "blablabla"));
    }

    @Test
    void testRandom() {
        int n = 100;
        Random rand = new Random();
        HashMap<String, Integer> map = new HashMap<>();
        String name;
        Integer price;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            name = String.valueOf(rand.nextInt());
            price = rand.nextInt();
            sum += price;
            map.put(name, price);
            assertServlet(addServlet, writer.addExpected, Map.of("name", name, "price", String.valueOf(price)));
        }

        Map.Entry<String, Integer> min =map.entrySet().stream().min(Map.Entry.comparingByValue()).get();
        String expected =  writer.writeBody("<h1>Product with min price: </h1>" , writer.createHTMLProductLine(min.getKey(), min.getValue().toString()));
        assertServlet(queryServlet, expected, Map.of("command", "min"));

        Map.Entry<String, Integer> max =map.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        expected =  writer.writeBody("<h1>Product with max price: </h1>" , writer.createHTMLProductLine(max.getKey(), max.getValue().toString()));
        assertServlet(queryServlet, expected, Map.of("command", "max"));

        expected = writer.writeBody("Summary price: ", String.valueOf(sum));
        assertServlet(queryServlet, expected, Map.of("command", "sum"));

        expected = writer.writeBody("Number of products: ", String.valueOf(map.size()));
        assertServlet(queryServlet, expected, Map.of("command", "count"));

        expected = "Unknown command: more blablabla" + writer.endl;
        assertServlet(queryServlet, expected, Map.of("command", "more blablabla"));

    }
}
