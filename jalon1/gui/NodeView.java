package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


/**
 * @author Zo
 * 
 */

public class NodeView extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 512;
    private static final double NB_PROC=2^160-2;
    private int a = SIZE / 2;
    private int b = a;
    private int r = 4 * SIZE / 5;
    private int n;
    private Hashtable<Integer,String> nodeList=new Hashtable<Integer,String>();
    private JPanel topPan;
    private JPanel drawerPan;
    Random rand=new Random();
    public List<Noeuds> liste;
    /** @param n  the desired number of circles. 
     * @throws IOException */
    public NodeView(ErlangNode erl) throws IOException {
        super(true);
        
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        //this.n = n;
        topPan=new JPanel();
        topPan.setBorder(new TitledBorder("Infos"));
        drawerPan=new JPanel();
        drawerPan.setBorder(new TitledBorder("Graphic"));
        addNode();
        this.add(topPan,BorderLayout.NORTH);
        this.add(drawerPan,BorderLayout.SOUTH);
        liste=ErlangNode.getList();
        n=liste.size();
        JLabel nombreProc=new JLabel("Nombre de Processus :"+n);
        topPan.add(nombreProc);
    }
    
    public void addNode(){
    	nodeList.put(0, "zou");
    	nodeList.put(1, "alice");
    	nodeList.put(2, "dave");
    	nodeList.put(3, "eve");
    	nodeList.put(4, "charlie");
    	//System.out.println("Value at 4 ,"+NB_PROC+" "+nodeList.get(4));
    }

    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    	
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        //traçage cercle noire
        g2d.setColor(Color.black);
        a = getWidth() / 2;
        b = getHeight() / 2;
        int m = Math.min(a, b);
        r = 4 * m / 5;
        int r2 = Math.abs(m - r) / 2;
        g2d.drawOval(a - r, b -r, 2*r, 2*r); //(wx,wy,dw,dy)
        
        //traçage cercle rouge
        g2d.setColor(Color.red);      
        
        for (int i = 0; i < n; i++) {
        	//String identifiant= nodeList.get(i);
        	//int j=rand.nextInt(100);
        	Noeuds noeud=liste.get(i);
        	long j=noeud.getIdHache();
        	String identifiant=noeud.getNode_name();
        	System.out.println(Math.abs(j)+" "+identifiant);
            double angle = 2 * Math.PI * Math.abs(j) / NB_PROC;               //j: id haché du process//NB_PROC; //alpha=(2pi*(val/2¹⁶⁰-2))
            this.dispProcess(g, g2d,r2,  angle, identifiant); 
            
        }
        
        
        g2d.setColor(Color.blue);
        
        for (int i = 0; i < n; i++) {
        	String identifiant= nodeList.get(i);        	
        	int j=rand.nextInt(1000000000);
        	//System.out.println(Math.abs(j)+" "+identifiant);
            double angle = 2 * Math.PI * j / NB_PROC;               //NB_PROC; //alpha=(2pi*(val/2¹⁶⁰-2))
            this.dispData(g, g2d, r2,  angle,identifiant);
            
        }
        
    }
    
    private void dispProcess(Graphics graphic,Graphics2D graphic2D,int radius,double t,String idProcess){
    	int x = (int) Math.round(a + r * Math.cos(t));
        int y = (int) Math.round(b + r * Math.sin(t));
        graphic2D.fillOval(x-radius/2, y-radius/2, 30, 30);
        AffineTransform aff = new AffineTransform();
        aff.translate(x - radius, y - 1.5*radius);            
        graphic2D.drawImage(createStringImage(graphic, idProcess), aff, this);
    }
    
    private void dispData(Graphics graphic,Graphics2D graphic2D,int radius,double t,String idProcess){
    	int x = (int) Math.round(a + r * Math.cos(t));
        int y = (int) Math.round(b + r * Math.sin(t));
        graphic2D.fillRect(x-radius/2, y-radius/2, 15, 15);
        AffineTransform aff2 = new AffineTransform();
        aff2.translate(x-radius/2, y-radius); 
        graphic2D.drawImage(createStringImage(graphic, "Data"), aff2, this);
    }
    

   /* private static void create() throws IOException {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new NodeView()); // n=5 affichage de n process
        f.pack();
        f.setVisible(true);
    }*/
    
    
    public BufferedImage createStringImage(Graphics g, String s) {
        int w = g.getFontMetrics().stringWidth(s) + 5;
        int h = g.getFontMetrics().getHeight();

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        imageGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        imageGraphics.setColor(Color.BLACK);
        imageGraphics.setFont(g.getFont());
        imageGraphics.drawString(s, 0, h - g.getFontMetrics().getDescent());
        imageGraphics.dispose();

        return image;
    }

    /*public static void main(String[] args) throws IOException {
    	//NodeView c=new NodeView();
    	//c.addNode();
        EventQueue.invokeLater(new Runnable() {       	
        	
            @Override
            public void run() {
                try {
					create();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }*/
    
    
    
    
    
}