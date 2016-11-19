package com.malcolmcrum.processing.sensorreader;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crummy on 19/11/2016.
 */
public class Particles extends PApplet {
	public static void main(String args[]) {
		PApplet.main(Particles.class.getName());
	}

	private boolean isDataFresh = false;
	private SensorReader.Sensors sensors;
	private List<Particle> particles = new ArrayList<>();

	@Override
	public void settings() {
		size(480, 480);
		SensorReader.readSensors(data -> {
			isDataFresh = true;
			sensors = data;
		});
	}

	@Override
	public void setup() {
		clear();
	}

	@Override
	public void draw() {
		if (isDataFresh) {
			if (abs((float)sensors.x) > 1 || abs((float)sensors.y) > 1) {
				particles.add(new Particle((float)sensors.x, (float)sensors.y, (float)sensors.z));
			}
			isDataFresh = false;
		}

		particles.forEach(particle -> {
			particle.move();
			particle.draw();
		});

		while (particles.size() > 512) {
			particles.remove(0);
		}
	}

	private class Particle {
		float x = width/2;
		float y = height/2;
		float rot;
		float accelerationX;
		float accelerationY;
		float accelerationRotation;
		float intensity;

		Particle(float velocityX, float velocityY, float velocityRotation) {
			this.accelerationX = velocityX;
			this.accelerationY = velocityY;
			this.accelerationRotation = velocityRotation * 0.01f;
			this.intensity = (abs(velocityX) + abs(velocityY)) * 10;
		}

		void move() {
			accelerationY += 0.02f;
			x += accelerationX;
			y += accelerationY;
			rot += accelerationRotation;
		}

		void draw() {
			pushMatrix();
			translate(x, y);
			rotate(rot);
			noStroke();
			fill(-128 + intensity, intensity, 0);
			rect(0, 0, 16, 16);
			popMatrix();
		}
	}
}
