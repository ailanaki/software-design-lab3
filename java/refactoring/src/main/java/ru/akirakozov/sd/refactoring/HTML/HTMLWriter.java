package ru.akirakozov.sd.refactoring.HTML;

import java.io.PrintWriter;

public class HTMLWriter {
    public final String endl = "\r\n";
    public final String start = "<html><body>";
    public final String end = "</body></html>";
    public final String addExpected =  "OK"  + endl;

    private PrintWriter writer;
    public void setWriter(PrintWriter writer){
        this.writer = writer;
    }
    public void writeBody(Runnable runnable){
        writer.println(start);
        runnable.run();
        writer.println(end);
    }

    public String writeBody(String... message){
        StringBuilder builder = new StringBuilder(start);
        builder.append(endl);
        for (String str:message) {
            builder.append(str);
            builder.append(endl);
        }
        builder.append(end);
        builder.append(endl);
        return builder.toString();
    }

    public void writeMessage(String message){
        writer.println(message);
    }

    public String createHTMLProductLine(String name, String price) {
        return name + "\t" + price + "</br>";
    }

}
