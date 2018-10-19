package ui;

import drawables.DefPolygon;
import drawables.Drawable;
import drawables.DrawableType;
import drawables.Line;
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
    private JPanel panel; // musime pridat panel, protoze to nevykreslovalo
    private Renderer renderer;
    private int coorX, coorY,coorX2, coorY2;
    private int clickX = 300;
    private int clickY = 300;
    private int count = 5; //nesmi klesnout pod 3!!! ukol v modelu

    private List<Drawable> drawables;
    private boolean firstClickLine = true;
    private boolean secondClickLine, thirdClickLine = false;
    private DrawableType type = DrawableType.DEFIN_NOBJECT;

    public static void main(String... args) {
        PgrfFrame pgrfFrame = new PgrfFrame();
        pgrfFrame.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pgrfFrame.init(width,height);
    }

    // Inicializace vykresleni
    private void init(int width, int height){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width,height);
        setTitle("Počítačová grafika");

        drawables = new ArrayList<>();

        panel = new JPanel();
        add(panel);


        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (type == DrawableType.LINE){
                    //zadavani usecky
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
                        System.out.println("prvni");
                        clickX = e.getX();
                        clickY = e.getY();
                        firstClickLine = false;
                        secondClickLine = true;
                    }else if (secondClickLine){
                        System.out.println("druhy");
                        secondClickLine = false;
                        firstClickLine = true;
                        coorX2 = e.getX();
                        coorY2 = e.getY();
                        draw();
                        drawables.add(new DefPolygon(clickX,clickY,coorX2,coorY2,count));
                    }
                }

                if(type == DrawableType.N_OBJECT){
                    //TODO: můj úhelník
                    // znamena, ze se bude klikak a az bude dvojklik tak se spoji posledni s prvni
                        if (firstClickLine){
                            clickX = e.getX();
                            clickY = e.getY();
                        }else{
                            drawables.add(new Line(clickX,clickY,e.getX(),e.getY()));
                        }

                }
                super.mouseClicked(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (secondClickLine){
                    if (e.getKeyCode() == KeyEvent.VK_UP){
                        //sipka nahoru
                        count++;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN){
                        if (count >3){
                            count--;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_NUMPAD1){
                        type = DrawableType.LINE;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_NUMPAD2){
                        type = DrawableType.DEFIN_NOBJECT;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_3){
                        type = DrawableType.N_OBJECT;
                    }

                }

                if (e.getKeyCode() == KeyEvent.VK_ADD){
                    // je plus na numericke klavesnici a SUBTRACT je -
                }
                super.keyReleased(e);
            }
        });

        setLocationRelativeTo(null);

        renderer = new Renderer(img);

        Timer timer = new Timer(); // timer pro obnoveni toho vykresleni a ted uz to funguje
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);
        draw();

    }

    // vykresleni
    private void draw(){
        img.getGraphics().fillRect(0,0,img.getWidth(),img.getHeight()); // prideleni pozadi


        //Vykreslovalo polygon
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
        coorX = e.getX(); // zjisti kde byla naposledy mys a ulozi jeji misto (pixely)
        coorY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        coorX = e.getX(); // zjisti kde byla naposledy mys a ulozi jeji misto (pixely)
        coorY = e.getY();
        if (type == DrawableType.LINE) {
            if (!firstClickLine) {
                for (int i = 0; i < 3000; i++) {
                    renderer.lineDDA(clickX, clickY, coorX, coorY);
                }
            }
        }
    }
}
