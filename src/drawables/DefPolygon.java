package drawables;

import utils.Renderer;

public class DefPolygon implements Drawable{
    int x1,y1,x2,y2,count,color;

    public DefPolygon(int x1, int y1, int x2, int y2, int count, int color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.count = count;
        this.color = color;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.polygon(x1,y1,x2,y2,count,color);
    }
}
