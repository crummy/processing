package com.malcolmcrum.processing.sensorreader;

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
public class SensorReader {

	private final String uri = "http://192.168.2.221:3000/";

	private final Set<FloatListener> floatListeners = new HashSet<>();
	private final Set<StatusListener> statusListeners = new HashSet<>();
	private final Set<SensorListener> sensorListeners = new HashSet<>();

	static SensorReader readSensors(SensorListener sensorListener) {
		return new SensorReader(sensorListener);
	}

	static SensorReader readFloats(FloatListener floatListener) {
		return new SensorReader(floatListener);
	}

	private SensorReader(SensorListener sensorListener) {
		this();
		addDataListener(sensorListener);
	}

	private void addDataListener(SensorListener sensorListener) {
		sensorListeners.add(sensorListener);
	}

	private SensorReader(FloatListener floatListener) {
		this();
		addFloatListener(floatListener);
	}

	private SensorReader() {
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
		}).on("phonemotion", data -> {
			receivedEvent(data);
		}).on(Socket.EVENT_DISCONNECT, data -> {
			updateStatus("Disconnected (" + data + ")");
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
		try {
			JSONObject obj = (JSONObject) data[0];

			//updateStatus("Event received: " + obj);

			double x = obj.getJSONObject("acceleration").getDouble("x");
			double y = obj.getJSONObject("acceleration").getDouble("y");
			double z = obj.getJSONObject("acceleration").getDouble("z");
			double alpha = obj.getJSONObject("rotationRate").getDouble("alpha");
			double beta = obj.getJSONObject("rotationRate").getDouble("beta");
			double gamma = obj.getJSONObject("rotationRate").getDouble("gamma");

			Sensors sensors = new Sensors(x, y, z, alpha, beta, gamma);

			sensorListeners.forEach(listener -> listener.receivedData(sensors));
			floatListeners.forEach(listener -> listener.receivedFloat((float)x));

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

	interface SensorListener {
		void receivedData(Sensors sensors);
	}

	class Sensors {
		public final double x,y,z;
		public final double alpha,beta,gamma;
		public Sensors(double x, double y, double z, double alpha, double beta, double gamma) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.alpha = alpha;
			this.beta = beta;
			this.gamma = gamma;
		}
	}
}
