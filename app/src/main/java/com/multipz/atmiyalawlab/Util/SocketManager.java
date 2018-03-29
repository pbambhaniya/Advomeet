/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jordon de Hoog
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.multipz.atmiyalawlab.Util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by jordon on 23/02/16.
 * Handle the events for the Socket Singleton
 */
public class SocketManager {
    private static SocketManager instance;
    private Context app;
    private Socket socket;


    public final static String EVENT_AUTHENTICATED = "server.authentication.success";
    public final static String EVENT_SERVER_CONVERSATION_CREATED = "server.conversation.created";
    public final static String EVENT_CONVERSATION_CREATE = "conversation.create";

    private SocketManager(Context context) {
        this.app = context;
        try {
            IO.Options options = new IO.Options();
            options.reconnectionAttempts = 0;
            socket = IO.socket(Config.SocketUrl, options);
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_RECONNECT_ATTEMPT, onReconnectAttempt);
            socket.on(Socket.EVENT_RECONNECT, onReconnect);
            socket.on(EVENT_AUTHENTICATED, onAuthenticated);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static SocketManager getInstance(Context context) {
        if (instance == null) {
            instance = new SocketManager(context);
        }

        return instance;
    }

    public SocketManager connect() {
        Log.d("SocketManager", "SocketManager#connect");
        this.socket.connect();

        return this;
    }

    public void sendEmit(String event, JSONObject data) {
        this.socket.emit(event, data);
    }

    public void sendEmit(String event, String data) {
        this.socket.emit(event, data);
    }

    public void addListener(String event, Emitter.Listener listener) {
        this.socket.off(event, listener);
        this.socket.on(event, listener);
    }

    private final Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("SocketManager", "socket event: connected");
            JSONObject obj = new JSONObject();
            socket.emit("authentication", obj);
        }
    };

    private final Emitter.Listener onReconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            // TODO
        }
    };

    private final Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("SocketManager", "socket event: authenticated");
            // TODO: ask the server what was lost
        }
    };

    private final Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("SocketManager", "socket event: disconnect");
        }
    };

    private Emitter.Listener onReconnectAttempt = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("SocketManager", "socket event: reconnect attempt");
        }
    };

}
