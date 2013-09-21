import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;
import java.util.Random;

public class crystal{
	private static final int WIDTH = 401;
	private static final int HEIGHT = 401;	
	//we'll see later this is an "unsafe" way to start a GUI based program and cover how to do it properly
	
	public static void main(String[] args){
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

//############################

class ImageFrame extends JFrame{
	private final JFileChooser chooser;
	private BufferedImage image = null;

	
	//============
	//constructor
	
	public ImageFrame(int width, int height){
		//------------
		//setup frame's attributes
		this.setTitle("CAP 3027 2010 - HW03 - David Saraydar");
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
		
		JMenuItem crystalItem = new JMenuItem("Crystal Structure");
		crystalItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event){
					crystal();
				}
			} );
		fileMenu.add(crystalItem);
	
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
	
	private void crystal(){
		String type = JOptionPane.showInputDialog("Bounded[0] or Toroid[1]");
		int Type = Integer.parseInt(type);
		String result = JOptionPane.showInputDialog("Size:");
		int span = Integer.parseInt(result);
		String result1 = JOptionPane.showInputDialog("Number of seeds:");
		int seedSize = Integer.parseInt(result1);
		String result2 = JOptionPane.showInputDialog("Number of particles:");
		int size = Integer.parseInt(result2);
		String result3 = JOptionPane.showInputDialog("Number of steps:");
		int steps = Integer.parseInt(result3);
		Random rand = new Random();
		image = new BufferedImage(span, span, BufferedImage.TYPE_INT_ARGB);
		for(int b=0; b<span; b++){
			for(int g=0; g<span; g++){
				image.setRGB(b, g, 0XFFFFFFEE);
			}
		}
		int particleX[] = new int[size];
		int particleY[] = new int[size];
		boolean particleF[] = new boolean[size];
		for(int o=0; o<size; o++){
			particleX[o] = rand.nextInt(span-3)+1;
			particleY[o] = rand.nextInt(span-3)+1;
			particleF[o] = false;
		}
		int seedX[] = new int[seedSize];
		int seedY[] = new int[seedSize];
		for(int n=0; n<seedSize; n++){
			seedX[n] = rand.nextInt(span-3)+1;
			seedY[n] = rand.nextInt(span-3)+1;
			image.setRGB(seedX[n], seedY[n], 0xff000fff);
		}
 		for(int g=0; g<steps; g++){
			for(int i=0; i<size; i++){
				if(particleF[i]==false){
					particleX[i]=particleX[i]+rand.nextInt(2)-1;
					particleY[i]=particleY[i]+rand.nextInt(2)-1;
					if(Type==0){
						if(particleX[i]<0) particleX[i]=1;
						if(particleX[i]>=span) particleX[i]=span-2;
						if(particleY[i]<0) particleY[i]=1;
						if(particleY[i]>=span) particleY[i]=span-2;
					}
					else{
						if(particleX[i]<0) particleX[i]=span-2;
						if(particleX[i]>=span) particleX[i]=1;
						if(particleY[i]<0) particleY[i]=span-2;
						if(particleY[i]>=span) particleY[i]=1;
					}
					for(int l=0; l<size; l++){
						if(particleF[l]==true){
							if((particleX[l]-2<particleX[i])&&(particleX[i]<particleX[l]+2)&&(particleY[l]-2<particleY[i])&&(particleY[i]<particleY[l]+2)){
								image.setRGB(particleX[i], particleY[i], 0xff000000);
								particleF[i]=true;
							}
						}
					} 
					for(int m=0; m<seedSize; m++){
						if((seedX[m]-2<particleX[i])&&(particleX[i]<seedX[m]+2)&&(seedY[m]-2<particleY[i])&&(particleY[i]<seedY[m]+2)){
							image.setRGB(particleX[i], particleY[i], 0xff000000);
							particleF[i]=true;
						}
					}
				}
			}
		}
		displayBufferedImage(image);
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
}