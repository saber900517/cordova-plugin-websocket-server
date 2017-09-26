# create a websocket server on android

应用场景：物联网设备，安卓系统上位机与下位机通讯，无显示器的板子通过热点配网等<br/>
支持事件：message，close，error，open, start<br/>
支持方法：write(socketId,data)<br/>



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
			if(data.type=="message"){
				console.log(data);
			}
			else if(data.type=="close"){
				console.log(data);
			}
			else if(data.type=="error"){
				console.log(data);
			}
			else if(data.type=="open"){
				console.log(data);
			}
			else if(data.type=="start"){
				console.log(data);
			}
		});
	}, false);


```````````
<br/>
如果您喜欢nodejs格式的事件写法，可以自行在业务层封装。<br/>

#API文档
接口很少，见示例



