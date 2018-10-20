package ui;

import drawables.*;
import utils.Renderer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame implements MouseMotionListener {

    static int FPS = 1000/60;
    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private JPanel panel;
    private Renderer renderer;
    private Polygon polygon = new Polygon();
    private Point point;
    private int coorX, coorY,firstX, firstY;
    private int clickX = 300;
    private int clickY = 300;
    private int count = 5;

    private List<Drawable> drawables;
    private boolean firstClickLine = true;
    private boolean firstLeftClickLine = true;
    private boolean n = true;
    private boolean doubleClick = false;
    private DrawableType type = DrawableType.N_OBJECT;

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
                        }else {

                            drawables.add(new Line(clickX,clickY,e.getX(),e.getY()));
                            firstClickLine = !firstClickLine;
                        }
                    }

                    if (type == DrawableType.DEFIN_NOBJECT){
                        if (firstClickLine){
                            clickX = e.getX();
                            clickY = e.getY();
                            firstClickLine = false;
                        }else {
                            draw();
                            drawables.add(new DefPolygon(clickX,clickY,e.getX(),e.getY(),count));
                            firstClickLine = true;
                        }
                    }

                    if(type == DrawableType.N_OBJECT){
                        //TODO: můj úhelník
                        // znamena, ze se bude klikak a az bude dvojklik tak se spoji posledni s prvni
                        clickX = e.getX();
                        clickY = e.getY();

                        if(n){
                            polygon = new Polygon();
                            firstX = e.getX();
                            firstY = e.getY();
                            point = new Point(clickX,clickY);
                            polygon.addPoint(point);
                            n = !n;
                            firstClickLine = !firstClickLine;
                        } else {
                            Point pos = new Point(clickX,clickY);
                            drawables.add(new Polygon(polygon.addPoint(pos)));
                        }
                    }

                    if(e.getClickCount()==2){
                        System.out.println("b");
                        clickX = e.getX();
                        clickY = e.getY();
                        drawables.add(new Line(firstX,firstY,clickX,clickY));
                        firstClickLine = true;
                        n = true;
                        doubleClick = true;
                    }
                }

                if(e.getButton()== MouseEvent.BUTTON3){
                    if (firstLeftClickLine){
                        clickX = e.getX();
                        clickY = e.getY();
                        firstLeftClickLine = !firstLeftClickLine;
                    }else {
                        drawables.add(new Circle(clickX,clickY,e.getX(),e.getY()));
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
                if (e.getKeyCode() == KeyEvent.VK_3){
                    type = DrawableType.N_OBJECT;
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
        img.getGraphics().fillRect(0,0,img.getWidth(),img.getHeight()); // prideleni pozadi

        if (!firstLeftClickLine){
            renderer.kruznice(clickX,clickY,coorX,coorY);
            renderer.lineDDA(clickX,clickY, coorX, coorY);
        }

        if (type == DrawableType.DEFIN_NOBJECT && !firstClickLine){
            renderer.lineDDA(clickX,clickY, coorX, coorY);
            renderer.polygon(clickX,clickY, coorX, coorY,count);
        } else if (!firstClickLine){
            renderer.lineDDA(clickX,clickY, coorX, coorY);
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
