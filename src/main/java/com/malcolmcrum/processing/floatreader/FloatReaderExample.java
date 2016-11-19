package com.malcolmcrum.processing.floatreader;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.PApplet;

import java.net.URISyntaxException;

/**
 * Created by crummy on 19/11/2016.
 */
public class FloatReaderExample extends PApplet {
	public static void main(String args[]) {
		PApplet.main(FloatReaderExample.class.getName());
	}

	public FloatReaderExample() throws URISyntaxException {
		String uri = "http://192.168.2.221:3000/";
		Socket socket = IO.socket(uri);
		status = "Connecting to " + uri;
		socket.on(Socket.EVENT_CONNECT, data -> {
			status = "Connected (" + data + ")";
			socket.emit("my other event", "hi from a new client");
		}).on("data", data -> {
			JSONObject obj = (JSONObject) data[0];
			status = "Event received: " + obj;
			receivedEvent(obj);
		}).on(Socket.EVENT_DISCONNECT, data -> {
			JSONObject obj = (JSONObject) data[0];
			status = "Disconnected (" + obj + ")";
		});
		socket.connect();
	}

	private void receivedEvent(JSONObject json) {
		try {
			System.out.println(json);
			x = (float)json.getDouble("acc");
		} catch (JSONException e) {
			status = "Json parse failed: " + e.getMessage();
		}
	}

	private String status = "Waiting for connection";
	private float x = -1;

	@Override
	public void settings() {
		size(800, 480);
	}

	@Override
	public void setup() {
		clear();
	}


	@Override
	public void draw() {
		clear();
		text(status, 0, 10);
		text(x, width / 2, height / 2);
	}
}
