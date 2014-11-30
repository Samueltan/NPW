import dao.generated.tables.Location;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
//import java.util.Enumeration;

/**
 * Created by Samuel on 2014/11/26.
 */
public class HelloWorldServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Entering doPost()...");

        FileWriter fw = new FileWriter("C:/temp/hello.log", true);
        BufferedWriter bw = new BufferedWriter(fw);

        String tmp = new Date() + " - \t" + req.getRequestURI();
        InputStream is = req.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String s;
        tmp += "\n\t*** from request inputStream: ";
        while((s = br.readLine()) != null){
            tmp +=  " " + s;
            System.out.println(s.toUpperCase());
        }
        br.close();

        tmp += "\n\t*** from Header: light = " + req.getHeader("light") + ", temperature = " + req.getHeader("temperature");

        bw.write(tmp);
        bw.newLine();
        bw.flush();

        saveToDB("FFFF", Long.toString(System.currentTimeMillis()), 113, 267);

        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><title>Hi</title></head><body>Hello world!!!!!  See hello.log.</body></html>");
        pw.flush();
        pw.close();
        bw.close();

        System.out.println("Exiting doPost()...");
    }

    public void saveToDB(String address, String timestamp, int dr, int cx){
        System.out.println("Entering saveToDB()...");

        java.sql.Connection conn = null;
        try {
            // For Sqlite
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

            System.out.println("Dummy db operation **********  1  ********** ...");

            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

            System.out.println("Dummy db operation **********  2  ********** ...");

//            create.insertInto(Location.LOCATION, Location.LOCATION.ADDR, Location.LOCATION.TIME,
//                    Location.LOCATION.DISTANCER, Location.LOCATION.CENTERX)
//                    .values(address, timestamp, dr, cx).execute();

        } catch (Exception e) {
            // For the sake of this tutorial, let's keep exception handling simple
            System.out.println("Dummy db operation **********  3  ********** ...");

            System.out.println(e.getMessage());

            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }
        System.out.println("Exiting saveToDB()...");
    }
}
