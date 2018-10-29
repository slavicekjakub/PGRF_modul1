package drawables;

import utils.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements Drawable{

    private int color;
    public List<Point> points;
    private boolean done;

    public Polygon() {
        points = new ArrayList<>();
    }

    public Polygon(List<Point> points, int color){
        this.points = points;
        this.color = color;
    }

    public List addPoint(Point p){
        points.add(p);
        return points;
    }

    @Override
    public void draw(Renderer renderer) {
        if (!done){
            renderer.pol(points, getColor());
        } else {
            renderer.scanLine(points, getColor(),getFillColor());
        }

    }

    @Override
    public void modifyLastPoint(int x, int y) {
        //ingored
    }

    @Override
    public int getColor() {
        return Color.BLUE.getRGB();
    }

    @Override
    public int getFillColor() {
        return Color.YELLOW.getRGB();
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
