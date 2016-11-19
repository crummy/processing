package com.malcolmcrum.processing.sensorreader;

import processing.core.PApplet;

/**
 * Created by crummy on 19/11/2016.
 */
public class Landscape extends PApplet{

	private float cameraX;
	private float cameraY;
	private float time;

	public static void main(String args[]) {
		PApplet.main(Landscape.class.getName());
	}

	int  d=8;

	float[][] val ;
	float xSize,ySize;
	float increment = 0.03f;
	float zincrement = 0.01f;

	@Override
	public void settings() {
		size(720, 480, P3D);
	}

	@Override
	public void setup(){
		//smooth();
		SensorReader.readSensors(sensor -> {
			cameraX += sensor.alpha;
			cameraY += sensor.beta;
			time += sensor.gamma * zincrement;
		});
		val = new float[width/d][height/d];
		xSize = width/d;
		ySize = height/d;
	}

	@Override
	public void draw(){
		background(0);
		lights();
		fill(255, 0, 175);
		//stroke(0, 150, 150);
		noStroke();
		camera(cameraX, cameraY, (height/2.0f) / tan(PI*60.0f / 360.0f),
				width/2.0f, height/2.0f, 0, // centerX, centerY, centerZ
				0.0f, 1.0f, 0.0f); // upX, upY, upZ
		translate(0, height/2, -300);
		rotateX(70);

		noiseSeed(12);
		float xoff = 0.0f; // Start xoff at 0
		noiseDetail(8,0.3f);
		// For every x,y coordinate in a 2D space, calculate a noise value and produce a brightness value
		for (int x = 0; x < width/d; x++) {
			xoff += increment;   // Increment xoff
			float yoff = 0.0f;   // For every xoff, start yoff at 0
			for (int y = 0; y < height/d; y++) {
				yoff += increment; // Increment yoff
				float z = noise(xoff,yoff,time)*600;
				val[x][y] = z;
			}
		}

		for (int x=0; x<xSize-1; x++){
			for(int y=0; y<ySize-1; y++){
				fill((val[x][y])-50, 0, 200);
				beginShape();
				vertex(x*d, y*d, val[x][y]);
				vertex( x*d+d, y*d, val[x+1][y]);
				vertex(x*d+d, y*d+d, val[x+1][y+1]);
				vertex(x*d, y*d+d,val[x][y+1]);
				endShape(CLOSE);
			}
		}

	}
}
