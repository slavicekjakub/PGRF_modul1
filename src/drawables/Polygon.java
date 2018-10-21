package drawables;

import utils.Renderer;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements Drawable{

    private int color;
    public List<Point> points;

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
        //TODO: for cyklus propojit body a nakonec prvni s poslednim
        renderer.pol(points, color);
    }
}
