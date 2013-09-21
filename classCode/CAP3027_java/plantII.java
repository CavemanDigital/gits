import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.Line2D;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;

public class plantII{
	private static final int WIDTH = 401;
	private static final int HEIGHT = 401;
	
	//we'll see later this is an "unsafe" way to start a GUI based program and cover how to do it properly
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}
	
	private static void createAndShowGUI(){
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

//############################

class ImageFrame extends JFrame{
	private final JFileChooser chooser;
	private BufferedImage image = null;
	private final int WIDTH;
	private final int HEIGHT;
	
	//============
	//constructor
	
	public ImageFrame(int width, int height){
		//------------
		//setup frame's attributes
		WIDTH = width;
		HEIGHT = height;
		this.setTitle("CAP 3027 2010 - HW06 - David Saraydar");
		this.setSize(width, height);
		
		//add a menu to the frame
		addMenu();
		
		//-------------
		//setup the file shooer dialog
		
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	
	private void addMenu(){
		//--------------
		//setup the frame's menu bar
		
		//=== File Menu
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem plant2Item = new JMenuItem("Plant");
		plant2Item.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event){
					plant2();
				}
			} );
		fileMenu.add(plant2Item);
	
		//---Exit
	
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event){
					System.exit(0);
				}
			});
		
		fileMenu.add(exitItem);
		
		//===attach menu to a menu bar
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void plant2(){
		new Thread(new Runnable(){
			public void run(){
				//-----promt-----
				String result = JOptionPane.showInputDialog("Size:");
					int size = Integer.parseInt(result);
				String results = JOptionPane.showInputDialog("Number of stems:");
					int stems = Integer.parseInt(results);
				String resultp = JOptionPane.showInputDialog("Number of stem steps:");
					int steps = Integer.parseInt(resultp);
				String resultt = JOptionPane.showInputDialog("Transmission probobility probability [0.0,1.0]:");
					double trans = Double.parseDouble(resultt);
				String resultr = JOptionPane.showInputDialog("Maximum rotation increment [0.0,1.0]");
					double rotation = Double.parseDouble(resultr);
				String resultg = JOptionPane.showInputDialog("Growth segment increment");
					double increment = Double.parseDouble(resultg);
				String resultS = JOptionPane.showInputDialog("Start color");
					int intRGBs = (int) Long.parseLong( resultS.substring( 2, resultS.length() ), 16 );
				String resultE = JOptionPane.showInputDialog("End color");
					int intRGBe = (int) Long.parseLong( resultE.substring( 2, resultE.length() ), 16 );
				Color SC = new Color(intRGBs);
				Color EC = new Color(intRGBe);
				int alpha = SC.getAlpha();
				
				int red = SC.getRed();
					System.out.println("red Start: " + red);
				int green = SC.getGreen();
					System.out.println("green Start: " + green);
				int blue = SC.getBlue();
					System.out.println("blue Start: " + blue);
				int red2 = EC.getRed();
					System.out.println("red End: " + red2);	
				int green2 = EC.getGreen();
					System.out.println("green End: " + green2);
				int blue2 = EC.getBlue();
					System.out.println("blue End: " + blue2);
				double deltaRed = (double)(red2-red)/ (double) steps;
				int dRed = (int) Math.round(deltaRed);
					System.out.println("delta red: " + dRed);
				double deltaGreen = (double)(green2-green)/ (double) steps;
				int dGreen = (int) Math.round(deltaGreen);
					System.out.println("delta green: " + dGreen);
				double deltaBlue = (double)(blue2-blue)/ (double) steps;
				int dBlue = (int) Math.round(deltaBlue);
					System.out.println("delta blue: " + dBlue);
				int startR = (int) red;
				int startG = (int) green;
				int startB = (int) blue;
				int rgb = intRGBs;

				//---initialize---
				double xPos = (double) size/2.0;
				double yPos = (double) size/2.0;
				double nextX = xPos;
				double nextY = yPos;
				double theta = Math.PI/2;
				double r = 1.0;
				double reflection = (1.0-trans);
				double direction = 1.0;
				double T = trans;	
				Random rand = new Random();
				BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = (Graphics2D) image.createGraphics();
				RenderingHints hint = new RenderingHints( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
				g2d.setRenderingHints( hint );
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0, 0, size, size);
				Color[] color = new Color[steps];
				for(int c=0; c<steps; c++){
					color[c] = new Color(rgb);
					startR=startR+dRed;
					startG=startG+dGreen;
					startB=startB+dBlue;
					rgb = (alpha<<24)|(startR << 16 ) | (startG << 8 ) | startB;
				}

				//-----draw-----
				//stems per drawing
				float strk = 6.0f;
				for(int stm=0; stm<stems; stm++){
					xPos = (double) size/2.0;
					yPos = (double) size/2.0;
					nextX = xPos;
					nextY = yPos;
					theta = Math.PI/2;
					r = 1.0;
					reflection = (1.0-trans);
					direction = 1.0;
					T = trans;	
					Line2D line = new Line2D.Double(xPos, yPos, nextX, nextY);
					g2d.setColor(color[0]);
					g2d.draw(line);
					//steps per stem
					for(int s=0; s<steps; s++){
						if(rand.nextDouble()>T){
							direction = 1.0;
							T=trans;
						}
						else{
							direction = -1.0;
							T=reflection;
						}
						r = r+increment;
						theta  = theta + (rotation*rand.nextDouble()*direction);
						nextX = xPos + r*Math.cos(theta);
						nextY = yPos + r*Math.sin(theta);
						strk = strk-((6.0f-.5f)/(float)steps);
						BasicStroke stroke = new BasicStroke( strk );
						g2d.setStroke( stroke );		
						line.setLine(xPos, yPos, nextX, nextY);
							//send (Color[] color, int[] stroke, int xPos, int yPos, int xNext, int yNext);
						g2d.setColor(color[s]);
						g2d.draw(line);		
						xPos = nextX;
						yPos = nextY;
					}
					strk = 6.0f;
				}
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						displayBufferedImage(image);
					}
				});
			}	
		}).start();
	}
	
	//-------------------------------------
	//Open a file selected by the user.
	
	private File getFile(){
		File file = null;
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		return file;
	}
	
	//--------------------------------------
	//display specified file in the frame
	
	private void displayFile(File file){
		try{
			displayBufferedImage(ImageIO.read(file));
		}
		catch(IOException exception){
			JOptionPane.showMessageDialog(this, exception);
		}
	}
	
	//---------------------------------------
	//Display BufferedImage
	
	public void displayBufferedImage(BufferedImage image){
		//There are many ways to display a buffered image. this is one:
		
		this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(image))));
		
		//problem: if this method is called more than once, it does NOT reuse the existing JScrollPane, JLabel, or ImageIcon
		
		//JFrames are a type of container. Anytime a container's subcomponents are modified (added to or removed from the container, 
		//or layout-related information changed) after the container has been displayedm one should call the validate() method
		//which causes the conatiner to lay out its subcomponents again.
		
		this.validate();
	}
	
	private BufferedImage makeImage(){
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
	}
}