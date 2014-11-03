import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import static dao.generated.tables.Location.LOCATION;

/**
 * Created by Samuel on 2014/11/3.
 */
public class DBUtil {
    Connection conn = null;
    public DBUtil() {
        try{
            // For Sqlite
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:challenges.db");
        } catch (Exception e) {
            // For the sake of this tutorial, let's keep exception handling simple
            e.printStackTrace();
            closeConn();
        }
    }

    public Connection getConn(){
        return conn;
    }

    public void saveDistances(String address, String timestamp, float r1, float r2, float r3){
        try {

            DSLContext create = DSL.using(conn, SQLDialect.SQLITE);

            create.insertInto(LOCATION, LOCATION.ADDR, LOCATION.TIME, LOCATION.R1, LOCATION.R2, LOCATION.R3)
                    .values(address, timestamp, r1, r2, r3).execute();

        } catch (Exception e) {
            // For the sake of this tutorial, let's keep exception handling simple
            e.printStackTrace();
            closeConn();
        }
    }

    public TripleR getSensorLocation(String address, long time){
        Result<Record> result = null;
        TripleR tr = null;
        try {
            // For Sqlite
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:challenges.db");
            DSLContext create = DSL.using(conn, SQLDialect.SQLITE);
            result = create.select().from(LOCATION)
                    .where(LOCATION.ADDR.equal(address)
                            .and(LOCATION.TIME.equal(Long.toString(time))))
                    .fetch();
//            System.out.println("Read from db:");

            float r1 = result.get(0).getValue(LOCATION.R1);
            float r2 = result.get(0).getValue(LOCATION.R1);
            float r3 = result.get(0).getValue(LOCATION.R1);
            tr = new TripleR(r1, r2, r3);
        }catch(Exception e){
            e.printStackTrace();
            closeConn();
        }
        return tr;
    }

    public void closeConn(){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignore) {
                ignore.printStackTrace();
            }
        }
    }

    public class TripleR{
        float r1, r2, r3;

        public TripleR(float r1, float r2, float r3) {
            this.r1 = r1;
            this.r2 = r2;
            this.r3 = r3;
        }

        public float getR1() {
            return r1;
        }

        public void setR1(float r1) {
            this.r1 = r1;
        }

        public float getR2() {
            return r2;
        }

        public void setR2(float r2) {
            this.r2 = r2;
        }

        public float getR3() {
            return r3;
        }

        public void setR3(float r3) {
            this.r3 = r3;
        }
    }

    public static void main(String[] args){
        long time = System.currentTimeMillis();
//        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String strTime = fmt.format(new Date(time)).toString();

        DBUtil dbutil = new DBUtil();
        for(int i=0;i<10;++i) {
            dbutil.saveDistances("b544", Long.toString(time + i), 500, 500-i, 500+i);
        }

//        TripleR tr = dbutil.getSensorLocation("b544", 1415051389172L);

        dbutil.closeConn();
    }
}
