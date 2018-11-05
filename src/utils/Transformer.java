package utils;

import solids.Solid;
import transforms.Mat4;
import transforms.Point3D;

import java.awt.image.BufferedImage;

public class Transformer {

    private BufferedImage img;

    private Mat4 model;
    private Mat4 view;
    private Mat4 projection;

    public Transformer(BufferedImage img) {
        this.img = img;
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
        /*TODO: 1) vynásobit body maticí
                2) ořez dle w z bodů
                3) tvorba z vektorů dehomogenizací (Point3D.dehomog())
                4) přepočet souřadnic na výšku/šířku našeho okna (viewport)
                5) Výsledek vykreslit - drawline(..)
        */
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
}
