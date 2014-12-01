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
    java.sql.Connection conn = null;
    DSLContext create = null;
    static int requestCount = 0;

    @Override
    public void init() throws ServletException {
        super.init();

        // Database initialization
        // For Mysql
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        create = DSL.using(conn, SQLDialect.MYSQL);

    }

    @Override
    public void destroy() {
        super.destroy();

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ++requestCount;
        System.out.println("Entering doPost()..." + requestCount);

        FileWriter fw = new FileWriter("C:/temp/hello.log", true);
        BufferedWriter bw = new BufferedWriter(fw);

        String tmp = new Date() + " - \t\t";
        InputStream is = req.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String s;
        while((s = br.readLine()) != null){
            tmp +=  " " + s;
            System.out.println(s.toUpperCase());
        }
        br.close();

//        tmp += "\n\t*** from Header: light = " + req.getHeader("light") + ", temperature = " + req.getHeader("temperature");

        bw.write(tmp);
        bw.newLine();
        bw.flush();

//        int distanceR = (int) (1473 + Math.random() * 1000);
//        int centerX = (int) (296 + Math.random() * 100);

        String address = req.getHeader("address");
        int distanceR = Integer.parseInt(req.getHeader("distanceR"));
        int centerX = (int)Float.parseFloat (req.getHeader("centerX"));

        saveToDB(address, Long.toString(System.currentTimeMillis()), distanceR, centerX);

        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><title>Hi</title></head><body>Hello world!!!!!  See hello.log.</body></html>");
        pw.flush();
        pw.close();
        bw.close();

        System.out.println("Exiting doPost()...");
    }

    public void saveToDB(String address, String timestamp, int dr, int cx){
//        System.out.println("Entering saveToDB()...");

        try {
            create.insertInto(Location.LOCATION, Location.LOCATION.ADDR, Location.LOCATION.TIME,
                    Location.LOCATION.DISTANCER, Location.LOCATION.CENTERX)
                    .values(address, timestamp, dr, cx).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Exiting saveToDB()...");
    }
}
