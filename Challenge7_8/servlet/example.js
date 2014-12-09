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
  var speedStart = 4;
  var speedStep = 1;
  var wheelStart = 90;
  var wheelStep = 22.5;
  // var manualFlg = false;

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
   
   for (var i=start; i>=start-8*step; i=i-step) {
      var angle = (Math.PI/8) * ((start - i)/step);
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

  var mm = document.getElementById("mymode");
  switch (code){
    case 27:
      if(manualFlg){
        // alert("Escape pressed!");
        sendCommand(-1, 0);
        mm.innerHTML = 'auto';
        drawDial(circle1, 90, 'Speed');
        drawDial(circle2, 90, 'Steering Wheel');
        manualFlg = false;
        speed = 0;
      }
      break;
    case 13:
      // alert("Enter pressed!");
      manualFlg = !manualFlg;
      if(manualFlg)
        mm.innerHTML = 'manual';
      else
        mm.innerHTML = 'auto';

      sendCommand(0, 0);
      break;
    case 32:
      // alert("Space pressed!");
      // alert(manualFlg);
      if(manualFlg){
        var speedTmp = speed;
        if(speed == 0){
          speed = 2;
        }else{
          speed = 0;
        }
        dashboardAngle = (speedStart - speed) * 45 / 2;
        drawDial(circle1, dashboardAngle, 'Speed');
        sendCommand(4, speed);
      }
      break;
    case 38:
      if(manualFlg){
        // alert("Up pressed!");
        if(dashboardAngle > 0){
          dashboardAngle -= 22.5;
          drawDial(circle1, dashboardAngle, 'Speed');
          speed = speedStart - 2*dashboardAngle / 45
          sendCommand(3, speed);
        }
      }
      break;
    case 40:
      if(manualFlg){
        // alert("Down pressed!");
        if(dashboardAngle < 180){
          dashboardAngle += 22.5;
          drawDial(circle1, dashboardAngle, 'Speed');
          speed = speedStart - 2*dashboardAngle / 45
          sendCommand(4, speed);
          //sendCommand(4, speedStart - 8 * speedStep * dashboardAngle / Math.PI );
        }
      }
      break;
    case 37:
      if(manualFlg){
        // alert("Left pressed!");
        if(dashboardAngle > -90){
          dashboardAngle -= 22.5;
          drawDial(circle2, 90 - dashboardAngle, 'Steering Wheel');
          sendCommand(1, dashboardAngle);
        }
      }
      break;
    case 39:
      if(manualFlg){
        // alert("Right pressed!");
        if(dashboardAngle < 90){
          dashboardAngle += 22.5;
          drawDial(circle2, 90 - dashboardAngle, 'Steering Wheel');
          sendCommand(2, dashboardAngle);
        }
      }
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
      drawAnnotations(circle, caption, speedStart, speedStep);
    // Steering wheel dail
   }else{
      context.clearRect(320,0,320,320);
      drawAnnotations(circle, caption, wheelStart, wheelStep);
    }

   // drawGrid(circle, 'lightgray', 10, 10);

   // alert(circle.x);
   drawCentroid(circle);
   drawCentroidGuidewire(circle, loc, angle);
   drawRing(circle);
   drawTickInnerCircle(circle);
   drawTicks(circle);

}

function sendCommand(command, value){
    var xmlHttp; 
     
    // 处理Ajax浏览器兼容
    if (window.ActiveXObject) {   
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");   
    }else if (window.XMLHttpRequest) {   
        xmlHttp = new XMLHttpRequest();   
    } 
     
    // cmd values:
    // 0: Mode switch (auto | manual) - blank key
    // 1: Turn Left                   - Left key
    // 2: Turn Right                  - Right key
    // 3: Speed up                    - Up key
    // 4: Speed down                  - Down key
    var url = "control.jsp?cmd=" + command.toString() + "&value=" + value.toString(); // 使用JS中变量tmp    
    xmlHttp.open("post",url,true);   //配置XMLHttpRequest对象
      
     // alert("*** url = " + url);
    //设置回调函数
    xmlHttp.onreadystatechange = function (){
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
           // var respText = xmlHttp.responseText;
           // alert("Success! cmd = " + command);        
        }else{
          // alert(xmlHttp.status);
        }
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

var c=document.getElementById("myCanvas");
var ctx = c.getContext("2d");

var img = new Image();
img.onload = function(){
    ctx.drawImage(img,0,0);
}
img.src = 'map2.png';

var x = 60;
var y = 560;
var speed = 0;
// var x = 275;
// var y = 80;
var ss = setInterval(
    function(){  
      if(x>56 && x <280){
        // speed = 2;
        // path 1 (before beacon 1)
        if(y>498){
          ctx.clearRect(x-12,y,31,31);
          ctx.beginPath();
          ctx.fillStyle="blue";  
          ctx.arc(x,y,5,0,Math.PI*2,true);
          ctx.fill();
          ctx.stroke();
          ctx.closePath();
          y -= speed;
        }else{
          if(x<275 && y>400){
            // path 2 (between beacon 1 and 2)
            ctx.clearRect(x-12,y-12,31,31);

            ctx.beginPath();
            ctx.fillStyle="blue";  
            ctx.arc(x,y,5,0,Math.PI*2,true);
            ctx.fill();
            ctx.stroke();
            ctx.closePath();
            x += speed;
          }else{
            if(y>35){
              // path 3 (between beacon 2 and 3)
              ctx.clearRect(x-12,y-12,31,31);
              ctx.beginPath();
              ctx.fillStyle="blue";  
              ctx.arc(x,y,5,0,Math.PI*2,true);
              ctx.fill();
              ctx.stroke();
              ctx.closePath();
              y -= speed;
            }
          }
        }

        if(x>60 && y<=35){
          // path 4 (between beacon 3 and 4)
          ctx.clearRect(x-12,y-12,31,31);

          ctx.beginPath();
          ctx.fillStyle="blue";  
          ctx.arc(x,y,5,0,Math.PI*2,true);
          ctx.fill();
          ctx.stroke();
          ctx.closePath();
          x -= speed;
        }
      }
    },
100);

// var s = "<%=sss%>"; //"" can not be ignored!
// alert(sss);  