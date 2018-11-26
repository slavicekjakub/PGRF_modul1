package utils;

import solids.Solid;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class Transformer {

    private BufferedImage img;

    private Mat4 model;
    private Mat4 view;
    private Mat4 projection;

    public Transformer(BufferedImage img) {
        this.img = img;
        this.model = new Mat4Identity();
        this.view = new Mat4Identity();
        this.projection = new Mat4Identity();

    }

    //FUNKCE
    public void drawWireFrame(Solid solid){
        //Výsledná matice zobrazení
        Mat4 matFinal = model.mul(view).mul(projection);

        //první index: 1. bod, druhý index: 2. bod úsečky
        for (int i = 0; i < solid.getIndicies().size(); i+=2) {
            Point3D p1 = solid.getVerticies().get(solid.getIndicies().get(i));
            Point3D p2 = solid.getVerticies().get(solid.getIndicies().get(i+1));
            transformEdge(matFinal,p1,p2);

        }
    }

    private void transformEdge(Mat4 mat, Point3D p1, Point3D p2) {
        // 1) vynásobit body maticí
        p1 = p1.mul(mat);
        p2 = p2.mul(mat);

        // 2) ořez dle W bodů
        if (p1.getW() <= 0 && p2.getW() <=0) return;

        // 3) tvorba z vektorů dehomogenizací (Point3D.dehomog())
        Optional<Vec3D> vo1 = p1.dehomog();
        Optional<Vec3D> vo2 = p2.dehomog();

        if(!vo1.isPresent() || !vo2.isPresent()) return;

        Vec3D v1 = vo1.get();
        Vec3D v2 = vo2.get();

        // 4) přepočet souřadnic na výšku/šířku našeho okna (viewport)
        v1 = v1.mul(new Vec3D(1,1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D(
                        0.5 * (img.getWidth()-1),
                        0.5 * (img.getHeight()),
                        1));

        v2 = v1.mul(new Vec3D(1,1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D(
                        0.5 * (img.getWidth()-1),
                        0.5 * (img.getHeight()),
                        1));

        // 5) Výsledek vykreslit - drawline(..)
        lineDDA((int)v1.getX(),(int)v1.getY(),(int)v2.getX(),(int)v2.getY(), Color.BLACK.getRGB());

    }

    //metody vykreslování
    public void lineDDA(int x1, int y1, int x2, int y2, int color) {
        int dx, dy;
        float x, y, G, H;

        dx = x2 - x1;
        dy = y2 - y1;
        float k = (float) dy / (float) dx;

        // urceni ridici osy
        if (Math.abs(dx) > Math.abs(dy)) {
            G = 1;
            H = k;
            if (x1 > x2) {
                int p = x1;
                x1 = x2;
                x2 = p;
                p = y1;
                y1 = y2;
                y2 = p;
            }
        } else {
            G = 1 / k;
            H = 1;
            //otoceni
            if (y1 > y2) {
                int p = x1;
                x1 = x2;
                x2 = p;
                p = y1;
                y1 = y2;
                y2 = p;
            }
        }

        x = x1;
        y = y1;
        int max = Math.max(Math.abs(dx), Math.abs(dy));
        for (int l = 0; l <= max; l++) {
            drawPixel(Math.round(x), Math.round(y),color);
            x += G;
            y += H;
        }
    }

    private void drawPixel(int x, int y, int color) {
        if (x < 0 || x > 1100) return;
        if (y < 0 || y > 700) return;
        img.setRGB(x, y, color);
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }
}
