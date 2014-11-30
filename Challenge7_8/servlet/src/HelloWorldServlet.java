import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by Samuel on 2014/11/26.
 */
public class HelloWorldServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><title>Hi</title></head><body>Hello world!!!!!  See hello.log.</body></html>");
        pw.flush();
        pw.close();
        bw.close();
    }
}
