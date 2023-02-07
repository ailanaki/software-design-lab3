package refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseServletTest {

    protected final String endl = "\r\n";
    protected final String start = "<html><body>" + endl;
    protected final String end = "</body></html>" + endl;

    protected final String addExpected =  "OK"  + endl;

    @BeforeEach
    protected final void initialize() throws Exception {
        executeSQL("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @BeforeEach
    protected final void clear() throws Exception {
        executeSQL("DELETE FROM PRODUCT WHERE 1 = 1");
    }

    private void executeSQL(String sql) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    protected String createHTMLLine(String name, String price) {
        return name + "\t" + price + "</br>" + endl;
    }

    protected void assertServlet(HttpServlet servlet, String expected, Map<String, String> parameters) {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();

        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            parameters.forEach((key, value) ->
                    when(request.getParameter(key)).thenReturn(value)
            );

            when(request.getMethod()).thenReturn("GET");
            when(response.getWriter()).thenReturn(printWriter);

            servlet.service(request, response);
            //System.out.print(stringWriter);
            assertThat(stringWriter).hasToString(expected);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
