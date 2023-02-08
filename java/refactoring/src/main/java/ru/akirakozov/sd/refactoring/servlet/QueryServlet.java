package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.Database;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final Database database;

    public QueryServlet(Database database) {
        this.database = database;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
           database.executeQueryList("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", response,"<h1>Product with max price: </h1>");
        } else if ("min".equals(command)) {
            database.executeQueryList("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", response,"<h1>Product with min price: </h1>");
        } else if ("sum".equals(command)) {
            database.executeQueryCollect("SELECT SUM(price) FROM PRODUCT", response, "Summary price: ");
        } else if ("count".equals(command)) {
            database.executeQueryCollect("SELECT COUNT(*) FROM PRODUCT", response, "Number of products: ");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
