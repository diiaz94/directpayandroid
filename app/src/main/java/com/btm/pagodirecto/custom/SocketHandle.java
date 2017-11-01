package com.btm.pagodirecto.custom;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.google.gson.Gson;

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
        mSocket.on(Constants.ENTER_REGION, onEnterRegion);
        mSocket.on(Constants.EXIT_REGION, onExitRegion);
        mSocket.on(Constants.ADD_USER, onAddUser);
        mSocket.on(Constants.REMOVE_USER, onRemoveUser);
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

    private static Emitter.Listener onAddUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            //Log.i(TAG,"onEnterRegion " );

            try {
                JSONObject obj = (JSONObject) args[0];
            //User user = Util.string2Object(obj.getJSONObject("user").toString(),User.class);

            Intent intent = new Intent(Constants.ADD_USER);
            intent.putExtra("user", obj.getJSONObject("user").toString());
            LocalBroadcastManager.getInstance(Util.getContext()).sendBroadcast(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static Emitter.Listener onRemoveUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            //Log.i(TAG,"onEnterRegion " );

        }


    };
}