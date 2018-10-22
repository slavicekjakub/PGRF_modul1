package ui;

import drawables.*;
import drawables.Point;
import drawables.Polygon;
import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame implements MouseMotionListener {

    static int FPS = 1000/60;
    private BufferedImage img;
    static int width = 1200;
    static int height = 800;
    private JPanel panel;
    private Renderer renderer;
    private Polygon polygon = new Polygon();
    private Point point;
    private int coorX, coorY,firstX, firstY;
    private int clickX = 300;
    private int clickY = 300;
    private int count = 5;
    private int color = Color.GREEN.getRGB();

    private List<Drawable> drawables;
    private boolean firstClickLine = true;
    private boolean firstLeftClickLine = true;
    private boolean n = true;
    private boolean doubleClick = false;
    private boolean fillMode = false;
    private DrawableType type = DrawableType.LINE;

    public static void main(String... args) {
        PgrfFrame pgrfFrame = new PgrfFrame();
        pgrfFrame.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pgrfFrame.init(width,height);
    }

    private void init(int width, int height){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width,height);
        setTitle("Modul 1");

        drawables = new ArrayList<>();
        panel = new JPanel();
        add(panel);

        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()== MouseEvent.BUTTON1){
                    if (type == DrawableType.LINE){
                        if (firstClickLine){
                            clickX = e.getX();
                            clickY = e.getY();
                            firstClickLine = !firstClickLine;
                            firstLeftClickLine = true;
                            color = Color.GREEN.getRGB();
                        }else {
                            drawables.add(new Line(clickX,clickY,e.getX(),e.getY(),color));
                            firstClickLine = !firstClickLine;
                        }
                    }

                    if (type == DrawableType.DEFIN_NOBJECT){
                        if (firstClickLine){
                            clickX = e.getX();
                            clickY = e.getY();
                            firstClickLine = false;
                            firstLeftClickLine = true;
                            color = Color.BLUE.getRGB();
                        }else {
                            draw();
                            drawables.add(new DefPolygon(clickX,clickY,e.getX(),e.getY(),count,color));
                            firstClickLine = true;
                        }
                    }

                    if(type == DrawableType.N_OBJECT){
                        //TODO: můj úhelník
                        // znamena, ze se bude klikak a az bude dvojklik tak se spoji posledni s prvni
                        clickX = e.getX();
                        clickY = e.getY();
                        firstLeftClickLine = true;
                        if(n){
                            polygon = new Polygon();
                            firstX = e.getX();
                            firstY = e.getY();
                            point = new Point(clickX,clickY);
                            polygon.addPoint(point);
                            n = !n;
                            firstClickLine = !firstClickLine;
                            color = Color.RED.getRGB();
                        } else {
                            Point pos = new Point(clickX,clickY);
                            drawables.add(new Polygon(polygon.addPoint(pos),color));
                        }
                    }

                    if(e.getClickCount()==2 && type == DrawableType.N_OBJECT){
                        clickX = e.getX();
                        clickY = e.getY();
                        color = Color.RED.getRGB();
                        drawables.add(new Line(firstX,firstY,clickX,clickY,color));
                        firstClickLine = true;
                        n = true;
                        doubleClick = true;
                    }
                }

                if(e.getButton()== MouseEvent.BUTTON3){
                    if (firstLeftClickLine){
                        firstClickLine =  true;
                        clickX = e.getX();
                        clickY = e.getY();
                        firstLeftClickLine = !firstLeftClickLine;
                        color = Color.MAGENTA.getRGB();
                    }else {
                        drawables.add(new Circle(clickX,clickY,e.getX(),e.getY(),color));
                        firstLeftClickLine = !firstLeftClickLine;
                    }
                }

                super.mouseClicked(e);
            }

        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (type == DrawableType.DEFIN_NOBJECT){
                    if (e.getKeyCode() == KeyEvent.VK_UP){
                        //sipka nahoru prida uhel pri vykresleni pravidelneho n-uhelniku
                        count++;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN){
                        //sipka dolu ubere uhel pri vykresleni pravidelneho n-uhelniku
                        if (count >3){
                            count--;
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD1){
                    //cislo 1 vykresluje caru
                    type = DrawableType.LINE;
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD2){
                    //cislo 2 vykresluje pravidelny n-uhelnik
                    type = DrawableType.DEFIN_NOBJECT;
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD3){
                    type = DrawableType.N_OBJECT;
                }
                if (e.getKeyCode() == KeyEvent.VK_F){
                    fillMode = !fillMode;
                }

                super.keyReleased(e);
            }
        });

        setLocationRelativeTo(null);
        renderer = new Renderer(img);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);
        draw();

    }

    private void draw(){
        if (!fillMode){
            img.getGraphics().fillRect(0,0,img.getWidth(),img.getHeight()); // prideleni pozadi
        }


        if (!firstLeftClickLine){
            renderer.kruznice(clickX,clickY,coorX,coorY,color);
            renderer.lineDDA(clickX,clickY, coorX, coorY,color);
        }

        if (type == DrawableType.DEFIN_NOBJECT && !firstClickLine){
            renderer.lineDDA(clickX,clickY, coorX, coorY,color);
            renderer.polygon(clickX,clickY, coorX, coorY,count,color);
        } else if (!firstClickLine){
            renderer.lineDDA(clickX,clickY, coorX, coorY,color);
        }
        for (Drawable drawable : drawables) {
            drawable.draw(renderer);
        }


        panel.getGraphics().drawImage(img, 0,0,img.getWidth(), img.getHeight(), null); // zde ji to vykresli
        panel.paintComponents(getGraphics());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        coorX = e.getX();
        coorY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        coorX = e.getX();
        coorY = e.getY();
    }
}
