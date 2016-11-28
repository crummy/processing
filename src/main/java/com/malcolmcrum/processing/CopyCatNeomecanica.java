package com.malcolmcrum.processing;

import processing.core.PApplet;

/**
 * Copying https://twitter.com/neomechanica/status/802975275071967232
 */
public class CopyCatNeomecanica extends PApplet {
	public static void main(String args[]) {
		PApplet.main(CopyCatNeomecanica.class.getName());
	}

	private static float perlinZoom = 0.003f;
	private static float perlinHeight = 256;
	private static int gridSize = 8;
	private static int SPHERE_RADIUS = 64;

	@Override
	public void settings() {
		size(480, 600, P3D);
	}

	@Override
	public void setup() {
		clear();
	}

	@Override
	public void draw() {
		clear();
		drawVerticalLines();
		//drawTerrainWireframe();
		drawTerrainWireframeFilled();
		drawSphere();
	}

	private void drawSphere() {
		fill(0);
		stroke(0);
		pushMatrix();
		translate(width/2, height * 0.25f, perlinHeight * abs(noise(width/2 * perlinZoom, height*0.25f * perlinZoom)));
		sphere(SPHERE_RADIUS);
		popMatrix();

	}

	private void drawTerrainWireframeFilled() {
		pushMatrix();
		translate(0, height/2, -128);
		rotateX(20);

		fill(0);
		for (int x = 0; x < width; x += gridSize) {
			for (int y = 0; y < height; y += gridSize) {
				beginShape();
				perlinVertex(x, y);
				perlinVertex(x + gridSize, y);
				perlinVertex(x + gridSize, y + gridSize);
				perlinVertex(x, y + gridSize);
				endShape();
			}
		}
		popMatrix();
	}

	private void perlinVertex(int x, int y) {
		vertex(x, y, perlinHeight * abs(noise(x * perlinZoom, y * perlinZoom)));
	}

	private void drawVerticalLines() {
		stroke(255);
		int distantBackground = -512;
		for (int x = -1024; x < width + 1024; x += width/16) {
			line(x, -1024, distantBackground, x, height, distantBackground);
		}
	}
}
