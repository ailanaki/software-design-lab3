package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.HTML.HTMLWriter;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Consumer;

public class Database {
    private final HTMLWriter writer = new HTMLWriter();

    public final void initialize() {
        executeSQL("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    public final void clear() {
        executeSQL("DELETE FROM PRODUCT WHERE 1 = 1");
    }

    public void executeSQL(String sql) {
        executeCommand(withError((Statement stmt) -> stmt.executeUpdate(sql)));
    }

    public void executeQueryList(String sql, HttpServletResponse response, String... message) {
        executeQuery(sql, response, withError((ResultSet rs) -> {
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
        }), message);
    }

    public void executeQueryCollect(String sql, HttpServletResponse response, String... message) {
        executeQuery(sql, response, withError((ResultSet rs) -> {
            if (rs.next()) {
                response.getWriter().println(rs.getInt(1));
            }
        }), message);
    }

    private void executeQuery(String sql, HttpServletResponse response, Consumer<ResultSet> get, String... message) {
        executeCommand(withError((Statement stmt) -> {
            ResultSet rs = stmt.executeQuery(sql);
            writer.setWriter(response.getWriter());
            writer.writeBody(() -> {
                if (message.length > 0) writer.writeMessage(message[0]);
                get.accept(rs);
            });
            rs.close();
        }));
    }

    private void executeCommand(Consumer<Statement> consumer) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            consumer.accept(stmt);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private interface ConsumerWithError<T> {
        void accept(T val) throws Exception;
    }

    public static <T> Consumer<T> withError(ConsumerWithError<T> cons) {
        return (val) -> {
            try {
                cons.accept(val);
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception", e);
            }
        };
    }

}
