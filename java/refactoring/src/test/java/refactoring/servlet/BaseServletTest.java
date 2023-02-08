package refactoring.servlet;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import ru.akirakozov.sd.refactoring.database.Database;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseServletTest {

    Database database = new Database();

    protected final String endl = "\r\n";
    protected final String start = "<html><body>" + endl;
    protected final String end = "</body></html>" + endl;

    protected final String addExpected =  "OK"  + endl;

    @BeforeEach
    protected final void initialize() throws Exception {
      database.initialize();
    }

    @BeforeEach
    protected final void clear() throws Exception {
       database.clear();
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
