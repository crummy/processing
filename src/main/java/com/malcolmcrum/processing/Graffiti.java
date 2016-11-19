package com.malcolmcrum.processing;

import javafx.geometry.Point2D;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Graffiti extends PApplet {

    private List<Dot> dots;

    public static void main(String args[]) {
        PApplet.main("Graffiti");
    }

    @Override
    public void settings() {
        // TODO: Customize screen size and so on here
        size(800, 480);
    }

    @Override
    public void setup() {
        // TODO: Your custom drawing and setup on applet start belongs here
        clear();
        dots = createPoints();
    }

    private List<Dot> createPoints() {
        List<Dot> dots = new ArrayList<>();
        for (int i = 0; i < (int)random(4) + 3; ++i) {
            dots.add(new Dot(random(width), random(height), 10 + random(32)));
        }
        return dots;
    }

    @Override
    public void draw() {
        // TODO: Do your drawing for each frame here
        clear();
        noStroke();
        //fill(0, 0, 255);
        //toLines(points).forEach(this::dropShadow);
        fill(255, 255, 0);
        toLines(dots).forEach(line -> {
            circle(line.start);
            circle(line.end);
            line.updateTangents();
            beginShape();
            line.getConnectingPolygon().forEach(point -> vertex((float)point.getX(), (float)point.getY()));
            endShape();
        });
    }

    private void dropShadow(Line line) {
        beginShape();
        vertex((float)line.start.getX() - 10, (float)line.start.getY());
        vertex((float)line.start.getX() - 10, (float)line.start.getY() + 40);
        vertex((float)line.end.getX() + 10, (float)line.end.getY() + 40);
        vertex((float)line.end.getX() + 10, (float)line.end.getY());
        endShape();
        ellipse((float)line.start.getX(), (float)line.start.getY(), 10, 10);
    }

    private void connectCircles(Line line) {
        circle(line.start);

    }

    private void circle(Dot p) {
        float x = (float) p.getX();
        float y = (float) p.getY();
        ellipse(x, y, p.getRadius() * 2, p.getRadius() * 2);
    }

    private static List<Line> toLines(List<Dot> dots) {
        List<Line> lines = new ArrayList<>();
        if (dots.size() <= 1) {
            return lines;
        }

        Dot last = dots.get(dots.size() - 1);
        for (Dot p : dots) {
            lines.add(new Line(last, p));
            last = p;
        }
        return lines;
    }

    static class Line {
        final Dot start;
        final Dot end;
        private List<Point2D> connectingPolygon;

        Line(Dot start, Dot end) {
            this.start = start;
            this.end = end;
            connectingPolygon = new ArrayList<>();
        }

        void updateTangents() {
            double[][] tangents = CircleTangents.getTangents(start, end);
            connectingPolygon.clear();

            connectingPolygon.add(new Point2D(tangents[0][0], tangents[0][1]));
            connectingPolygon.add(new Point2D(tangents[0][2], tangents[0][3]));

            connectingPolygon.add(new Point2D(tangents[1][2], tangents[1][3]));
            connectingPolygon.add(new Point2D(tangents[1][0], tangents[1][1]));
        }

        List<Point2D> getConnectingPolygon() {
            return connectingPolygon;
        }
    }

    static class Dot extends Point2D {
        private float r;

        Dot(float x, float y, float r) {
            super(x, y);
            this.r = r;
        }

        float getRadius() {
            return r;
        }
    }
}
