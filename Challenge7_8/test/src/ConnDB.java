import java.sql.DriverManager;
import java.sql.SQLException;

import dao.generated.tables.Location;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * Created by Samuel on 2014/11/29.
 */
public class ConnDB {
    public static void saveToDB(String address, String timestamp, int dr, int cx){
        System.out.println("Entering saveToDB()...");

        java.sql.Connection conn = null;
        try {
            // For Sqlite
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

            System.out.println("Dummy db operation **********  1  ********** ...");

            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

            System.out.println("Dummy db operation **********  2  ********** ...");

            create.insertInto(Location.LOCATION, Location.LOCATION.ADDR, Location.LOCATION.TIME,
                    Location.LOCATION.DISTANCER, Location.LOCATION.CENTERX)
                    .values("FFFF", Long.toString(System.currentTimeMillis()), 132, 347).execute();

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
