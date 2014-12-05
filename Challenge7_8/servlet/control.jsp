<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*,java.util.Properties,java.io.*,java.util.Calendar,java.text.*"%>
<html>
<body>
    <%
    String strCmd = request.getParameter("cmd");
    // String strCmd = "test *** cmd ***";
    System.out.println("************** 1 **************" + strCmd);
    try{       
        if(strCmd!=null) {   
            //cmdValue = Integer.parseInt(strCmd); 

            // Save the command value into file
            try {
              // InputStream fis = new FileInputStream("/Users/samueltango/Downloads/Dev/apache-tomcat-8.0.8/webapps/ec544/cmd.properties");
              Properties prop = new Properties();
              // prop.load(fis);
              OutputStream fos = new FileOutputStream(new File("D:/Program Files/Apache Software Foundation/Tomcat 8.0/webapps/myservlet/cmd.properties"));
              prop.setProperty("cmd", strCmd);
              prop.store(fos, "save command value");
            } catch (IOException e) {
              System.err.println("IO error occurred.");
            }

        }
    }catch(Exception e){
        out.print(e);
    }

    %>
    <input type="text" name="test" value="<%=strCmd%>">
</body>
</html>