package ui;

import drawables.*;
import drawables.Polygon;
import solids.Cube;
import solids.Solid;
import transforms.Camera;
import transforms.Mat4PerspRH;
import transforms.Vec3D;
import utils.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static ui.PgrfFrame.FPS;

public class PgrfWireFrame extends JFrame {

    static int FPS = 1000/30;
    static int width = 1200;
    static int height = 800;
    private int beginX, beginY;
    private double moveX,moveY,moveZ;
    private BufferedImage img;
    private JPanel panel;
    private Transformer transformer;
    private List<Solid> solids;
    private Camera camera;

    public static void main(String[] args) {
        PgrfWireFrame pgrfWireFrame = new PgrfWireFrame();
        pgrfWireFrame.init(width,height);
    }

    private void init(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width,height);
        setTitle("Drátový model");

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        solids = new ArrayList<>();
        panel = new JPanel();
        add(panel);

        setLocationRelativeTo(null);
        transformer = new Transformer(img);
        transformer.setProjection(new Mat4PerspRH(1,1,1,100));
        camera = new Camera();

        solids.add(new Cube(3));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                beginX = e.getX();
                beginY = e.getY();
                super.mousePressed(e);
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int finalX = e.getX() - beginX;
                int finalY = e.getY() - beginY;
                if (finalX < -5){
                    camera.addAzimuth(0.001);
                }
                if (finalX > 5){
                    camera.addAzimuth(-0.001);
                }
                if (finalY < -5){
                    camera.addZenith(0.001);
                }
                if (finalY > 5){
                    camera.addZenith(-0.001);
                }

                super.mouseDragged(e);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP: camera.forward(0.1); break;
                    case KeyEvent.VK_DOWN: camera.backward(0.1); break;
                    case KeyEvent.VK_LEFT: camera.left(0.1); break;
                    case KeyEvent.VK_RIGHT: camera.right(0.1); break;
                    //TODO - camera up and down

                    case KeyEvent.VK_A: moveX -= 0.1;break;
                    case KeyEvent.VK_D: moveX += 0.1;break;
                    case KeyEvent.VK_W: moveZ += 0.1;break;
                    case KeyEvent.VK_S: moveZ -= 0.1;break;
                    //TODO - moveY, rotateX, rotateY, scaleX, scaleY,...

                    case KeyEvent.VK_R: resetCamera();break;
                }
                super.keyReleased(e);
            }
        });

        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);
        draw();
    }

    private void resetCamera(){
        //TODO správné hodnoty
        moveX = 0; moveY = 0; moveZ = 0;
        camera = new Camera(new Vec3D(10,5,0),-2.5,-0.1,1.0,true);
    }
    private void draw() {
        img.getGraphics().fillRect(0,0,img.getWidth(),img.getHeight());

        //transformer.setModel(); -- TODO Úloha 3
        transformer.setView(camera.getViewMatrix());

        for (Solid solid : solids){
            transformer.drawWireFrame(solid);
        }

        panel.getGraphics().drawImage(img, 0,0, null); // zde ji to vykresli
        panel.paintComponents(getGraphics());
    }
    /*
     TODO: v main inicializovat PgrfWireFrame metody:
     init (šířka, výška)
     draw();

     fields:
     BufferedImage, List<Solid>, Transformer
     Camera
     */
}
