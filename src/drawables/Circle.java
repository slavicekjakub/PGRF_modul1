package drawables;

import utils.Renderer;

public class Circle implements Drawable {

    int x1,y1,x2,y2,color;

    public Circle(int x1, int y1, int x2, int y2, int color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.kruznice(x1,y1,x2,y2,color);
    }
}
