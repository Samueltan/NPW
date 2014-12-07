var canvas = document.getElementById('canvas'),
    context = canvas.getContext('2d'),
    CENTROID_RADIUS = 10,
    CENTROID_STROKE_STYLE = 'rgba(0, 0, 0, 0.5)',
    CENTROID_FILL_STYLE   = 'rgba(80, 190, 240, 0.6)',
    RING_INNER_RADIUS = 35,
    RING_OUTER_RADIUS = 55,
    ANNOTATIONS_FILL_STYLE = 'rgba(0, 0, 230, 0.9)',
    ANNOTATIONS_TEXT_SIZE = 12,
    TICK_WIDTH = 10,
    TICK_LONG_STROKE_STYLE = 'rgba(100, 140, 230, 0.9)',
    TICK_SHORT_STROKE_STYLE = 'rgba(100, 140, 230, 0.7)',
    TRACKING_DIAL_STROKING_STYLE = 'rgba(100, 140, 230, 0.5)',
    GUIDEWIRE_STROKE_STYLE = 'goldenrod',
    GUIDEWIRE_FILL_STYLE = 'rgba(250, 250, 0, 0.6)',
    circle1 = { x: canvas.width/4,
               y: canvas.height/2,
               radius: 60
             };
    circle2 = { x: canvas.width * 3/4,
               y: canvas.height/2,
               radius: 60
             };
    dashboardAngle = 90;
// Functions..........................................................
function drawGrid(circle, color, stepx, stepy) {
   context.save()
   context.shadowColor = undefined;
   context.shadowOffsetX = 0;
   context.shadowOffsetY = 0;
   
   if(context.strokeStyle == '#000000'){
      context.strokeStyle = color;    
   }
   context.fillStyle = '#ffffff';
   context.lineWidth = 0.5;
   // alert(circle.x + ', ' + circle.x);
   // context.fillRect(circle.x - context.canvas.width/4, 0,
   //                  context.canvas.width/2, context.canvas.height);
   context.fillRect(0, 0, context.canvas.width, context.canvas.height);
   for (var i = stepx + 0.5;
            i < context.canvas.width; i += stepx) {
     context.beginPath();
     context.moveTo(i, 0);
     context.lineTo(i, context.canvas.height);
     context.stroke();
   }
   for (var i = stepy + 0.5;
            i < context.canvas.height; i += stepy) {
     context.beginPath();
     context.moveTo(0, i);
     context.lineTo(context.canvas.width, i);
     context.stroke();
   }
   context.restore();
}
function drawCentroid(circle) {
   context.beginPath();
   context.save();
   context.strokeStyle = CENTROID_STROKE_STYLE;
   context.fillStyle = CENTROID_FILL_STYLE;
   context.arc(circle.x, circle.y,
               CENTROID_RADIUS, 0, Math.PI*2, false);
   context.stroke();
   context.fill();
   context.restore();
}
function drawCentroidGuidewire(circle, loc, ang) {
   var angle = -(Math.PI/180)*ang,
       radius, endpt;
  radius = circle.radius + RING_OUTER_RADIUS;
  if (loc.x >= circle.x) {
      endpt = { x: circle.x + radius * Math.cos(angle),
                y: circle.y + radius * Math.sin(angle)
      };
   }
   else {
      endpt = { x: circle.x - radius * Math.cos(angle),
                y: circle.y - radius * Math.sin(angle)
      };
   }
   
   context.save();
   context.strokeStyle = GUIDEWIRE_STROKE_STYLE;
   context.fillStyle = GUIDEWIRE_FILL_STYLE;
   context.beginPath();
   context.moveTo(circle.x, circle.y);
   context.lineTo(endpt.x, endpt.y);
   context.stroke();
   context.beginPath();
   context.strokeStyle = TICK_LONG_STROKE_STYLE;
   context.arc(endpt.x, endpt.y, 5, 0, Math.PI*2, false);
   context.fill();
   context.stroke();
   context.restore();
}
function drawRing(circle) {
   drawRingOuterCircle(circle);
   context.strokeStyle = 'rgba(0, 0, 0, 0.1)';
   context.arc(circle.x, circle.y,
               circle.radius + RING_INNER_RADIUS,
               0, Math.PI*2, false);
   context.fillStyle = 'rgba(100, 140, 230, 0.1)';
   context.fill();
   context.stroke();
}
function drawRingOuterCircle(circle) {
   context.shadowColor = 'rgba(0, 0, 0, 0.7)';
   context.shadowOffsetX = 3,
   context.shadowOffsetY = 3,
   context.shadowBlur = 6,
   context.strokeStyle = TRACKING_DIAL_STROKING_STYLE;
   context.beginPath();
   context.arc(circle.x, circle.y, circle.radius +
               RING_OUTER_RADIUS, 0, Math.PI*2, true);
   context.stroke();
}
function drawTickInnerCircle(circle) {
   context.save();
   context.beginPath();
   context.strokeStyle = 'rgba(0, 0, 0, 0.1)';
   context.arc(circle.x, circle.y,
               circle.radius + RING_INNER_RADIUS - TICK_WIDTH,
               0, Math.PI*2, false);
   context.stroke();
   context.restore();
}
   
