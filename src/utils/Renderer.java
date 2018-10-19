package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {

    private int color;
    BufferedImage img;

    public Renderer(BufferedImage img) {
        this.img = img;
        color = Color.GREEN.getRGB();
    }

    public Renderer(BufferedImage img, int color){
        this.img = img;
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private void drawPixel(int x, int y) {
        if (x < 0 || x > 800) return;
        if (y < 0 || y > 600) return;
        img.setRGB(x, y, color);
    }

    public void lineDDA(int x1, int y1, int x2, int y2) { //TODO: Do projektu udelat Bresemham a budou Bonus Body (BB) :P
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

            drawPixel(Math.round(x), Math.round(y));
            x += G;
            y += H;
        }
    }

    public void polygon(int x1, int y1, int x2, int y2, int count) {
        double x0 = x2 - x1;
        double y0 = y2 - y1;
        double circleRaridus = 2 * Math.PI;
        double step = circleRaridus / (double) count;


        for (double i = 0; i < circleRaridus; i += step) {
            //dle rotacni matice
            double x = x0 * Math.cos(step) + y0 * Math.sin(step);
            double y = y0 * Math.cos(step) - x0 * Math.sin(step);
            lineDDA((int) x0 + x1, (int) y0 + y1, (int) x + x1, (int) y + y1);

            // potreba zmenit x0, y0
            x0 = x;
            y0 = y;


        }
    }


    public void Bresenham(int x1, int y1, int x2, int y2) {
        int x, y, p, k1, k2, dy, dx;
        dy = y2 - y1;
        dx = x2 - x1;
        x = x1;
        y = y1;
        p = 2 * dy - dx;
        k1 = 2 * dy;
        k2 = 2 * (dy - dx);
        drawPixel(x, y);

        if (Math.abs(dx) > Math.abs(dy)) {
            if (x1 > x2) {
                int pom = x1;
                x1 = x2;
                x2 = pom;
                pom = y1;
                y1 = y2;
                y2 = pom;
            }
        } else {
            if (y1 > y2) {
                int pom = x1;
                x1 = x2;
                x2 = pom;
                pom = y1;
                y1 = y2;
                y2 = pom;
            }
        }

        while (x < x2) {
            x = x + 1;
            if (p < 0) p = p + k1;
            else {
                p = p + k2;
                y = y + 1;
            }
            drawPixel(x, y);
        }

    }
}
