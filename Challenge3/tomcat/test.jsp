<%@   page   contentType="text/html;   charset=GB2312"   %>   
  <title>EC544 Challenge 3 group 2</title>
  <form   name="form1"   action="test.jsp"   method="post">  
  <br><br>   
 Input the light command: <br><br>
  <input   type="text"   name="cmd"><br><br>
  <input   type="submit"   name="Submit"   value="Send Command">   
  </form>   
  <%@ page import="java.io.*,java.util.Properties" %>
  <%   
  int   cmdValue;   
  if(request.getParameter("cmd")!=null)   
  {   
    String strCmd = request.getParameter("cmd");
    if (strCmd.equals("")){
      out.print("Please input a valid command value: 0 for off, 1 for on.");
    }else{
      try{
        cmdValue   =   Integer.parseInt(strCmd);   
        if(cmdValue == 0 || cmdValue == 1){
          if(cmdValue == 0)
            out.print("<br>Command sent was: 0 (Turn off the light)"); 
          else if(cmdValue == 1)  
            out.print("<br>Command sent was: 1 (Turn on the light)"); 
            // Save the command value into file
            try {
              // InputStream fis = new FileInputStream("/Users/samueltango/Downloads/Dev/apache-tomcat-8.0.8/webapps/ec544/cmd.properties");
              Properties prop = new Properties();
              // prop.load(fis);
              OutputStream fos = new FileOutputStream(new File("/Users/samueltango/Downloads/Dev/apache-tomcat-8.0.8/webapps/ec544/cmd.properties"));
              prop.setProperty("cmd", strCmd);
              prop.store(fos, "save command value");
            } catch (IOException e) {
              System.err.println("IO error occurred.");
            }

        }else{
          out.print("Invalid command value! Please input a valid command value: 0 for off, 1 for on.");
        }
      }catch(Exception e){
        out.print("Invalid command value! Please input a valid command value: 0 for off, 1 for on.");
      }
    }
  }else{   
     
  }   
  %> 