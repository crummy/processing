package com.malcolmcrum.processing.sensorreader;

import processing.core.PApplet;

/**
 * Created by crummy on 19/11/2016.
 */
public class Curves extends PApplet{
	public static void main(String args[]) {
		PApplet.main(Curves.class.getName());
	}

	private float f = -1;

	@Override
	public void settings() {
		size(480, 480);
		SensorReader.readFloats(f -> this.f = f);
	}

	@Override
	public void setup() {
		clear();
	}

	@Override
	public void draw() {
		clearScreen();

		//if (frameCount % 16 == 0) {
			for (int y = 0; y < height; y += 48) {
				drawCurve(y);
			}
		//}
	}

	private void clearScreen() {
		fill(0, 0, 0, 10);
		rect(0, 0, width, height);
	}

	private void drawCurve(int y) {
		noFill();
		stroke(255);
		beginShape();
		for (int x = 0; x <= width; x += 16) {
			curveVertex(x, y + noise(x, y  * (frameCount / 10000f), f) * 64);
		}
		endShape();
	}
}
