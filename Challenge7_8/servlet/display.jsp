<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*"%>
<html>
<body>
<%
Connection con;
Statement sql;
ResultSet rs;
try{Class.forName("com.mysql.jdbc.Driver").newInstance();}
catch(Exception e){out.print(e);}
try{
    String uri="jdbc:mysql://localhost:3306/test";
    con=DriverManager.getConnection(uri,"root","root");
    sql=con.createStatement();
    rs=sql.executeQuery("SELECT * FROM location");
    out.print("<table border=2>");
    out.print("<tr>");
    out.print("<th width=100>"+"addr");
    out.print("<th width=100>"+"time");
    out.print("<th width=100>"+"distanceR");
    out.print("<th width=100>"+"centerX");
    out.print("</tr>");
    while(rs.next()){
        out.print("<tr>");
        out.print("<td>"+rs.getString(1)+"</td>");
        out.print("<td>"+rs.getString(2)+"</td>");
        out.print("<td>"+rs.getString(3)+"</td>");
        out.print("<td>"+rs.getString(4)+"</td>");
        out.print("</tr>");
    }
    out.print("</table>");
    con.close();
}
catch(SQLException e1){out.print(e1);}
%>
    <canvas id="myCanvas" width="1024" height="768" style="border:1px solid #d3d3d3;">
    Your browser does not support the HTML5 canvas tag.
    </canvas>

    <script>
        var c=document.getElementById("myCanvas");
        var ctx = c.getContext("2d");

        var img = new Image();
        img.onload = function(){
            ctx.drawImage(img,0,0);
        }
        img.src = 'Penguins.jpg';

        var x = 0;
        var y = 20;
        var ss = setInterval(
            function(){  
                ctx.clearRect(x-11,y-11,22,22);
                ctx.clearRect(x-31,y-11,22,22);

                //ctx.translate(x,0);
                ctx.beginPath();
                ctx.fillStyle="blue";  
                ctx.arc(x,y,10,0,Math.PI*2,true);
                ctx.fill();
                ctx.stroke();
                ctx.closePath();
                //ctx.fillRect(x,10,100,50);
                if (x > 1030  ) {
                    // clearInterval(ss);
                    x = 0;
                    y += 100;
                    if(y > 774){
                        y = 20;
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
    </script>
</body>
</html>