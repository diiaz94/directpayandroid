package com.btm.pagodirecto.custom;

import android.content.Context;
import android.util.Log;

import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by Pedro on 21/10/2017.
 */

public class SocketHandle {
    private static final String TAG = "SocketHandle";
    private static Socket mSocket;

    private static final String EVENT_ENTER_REGION = "Enter region";
    private static final String EVENT_EXIT_REGION = "Exit region";


    public static void init(Context context) {

        try {
            mSocket = IO.socket(Constants.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Util.setContext(context);
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        //mSocket.on("enter region", onJoin);
        mSocket.on(EVENT_ENTER_REGION, onEnterRegion);
        mSocket.on(EVENT_EXIT_REGION, onExitRegion);
        //mSocket.on("new message", onNewMessage);
        //mSocket.on("received message", onReceiveMessage);
        //mSocket.on("read message", onReadMessage);
        mSocket.connect();

    }

    public static void emitEvent(String event, JSONObject args) {

        mSocket.emit(event, args);
    }

    private static Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.i(TAG, "Connected ");

            JSONObject data = new JSONObject();
            //data.put("user_id",Util.getId());
            //IterSocket.emitEvent("rejoin",data);

            //Util.showToastMessage("Connected" + Util.getEmail());
        }
    };
    private static Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "Disconnected ");
            // Util.showToastMessage("Disconnected"  + Util.getEmail());
        }
    };
    private static Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "onConnectError ");
            //Util.showToastMessage(R.string.error_connect + Util.getEmail());
        }
    };


    private static Emitter.Listener onEnterRegion = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            //Log.i(TAG,"onEnterRegion " );

        }

        
    };

    private static Emitter.Listener onExitRegion = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            //Log.i(TAG,"onExitRegion " );

        }


    };
}