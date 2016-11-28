package com.malcolmcrum.processing.graffiti;

import java.util.Arrays;

/**
 * Created by crummy on 17/07/16.
 * copied from wikipedia
 */
public class CircleTangents {
	public static double[][] getTangents(Graffiti.Dot p1, Graffiti.Dot p2) {
		double distance = p1.distance(p2);
		if (distance <= p1.getRadius() - p2.getRadius()) {
			return new double[0][4];
		}

		double vx = (p2.getX() - p1.getX()) / distance;
		double vy = (p2.getY() - p1.getY()) / distance;
		double[][] res = new double[4][4];
		int i = 0;

		for (int sign1 = +1; sign1 >= -1; sign1 -= 2) {
			double c = (p1.getRadius() - sign1 * p2.getRadius()) / distance;

			if (c * c > 1.0) {
				continue;
			}

			double h = Math.sqrt(Math.max(0, 1 - c * c));

			for (int sign2 = +1; sign2 >= -1; sign2 -= 2) {
				double nx = vx * c - sign2 * h * vy;
				double ny = vy * c + sign2 * h * vx;

				double[] a = res[i++];
				a[0] = p1.getX() + p1.getRadius() * nx;
				a[1] = p1.getY() + p1.getRadius() * ny;
				a[2] = p2.getX() + sign1 * p2.getRadius() * nx;
				a[3] = p2.getY() + sign1 * p2.getRadius() * ny;
			}
		}

		return Arrays.copyOf(res, i);
	}
}
