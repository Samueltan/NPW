import java.util.Calendar;
import java.util.Date;

/**
 * Created by Samuel on 2014/12/3.
 */
public class Sample implements Runnable{
    public static void main(String[] args) {
        Date d = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        String s = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE)
                + " " + c.get(Calendar.HOUR) + ":" + (c.get(Calendar.MINUTE)) + ":" + c.get(Calendar.SECOND);
        System.out.println(s);
    }


    @Override
    public void run() {

    }
}
