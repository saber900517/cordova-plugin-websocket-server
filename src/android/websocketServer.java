package cordova-plugin-websocketServer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//websocket start
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
//websocket end

/**
 * This class echoes a string called from JavaScript.
 */
public class websocketServer extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("startServer")) {
			String ipAddress = args.getString(0);
			int port= args.getInt(1);
			this.startServer(ipAddress, port, callbackContext);
			return true;
		}
		return false;
	}

	private void startServer(String ipAddress, int port, CallbackContext callbackContext){
		 InetSocketAddress inetSockAddress = new InetSocketAddress(ipAddress, port);
		 WebsocketServer wsServer = new WebsocketServer(inetSockAddress,callbackContext);
		 wsServer.start();
	}

	private class WebsocketServer extends WebSocketServer {
		CallbackContext callbackContext;
		public WebsocketServer(InetSocketAddress address, CallbackContext callbackContext) {
			super(address);
			this.callbackContext=callbackContext;
		}

		@Override
		public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
			JSONObject r = new JSONObject();
			r.put("type", "onClose");
			r.put("arg1", arg1);
			r.put("arg2", arg2);
			r.put("arg3", arg3);
			this.callbackContext.success(r);
		}

		@Override
		public void onError(WebSocket arg0, Exception arg1) {
			JSONObject r = new JSONObject();
			r.put("type", "onError");
			this.callbackContext.success(r);
			System.out.println(arg1.getStackTrace());
		}

		@Override
		public void onMessage(WebSocket arg0, String arg1) {
			JSONObject r = new JSONObject();
			r.put("type", "onMessage");
			r.put("arg1", arg1);
			this.callbackContext.success(r);
		}

		@Override
		public void onOpen(WebSocket arg0, ClientHandshake arg1) {
			JSONObject r = new JSONObject();
			r.put("type", "onOpen");
			r.put("RemoteSocketAddress", arg0.getRemoteSocketAddress());
			this.callbackContext.success(r);
			System.out.println("new connection to " + arg0.getRemoteSocketAddress());
		}

		@Override
		public void onStart() {
			JSONObject r = new JSONObject();
			r.put("type", "onStart");
			this.callbackContext.success(r);
			System.out.println("Server started!");
		}
	}
}
