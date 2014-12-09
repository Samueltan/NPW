<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.io.*,java.util.Properties"%>

    <%  
        InputStream fis = new FileInputStream("D:/Program Files/Apache Software Foundation/Tomcat 8.0/webapps/myservlet/cmd.properties");
        Properties prop = new Properties();
        prop.load(fis);

        String speed = prop.getProperty("speed");
        String centerOffset = prop.getProperty("steering");
        String passedTriggerNo = prop.getProperty("speed");

        //设置输出信息的格式及字符集  
        response.setContentType("text/xml; charset=UTF-8");  
        response.setHeader("Cache-Control","no-cache");  
        out.println("<response>");  
          
        out.println("<speed>"+speed+ "</speed>");  
        out.println("<centerOffset>" +centerOffset+ "</centerOffset>");  
        out.println("<passedTriggerNo>"+passedTriggerNo+ "</passedTriggerNo>");
        out.println("</response>");  
        out.close();  
    %>   