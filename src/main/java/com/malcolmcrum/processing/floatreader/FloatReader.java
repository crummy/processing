package com.malcolmcrum.processing.floatreader;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by crummy on 19/11/2016.
 */
public class FloatReader {

	private final String uri = "http://192.168.2.221:3000/";

	private final Set<FloatListener> floatListeners = new HashSet<>();
	private final Set<StatusListener> statusListeners = new HashSet<>();

	public static FloatReader read(FloatListener floatListener) {
		return new FloatReader(floatListener);
	}

	private FloatReader(FloatListener floatListener) {
		this();
		addFloatListener(floatListener);
	}

	private FloatReader() {
		IO.Options opts = new IO.Options();
		opts.reconnection = true;

		addStatusListener(System.out::println);

		Socket socket;
		try {
			socket = IO.socket(uri, opts);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Failed to parse " + uri, e);
		}

		updateStatus("Connecting to " + uri);
		socket.on(Socket.EVENT_CONNECT, data -> {
			updateStatus("Connected (" + data + ")");
			socket.emit("my other event", "hi from a new client");
		}).on("data", data -> {
			receivedEvent(data);
		}).on(Socket.EVENT_DISCONNECT, data -> {
			JSONObject obj = (JSONObject) data[0];
			updateStatus("Disconnected (" + obj + ")");
		});
		socket.connect();
	}

	private void addStatusListener(StatusListener statusListener) {
		statusListeners.add(statusListener);
	}

	private void addFloatListener(FloatListener floatListener) {
		floatListeners.add(floatListener);
	}

	private void updateStatus(String message) {
		statusListeners.forEach(listener -> listener.statusChanged(message));
	}

	private void receivedEvent(Object[] data) {
		float f;
		try {
			JSONObject obj = (JSONObject) data[0];

			updateStatus("Event received: " + obj);

			f = (float) obj.getDouble("acc");

			floatListeners.forEach(listener -> listener.receivedFloat(f));
		} catch (ClassCastException e) {
			updateStatus("Couldn't cast class" + e.getMessage());
		} catch (JSONException e) {
			updateStatus("Json parse failed: " + e.getMessage());
		}
	}

	interface FloatListener {
		void receivedFloat(float f);
	}

	interface StatusListener {
		void statusChanged(String message);
	}
}
