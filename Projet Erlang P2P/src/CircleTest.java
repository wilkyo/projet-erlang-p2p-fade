package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class CircleTest extends JPanel {

    private static final int SIZE = 512;
    private static final double NB_PROC=2^160-2;
    private int a = SIZE / 2;
    private int b = a;
    private int r = 4 * SIZE / 5;
    private int n;
    private Hashtable<Integer,String> nodeList=new Hashtable<Integer,String>();
    private JLabel label;
    private JPanel topPan;
    private JPanel drawerPan;
    Random rand=new Random();
    /** @param n  the desired number of circles. */
    public CircleTest(int n) {
        super(true);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        this.n = n;
        label=new JLabel("etiquette");
        topPan=new JPanel();
        topPan.setBorder(new TitledBorder("Infos"));
        drawerPan=new JPanel();
        drawerPan.setBorder(new TitledBorder("Graphic"));
        this.dispLabel();
        this.add(topPan,BorderLayout.NORTH);
        this.add(drawerPan,BorderLayout.SOUTH);
        
    }
    
    public void addNode(){
    	nodeList.put(0, "zou");
    	nodeList.put(1, "alice");
    	nodeList.put(2, "dave");
    	nodeList.put(3, "eve");
    	nodeList.put(4, "charlie");
    	System.out.println("Value at 4 ,"+NB_PROC+" "+nodeList.get(4));
    }

    
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
    	
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //traçage cercle noire
        g2d.setColor(Color.black);
        a = getWidth() / 2;
        b = getHeight() / 2;
        int m = Math.min(a, b);
        r = 4 * m / 5;
        int r2 = Math.abs(m - r) / 2;
        g2d.drawOval(a - r, b - r, 2*r, 2 * r); //(wx,wy,dw,dy)
        
        //traçage cercle rouge
        g2d.setColor(Color.red);      
        
        for (int i = 0; i < n; i++) {
        	String identifiant= nodeList.get(i);
        	int j=rand.nextInt(100);
            double t = 2 * Math.PI * j / 10;               //NB_PROC; //alpha=(2pi*(val/2¹⁶⁰-2))
            int x = (int) Math.round(a + r * Math.cos(t));
            int y = (int) Math.round(b + r * Math.sin(t));
            g2d.fillOval(x - r2, y - r2, 2 * r2, 2 * r2);            
        }
        
        //taçage des cercles verts
        g2d.setColor(Color.green);  
        
        for (int i = 0; i < n; i++) {
        	String identifiant= nodeList.get(i);
        	
        	int j=rand.nextInt(100);
        	System.out.println(Math.abs(j));
            double t = 2 * Math.PI * j / 10;               //NB_PROC; //alpha=(2pi*(val/2¹⁶⁰-2))
            int x = (int) Math.round(a + r * Math.cos(t));
            int y = (int) Math.round(b + r * Math.sin(t));
           
            g2d.fillRect(x-r2/2, y-r2/2, 20, 20);
        }
        
    }
    
    public void dispLabel(){
    	Random rand=new Random();
    	
    	//System.out.println(Math.abs(j));
                    //NB_PROC; //alpha=(2pi*(val/2¹⁶⁰-2))
        for (int i = 0; i < n; i++) {
        	//String identifiant= nodeList.get(i);
        	int j=rand.nextInt(100);
            double t = 2 * Math.PI * j / 10;               //NB_PROC; //alpha=(2pi*(val/2¹⁶⁰-2))
            int x = (int) Math.round(a + r * Math.cos(t));
            int y = (int) Math.round(b + r * Math.sin(t));
            label.setBounds(x+100, y+100, 20, 20);
            this.add(label);
        }
        
        

    }

    private static void create() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new CircleTest(4));
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
    	CircleTest c=new CircleTest(2);
    	c.addNode();
        EventQueue.invokeLater(new Runnable() {       	
        	
            @Override
            public void run() {
                create();
            }
        });
    }
    
    public class Node{
    	
    }
}