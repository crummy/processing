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
	private int time = 0;

	@Override
	public void settings() {
		size(480, 480);
		SensorReader.readSensors(sensors -> x = (float)sensors.x);
	}

	@Override
	public void setup() {
		clear();
	}

	@Override
	public void draw() {
		time++;

		clearScreen();

		int radius = 32;
		for (int i = 0; i < 32; ++i) {
			ellipse(radius * i, width/2 + sin(time + i*32), radius, radius);
		}
	}

	private void clearScreen() {
		color(255, 255, 255, 0.5f);
		rect(0, 0, width, height);
	}
}