function drawTick(circle, angle, radius, cnt) {
   var tickWidth = cnt % 4 === 0 ? TICK_WIDTH : TICK_WIDTH/2;
   
   context.beginPath();
   context.moveTo(circle.x + Math.cos(angle) * (radius - tickWidth),
                  circle.y + Math.sin(angle) * (radius - tickWidth));
   context.lineTo(circle.x + Math.cos(angle) * (radius),
                  circle.y + Math.sin(angle) * (radius));
   context.strokeStyle = TICK_SHORT_STROKE_STYLE;
   context.stroke();
}
function drawTicks(circle) {
   var radius = circle.radius + RING_INNER_RADIUS,
       ANGLE_MAX = 2*Math.PI,
       ANGLE_DELTA = Math.PI/64,
       tickWidth;
   context.save();
   
   for (var angle = 0, cnt = 0; angle < ANGLE_MAX;
                                angle += ANGLE_DELTA, cnt++) {
      drawTick(circle, angle, radius, cnt++); 
   }
   context.restore();
}
function drawAnnotations(circle, caption, start, step) {
   var radius = circle.radius + RING_INNER_RADIUS;
   context.save();
   context.fillStyle = ANNOTATIONS_FILL_STYLE;
   context.font = ANNOTATIONS_TEXT_SIZE + 'px Helvetica'; 
   
   for (var angle=0, i=start; angle <= Math.PI; angle += Math.PI/8, i=i-step) {
      context.beginPath();
      context.fillText(i,
         circle.x + Math.cos(angle) * (radius - TICK_WIDTH*2),
         circle.y - Math.sin(angle) * (radius - TICK_WIDTH*2));
      // context.fillText((angle * 180 / Math.PI).toFixed(0),
      //    circle.x + Math.cos(angle) * (radius - TICK_WIDTH*2),
      //    circle.y - Math.sin(angle) * (radius - TICK_WIDTH*2));
   }
   context.fillText(caption, circle.x, circle.y + 20);
   context.restore();
}
function keyUp(){
  var code = event.keyCode;
  var c=document.getElementById("canvas");
  var ctx=c.getContext("2d");

  switch (code){
    case 38:
      // alert("Up pressed!");
      if(dashboardAngle > 0)
        dashboardAngle -= 22.5;
      drawDial(circle1, dashboardAngle, 'Speed');
      break;
    case 40:
      // alert("Down pressed!");
      if(dashboardAngle < 180)
        dashboardAngle += 22.5;
      drawDial(circle1, dashboardAngle, 'Speed');
      break;
    case 37:
      // alert("Left pressed!");
      if(dashboardAngle < 180)
        dashboardAngle += 22.5;
      drawDial(circle2, dashboardAngle, 'Steering Wheel');
      break;
    case 39:
      // alert("Right pressed!");
      if(dashboardAngle > 0)
        dashboardAngle -= 22.5;
      drawDial(circle2, dashboardAngle, 'Steering Wheel');
      break;
    default:
      break;
  }
} 

function drawDial(circle, angle, caption) {
   var loc = {x: circle.x, y: circle.y};
   // Speed dail
   if(circle.x == 160){
      context.clearRect(0,0,320,320);
      drawAnnotations(circle, caption, 4, 1);
    // Steering wheel dail
   }else{
      context.clearRect(320,0,320,320);
      drawAnnotations(circle, caption, 90, 22.5);
    }

   // drawGrid(circle, 'lightgray', 10, 10);

   // alert(circle.x);
   drawCentroid(circle);
   drawCentroidGuidewire(circle, loc, angle);
   drawRing(circle);
   drawTickInnerCircle(circle);
   drawTicks(circle);

}

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
    
// Initialization....................................................
context.shadowOffsetX = 2;
context.shadowOffsetY = 2;
context.shadowBlur = 4;
context.textAlign = 'center';
context.textBaseline = 'middle';

drawDial(circle1, dashboardAngle, 'Speed');
drawDial(circle2, dashboardAngle, 'Steering Wheel');
document.onkeyup=keyUp;

// var loc1 = {x: circle1.x, y: circle1.y};
// var loc2 = {x: circle2.x, y: circle2.y};
//    // if(circle.x == 160)
//    //    context.clearRect(0,0,320,320);
//    // else
//    //    context.clearRect(320,0,320,320);

//    drawGrid('lightgray', 10, 10);

//    drawCentroid(circle1);
//    drawCentroidGuidewire(circle1, loc1, dashboardAngle);
//    drawRing(circle1);
//    drawTickInnerCircle(circle1);
//    drawTicks(circle1);
//    drawAnnotations(circle1, 'speed');

//    drawCentroid(circle2);
//    drawCentroidGuidewire(circle2, loc2, dashboardAngle);
//    drawRing(circle2);
//    drawTickInnerCircle(circle2);
//    drawTicks(circle2);
//    drawAnnotations(circle2, 'steering wheel');

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