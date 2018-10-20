package drawables;

import utils.Renderer;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements Drawable{

    public List<Point> points;

    public Polygon() {
        points = new ArrayList<>();
    }

    public Polygon(List<Point> points){
        this.points = points;
    }

    public List addPoint(Point p){
        points.add(p);
        return points;
    }

    @Override
    public void draw(Renderer renderer) {
        //TODO: for cyklus propojit body a nakonec prvni s poslednim
        renderer.pol(points);
    }
}
