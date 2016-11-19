package com.malcolmcrum.processing.sensorreader;

import processing.core.PApplet;

/**
 * Created by crummy on 19/11/2016.
 */
public class Sine extends PApplet {
	public static void main(String args[]) {
		PApplet.main(Sine.class.getName());
	}

	private float x = -1;
	private float time = 0;

	@Override
	public void settings() {
		size(480, 480);
		SensorReader.readSensors(sensors -> x += (float)sensors.beta);
	}

	@Override
	public void setup() {
		clear();
	}

	@Override
	public void draw() {
		time += 0.0001;

		clearScreen();

		int radius = 8;
		for (int i = 0; i < width/radius; ++i) {
			fill((time + i*x*0.01f) * 255, (time + i*x*0.01f) * -255, 0);
			ellipse(radius * i, width/2 + width/2 * sin(time + i*x*0.01f), radius, radius);
		}
	}

	private void clearScreen() {
		fill(255, 255, 255, 20);
		rect(0, 0, width, height);
	}
}
