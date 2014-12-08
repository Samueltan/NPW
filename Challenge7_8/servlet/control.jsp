<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*,java.util.Properties,java.io.*,java.util.Calendar,java.text.*"%>
<html>
<body>
    <%
    String strCmd = request.getParameter("cmd");
    String strValue = request.getParameter("value");
    System.out.println("************** 1 ************** strCmd = " + strCmd + ", strValue = " + strValue);
    try{       
        if(strCmd!=null) {   
            int cmdValue = Integer.parseInt(strCmd); 

            // Save the command value into file
            try {
              String value;
              InputStream fis = new FileInputStream("D:/Program Files/Apache Software Foundation/Tomcat 8.0/webapps/myservlet/cmd.properties");
              Properties prop = new Properties();
              prop.load(fis);

              switch (cmdValue){
                case -1:
                  prop.setProperty("mode", "auto");
                  prop.setProperty("speed", "0");
                  prop.setProperty("steering", "0");
                  break;
                case 0:
                  value = prop.getProperty("mode");

                  if(value.equalsIgnoreCase("auto"))
                    prop.setProperty("mode", "manual");
                  if(value.equalsIgnoreCase("manual"))
                    prop.setProperty("mode", "auto");
                  break;
                case 1:
                case 2:
                  //value = prop.getProperty("steering");
                  //float angle = Float.parseFloat(value);
                  float angle = Float.parseFloat(strValue);
                  if(cmdValue == 1) // turn left
                    prop.setProperty("steering", String.valueOf(angle));
                  if(cmdValue == 2) // turn right
                    prop.setProperty("steering", String.valueOf(angle));
                  break;
                case 3:
                case 4:                
                  //value = prop.getProperty("speed");
                  int speed = Integer.parseInt(strValue);
                  if(cmdValue == 3) // speed up
                    prop.setProperty("speed", String.valueOf(speed));
                  if(cmdValue == 4) // speed down
                    prop.setProperty("speed", String.valueOf(speed));
                  break;
                default:
                  break;
               }

              // Save the properties
              
              OutputStream fos = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 8.0/webapps/myservlet/cmd.properties");
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