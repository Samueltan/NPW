<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*,java.util.Calendar,java.text.*"%>
<html>
<head>
    <script>
    function sendCommand(command){
    var xmlHttp; 
     
    // 处理Ajax浏览器兼容
    if (window.ActiveXObject) {   
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");   
    }else if (window.XMLHttpRequest) {   
        xmlHttp = new XMLHttpRequest();   
    } 
     
    var url = "control.jsp?cmd=" + command.toString(); // 使用JS中变量tmp    
    xmlHttp.open("post",url,true);   //配置XMLHttpRequest对象
      
     // alert("*** url = " + url);
    //设置回调函数
    xmlHttp.onreadystatechange = function (){
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
           // var respText = xmlHttp.responseText;
           alert("Success! cmd = " + command);        }
    }
    xmlHttp.send(null);  // 发送请求
}
</script>
</head>
<body>
    <%
    Connection con;
    Statement sql;
    ResultSet rs = null;
    
    try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
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
    
    <canvas id="myCanvas" width="360" height="730" style="border:1px solid #d3d3d3;">
    Your browser does not support the HTML5 canvas tag.
    </canvas>

    <input type="button" name="btnForward" value="^" onclick="sendCommand(2)"/>
    <input type="button" name="btnBackward" value="v" onclick="sendCommand(4)"/>
    <input type="button" name="btnStop" value="||" onclick="sendCommand(3)"/>
    <input type="button" name="btnAuto" value="Auto" onclick="sendCommand(0)"/>
    <input type="button" name="btnManual" value="Manual" onclick="sendCommand(1)"/>
    <input type="button" name="btnLeft" value="<<" onclick="sendCommand(5)"/>
    <input type="button" name="btnRight" value=">>" onclick="sendCommand(6)"/>
    <input type="button" name="speedup" value="+" onclick="sendCommand(7)"/>
    <input type="button" name="speeddown" value="-" onclick="sendCommand(8)"/>
    

    <script>
        var c=document.getElementById("myCanvas");
        var ctx = c.getContext("2d");

        var img = new Image();
        img.onload = function(){
            ctx.drawImage(img,0,0);
        }
        img.src = 'map2.png';

        var x = 60;
        var y = 30;
        var ss = setInterval(
            function(){  
                ctx.clearRect(x-10,y-11,22,22);
                if(x>70)
                    ctx.clearRect(x-31,y-11,22,22);

                //ctx.translate(x,0);
                ctx.beginPath();
                ctx.fillStyle="blue";  
                ctx.arc(x,y,10,0,Math.PI*2,true);
                ctx.fill();
                ctx.stroke();
                ctx.closePath();
                //ctx.fillRect(x,10,100,50);
                if (x > 270  ) {
                    // clearInterval(ss);
                    ctx.clearRect(x-11,y-11,22,22);
                    x = 60;
                    y += 283;
                    if(y > 360){
                        y = 30;
                    }
                }
                // for(i=0;i<1000;++i){
                //     for(j=0;j<1000;++j){
                //         for(k=0;k<50;++k){
                //             var l=0;
                //         }
                //     }
                // }
                x += 20; 
            },
        100);

        var s = "<%=s%>"; //"" can not be ignored!
        //alert(s);  
    </script>
</body>
</html>