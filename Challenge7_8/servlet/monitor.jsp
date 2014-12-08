<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*,java.io.*,java.util.Calendar,java.text.*,java.util.Properties"%>
<html>
<head>
    <style> 
        .div-a{ float:left} 
        .div-b{ float:left} 
    </style> 

</head>
<body>
    <%
    Connection con;
    Statement sql;
    ResultSet rs = null;
    String mode = null;
    
    try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        InputStream fis = new FileInputStream("D:/Program Files/Apache Software Foundation/Tomcat 8.0/webapps/myservlet/cmd.properties");
        Properties prop = new Properties();
        prop.load(fis);

        mode = prop.getProperty("mode");

    }catch(Exception e){
        out.print(e);
    }
    
    String s = null;
    try{
        String uri="jdbc:mysql://localhost:3306/test";
        con=DriverManager.getConnection(uri,"root","root");
        sql=con.createStatement();
        rs=sql.executeQuery("SELECT * FROM location");
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

/*        out.print("<table border=2>");
        out.print("<tr>");
        out.print("<th width=100>"+"addr");
        out.print("<th width=100>"+"time");
        out.print("<th width=100>"+"speed");
        out.print("<th width=100>"+"center offset");
        out.print("<th width=100>"+"passed trigger No#");
        out.print("</tr>");
        */
        while(rs.next()){
            java.util.Date d = new java.util.Date(Long.parseLong(rs.getString(2)));
            //s = stmp;
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            s = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + " "
                + c.get(Calendar.HOUR) + ":" + (c.get(Calendar.MINUTE) + 1) + ":" + c.get(Calendar.SECOND);
/*            out.print("<tr>");
            out.print("<td>"+rs.getString(1)+"</td>");
            out.print("<td>"+rs.getString(2)+"</td>");
            out.print("<td>"+rs.getString(3)+"</td>");
            out.print("<td>"+rs.getString(4)+"</td>");
            out.print("<td>"+rs.getString(5)+"</td>");
            out.print("</tr>");
        */
        }
        //out.print("</table>");
        con.close();
    }catch(SQLException e1){
        out.print(e1);
    }catch(Exception e){
        out.print(e);
    }

    %>
    <div class="div-a">
        <canvas id="myCanvas" width="360" height="730" style="border:1px solid #d3d3d3;">
        Your browser does not support the HTML5 canvas tag.
        </canvas>
    </div>

   <!--  <input type="button" name="btnForward" value="^" onclick="sendCommand(2)"/>
    <input type="button" name="btnBackward" value="v" onclick="sendCommand(4)"/>
    <input type="button" name="btnStop" value="||" onclick="sendCommand(3)"/>
    <input type="button" name="btnAuto" value="Auto" onclick="sendCommand(0)"/>
    <input type="button" name="btnManual" value="Manual" onclick="sendCommand(1)"/>
    <input type="button" name="btnLeft" value="<<" onclick="sendCommand(5)"/>
    <input type="button" name="btnRight" value=">>" onclick="sendCommand(6)"/>
    <input type="button" name="speedup" value="+" onclick="sendCommand(7)"/>
    <input type="button" name="speeddown" value="-" onclick="sendCommand(8)"/> -->

    <div class="div-b">
        <canvas id='canvas' width='640' height='320'> Canvas not supported</canvas>
        <center>
            <H1>
                Current Mode: <span id="mymode"><%=mode%></span>
            </H1>
        </center>
    </div>
    <script src = 'example.js'></script>

</body>
</html>