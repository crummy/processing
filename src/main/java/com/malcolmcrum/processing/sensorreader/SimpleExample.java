package com.malcolmcrum.processing.sensorreader;

import processing.core.PApplet;

/**
 * Created by crummy on 19/11/2016.
 */
public class SimpleExample extends PApplet {
	public static void main(String args[]) {
		PApplet.main(SimpleExample.class.getName());
	}

	private float radius = 0;
	private float x = -1;

	@Override
	public void settings() {
		size(480, 480);
		SensorReader.readFloats(f -> x = f);
	}

	@Override
	public void setup() {
		clear();
	}

	@Override
	public void draw() {
		radius = lerp(radius, width*x/2, 0.1f);

		clear();
		ellipseMode(RADIUS);
		ellipse(width/2, height/2, radius, radius);
	}
}
