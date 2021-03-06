package drawables;

import utils.Renderer;

import java.awt.*;

public class Line implements Drawable{

    private int x1,y1,x2,y2,color;

    public Line(int x1, int y1, int x2, int y2, int color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.lineDDA(x1,y1,x2,y2,getColor());
    }

    @Override
    public void modifyLastPoint(int x, int y) {
        this.x2 = x;
        this.y2 = y;
    }

    @Override
    public int getColor() {
        return Color.GREEN.getRGB();
    }

}
