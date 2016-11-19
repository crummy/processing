package com.malcolmcrum.processing.floatreader;

import processing.core.PApplet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

/**
 * Created by crummy on 19/11/2016.
 */
public class FloatReaderExample extends PApplet {
	public static void main(String args[]) {
		PApplet.main(FloatReaderExample.class.getName());
	}

	public FloatReaderExample() {
		CompletableFuture.runAsync(() -> {
			URI server;
			try {
				server = new URI("ws://Malcolms-MacBook-Pro.local:8080/");
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
			WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(server);

			clientEndPoint.addMessageHandler(message -> {
				float f = readFloat(message);
				receivedFloat(f);
			});
		});
	}

	private float readFloat(String message) {
		return parseFloat(message);
	}

	private void receivedFloat(float f) {
		x = f;
	}

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
		if (x == -1) {
			text("Waiting for server...", width/2, height/2);
		} else {
			text(x, width / 2, height / 2);
		}
	}
}
