package refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class QueryServletTest extends BaseServletTest {
    private final AddProductServlet addServlet = new AddProductServlet(database);
    private final QueryServlet queryServlet = new QueryServlet(database);

    public void createProducts() {
        assertServlet(addServlet, addExpected, Map.of("name", "candy", "price", "20"));
        assertServlet(addServlet, addExpected, Map.of("name", "pumpkin", "price", "100"));
        assertServlet(addServlet, addExpected, Map.of("name", "pumpkin latte", "price", "350"));
        assertServlet(addServlet, addExpected, Map.of("name", "cream", "price", "50"));
        assertServlet(addServlet, addExpected, Map.of("name", "router", "price", "6000"));
    }

    @Test
    void testMax() {
        createProducts();
        String expected = start + "<h1>Product with max price: </h1>" + endl
                + createHTMLLine("router", "6000") + end;
        assertServlet(queryServlet, expected, Map.of("command", "max"));
    }

    @Test
    void testMin() {
        createProducts();
        String expected = start + "<h1>Product with min price: </h1>" + endl
                + createHTMLLine("candy", "20") + end;
        assertServlet(queryServlet, expected, Map.of("command", "min"));
    }

    @Test
    void testSum() {
        createProducts();
        String expected = start + "Summary price: " + endl
                + "6520" + endl + end;
        assertServlet(queryServlet, expected, Map.of("command", "sum"));
    }

    @Test
    void testCount() {
        createProducts();
        String expected = start + "Number of products: " + endl
                + "5" + endl + end;
        assertServlet(queryServlet, expected, Map.of("command", "count"));
    }

    @Test
    void testUnknown() {
        createProducts();
        String expected = "Unknown command: blablabla" + endl;
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
            assertServlet(addServlet, addExpected, Map.of("name", name, "price", String.valueOf(price)));
        }

        Map.Entry<String, Integer> min =map.entrySet().stream().min(Map.Entry.comparingByValue()).get();
        String expected = start + "<h1>Product with min price: </h1>" + endl +createHTMLLine(min.getKey(), min.getValue().toString()) + end;
        assertServlet(queryServlet, expected, Map.of("command", "min"));

        Map.Entry<String, Integer> max =map.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        expected = start + "<h1>Product with max price: </h1>" + endl +createHTMLLine(max.getKey(), max.getValue().toString()) + end;
        assertServlet(queryServlet, expected, Map.of("command", "max"));

        expected = start + "Summary price: " + endl + sum + endl + end;
        assertServlet(queryServlet, expected, Map.of("command", "sum"));

        expected = start + "Number of products: " + endl + map.size() + endl + end;
        assertServlet(queryServlet, expected, Map.of("command", "count"));

        expected = "Unknown command: more blablabla" + endl;
        assertServlet(queryServlet, expected, Map.of("command", "more blablabla"));

    }
}
