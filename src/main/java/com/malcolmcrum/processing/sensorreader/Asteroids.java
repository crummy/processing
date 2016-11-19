package com.malcolmcrum.processing.sensorreader;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crummy on 19/11/2016.
 */
public class Asteroids extends PApplet {
	public static void main(String args[]) {
		PApplet.main(Asteroids.class.getName());
	}

	private float angle;
	private float speed;

	private float desiredAngle;
	private float desiredSpeed;
	private List<Star> stars = new ArrayList<>();

	@Override
	public void settings() {
		size(480, 480);
	}

	@Override
	public void setup() {
		clear();
		SensorReader.readSensors(sensors -> {
			desiredAngle += radians((float)sensors.gamma);
			desiredSpeed = abs((float)sensors.x) + abs((float)sensors.y) + abs((float)sensors.z);
		});

		for (int i = 0; i < 100; ++i) {
			stars.add(new Star(random(width), random(height)));
		}
	}

	@Override
	public void draw() {
		clear();
		angle = lerp(angle, desiredAngle, 0.9f);
		speed = lerp(speed, desiredSpeed, 0.01f);
		drawStarField();
		drawShip();
	}

	private void drawStarField() {
		ellipseMode(CENTER);
		stars.forEach(star -> {
			star.move();
			float x = star.getX();
			float y = star.getY();
			ellipse(x, y, 3, 3);
		});
	}

	private void drawShip() {
		pushMatrix();
		translate(width/2 - 8, height/2 - 8);
		rotate(-angle);
		triangle(0, 16, 16, 16, 8, -4);
		popMatrix();
	}

	private class Star {
		private float x;
		private float y;

		Star(float x, float y) {
			this.x = x;
			this.y = y;
		}

		float getX() {
			return x;
		}

		float getY() {
			return y;
		}

		void move() {
			x = x + sin(angle) * speed;
			y = y + cos(angle) * speed;

			if (x < 0) {
				x = width;
			} else if (x > width) {
				x = 0;
			}

			if (y < 0) {
				y = height;
			} else if (y > height) {
				y = 0;
			}
		}
	}
}
