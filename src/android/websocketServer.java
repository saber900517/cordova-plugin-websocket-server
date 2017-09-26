package websocketServer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;

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
import android.util.Log;
import java.util.UUID;
import android.util.Base64;
import java.util.HashMap;

/**
 * This class echoes a string called from JavaScript.
 */
public class websocketServer extends CordovaPlugin {
    public WebsocketServer wsServer=null;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("startServer")) {
            String ipAddress = args.getString(0);
            int port= args.getInt(1);
            this.startServer(ipAddress, port, callbackContext);
            return true;
        }
        else if (action.equals("stopServer")) {
            try{
                wsServer.stop();
            }
            catch(Exception e){
                return false;
            }
            return true;
        }
        return false;
    }

    private void startServer(String ipAddress, int port, CallbackContext callbackContext){
         InetSocketAddress inetSockAddress = new InetSocketAddress(ipAddress, port);
         wsServer = new WebsocketServer(inetSockAddress,callbackContext);
         wsServer.start();
    }

    public void sendPluginResult(CallbackContext callbackContext,JSONObject obj){
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }

    private class WebsocketServer extends WebSocketServer {
        CallbackContext myCallbackContext;
        HashMap<String, WebSocket> socketHashMap = new HashMap<String, WebSocket>();

        public WebsocketServer(InetSocketAddress address, CallbackContext callbackContext) {
            super(address);
            this.myCallbackContext=callbackContext;
        }

        @Override
        public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
            String id=getSocketId(arg0);
            socketHashMap.remove(id);
            JSONObject closeObject = new JSONObject();
            try {
                closeObject.put("type", "close");
                closeObject.put("id", id);
                closeObject.put("arg1", arg1);
                closeObject.put("arg2", arg2);
                closeObject.put("arg3", arg3);
            } catch(JSONException e){
                e.printStackTrace();
            }
            websocketServer.this.sendPluginResult(myCallbackContext, closeObject);
        }

        @Override
        public void onError(WebSocket arg0, Exception arg1) {
            String id=getSocketId(arg0);
            socketHashMap.remove(id);
            for (HashMap.Entry<String, WebSocket> entry : socketHashMap.entrySet()) {
                if(arg0==entry.getValue()){
                    id=entry.getKey();
                }
            }
            JSONObject errorObject = new JSONObject();
            try{
                errorObject.put("type", "onError");
                errorObject.put("id", id);
                errorObject.put("exception", arg1.getMessage());
            } catch(JSONException e){
                e.printStackTrace();
            }   
            websocketServer.this.sendPluginResult(this.myCallbackContext,errorObject);
        }

        @Override
        public void onMessage(WebSocket arg0, String arg1) {
            String id=getSocketId(arg0);
            JSONObject msgObject = new JSONObject();
            try{
                msgObject.put("type", "onMessage");
                msgObject.put("data", arg1);
                msgObject.put("socketId", id);
            } catch(JSONException e){
                e.printStackTrace();
            }   
            websocketServer.this.sendPluginResult(this.myCallbackContext,msgObject);
        }

        @Override
        public void onOpen(WebSocket arg0, ClientHandshake arg1) {
            String uuid = UUID.randomUUID().toString();
            socketHashMap.put(uuid,arg0);
            JSONObject openObject = new JSONObject();
            try{
                openObject.put("type", "onOpen");
                openObject.put("socketId", uuid);
                openObject.put("address", arg0.getRemoteSocketAddress());
            } catch(JSONException e){
                e.printStackTrace();
            }
            websocketServer.this.sendPluginResult(this.myCallbackContext,openObject);
        }

        @Override
        public void onStart() {
            JSONObject startObject = new JSONObject();
            try{
                startObject.put("type", "onStart");
            } catch(JSONException e){
                e.printStackTrace();
            }
            websocketServer.this.sendPluginResult(this.myCallbackContext,startObject);
        }

        public String getSocketId(WebSocket arg ) {
            String id="";
            for (HashMap.Entry<String, WebSocket> entry : socketHashMap.entrySet()) {
                if(arg==entry.getValue()){
                    id=entry.getKey();
                }
            }
            return id;
        }
    }
}
