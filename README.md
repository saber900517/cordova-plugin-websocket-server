# create a websocket server on android

应用场景：物联网设备，安卓系统上位机与下位机通讯，无显示器的板子通过热点配网等<br/>
支持事件：onconnect，ondata，onclose，<br/>
//支持方法：write(socketId,data)<br/>

只有一个.java文件文件和一个.js文件，不依赖其他第三方模块，修改维护简单。

#安装方法: 

``````
cordova plugin add https://github.com/huge818/cordova-plugin-websocket-server.git
````````

#使用示例
```````````
	document.addEventListener("deviceready", function(){
		var websocketServer=cordova.plugins.websocketServer;
		websocketServer.stopServer();
		websocketServer.startServer("0.0.0.0",8080,function(data){
			   console.log(data);
		  	 var str=JSON.stringify(data);
		  	 console.log(str);
		});
	}, false);

```````````
<br/>
如果您喜欢nodejs格式的事件写法，可以自行在业务层封装。<br/>

#API文档
接口很少，见示例



