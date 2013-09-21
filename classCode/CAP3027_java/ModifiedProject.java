import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.imageio.*;
import java.io.*;
import org.jfugue.*;

//MODIFIED since 8:30 friday December 3, 2010 by David Saraydar=>
//E Major scale, Dorian Major Scale
//no bugs when leaves image area, but reamins in GUI area
//when promts for new sound, displays all instruments with their int value
//User Instruction default image "alt" fixed to "ctrl"
//Kalypso file example


class ModifiedProject{
	static private final int WIDTH = 600;
	static private final int HEIGHT = 650;
	
	public static void main (String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI(){
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.validate();
		frame.setVisible(true);
	}
}

class ImageFrame extends JFrame{
	private final int WIDTH;
	private final int HEIGHT;
	private MousePanel mouse;
	
	//============
	//constructor
	
	public ImageFrame(int width, int height){
		//------------
		//setup frame's attributes
		WIDTH = width;
		HEIGHT = height;
		this.setTitle("CAP 3027 2010 - Term Project - David Saraydar ~ Image Chime");
		this.setSize(WIDTH, HEIGHT);
		mouse = new MousePanel();
		mouse.setFocusable(true);
		this.getContentPane().add(mouse, BorderLayout.CENTER);
		
		//add a menu to the frame
		addMenu();
	}
	
	private void addMenu(){
		//--------------
		//setup the frame's menu bar
		
		//=== File Menu
		
		JMenu fileMenu = new JMenu("File");
		JMenu MusicMenu = new JMenu("Music");
		JMenu scaleMenu = new JMenu("Scale");
		
		JMenuItem openItem = new JMenuItem("New Image");
		openItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.newFile();
						}
					}).start();
				}
			} );
		fileMenu.add(openItem);
	
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					System.exit(0);
				}
			});
		
		fileMenu.add(exitItem);
		//________________________________________
		JMenuItem playItem = new JMenuItem("Play");
		playItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.play();
						}
					}).start();
				}
			} );
		MusicMenu.add(playItem);
		
		JMenuItem pauseItem = new JMenuItem("Pause");
		pauseItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.pause();
						}
					}).start();
				}
			} );
		MusicMenu.add(pauseItem);
		
		JMenuItem resumeItem = new JMenuItem("Resume");
		resumeItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.resume();
						}
					}).start();
				}
			} );
		MusicMenu.add(resumeItem);
		
		JMenuItem stopItem = new JMenuItem("Clear");
		stopItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.stop();
						}
					}).start();
				}
			} );
		MusicMenu.add(stopItem);
		
		JMenuItem soundItem = new JMenuItem("New Sound");
		soundItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.sound();
						}
					}).start();
				}
			} );
		scaleMenu.add(soundItem);
		
		JMenuItem cItem = new JMenuItem("C Major");
		cItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.setScale(0);
						}
					}).start();
				}
			} );
		scaleMenu.add(cItem);
		
		JMenuItem eItem = new JMenuItem("E Major");
		eItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.setScale(1);
						}
					}).start();
				}
			} );
		scaleMenu.add(eItem);
		
		JMenuItem dItem = new JMenuItem("Dorian Scale");
		dItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.setScale(2);
						}
					}).start();
				}
			} );
		scaleMenu.add(dItem);
		
		JMenuItem crItem = new JMenuItem("Chromatic Scale");
		crItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					new Thread(new Runnable(){
						public void run(){
							mouse.setScale(3);
						}
					}).start();
				}
			} );
		scaleMenu.add(crItem);
		
		//===attach menu to a menu bar
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(MusicMenu);
		menuBar.add(scaleMenu);	
		this.setJMenuBar(menuBar);
	}
}


class MousePanel extends JPanel{
	private JFileChooser chooser;	private BufferedImage image, img;	private File file;	private Graphics2D g2d;
	private int xPos, yPos, colorX, colorY, z, value;
	private double ds;	private int scale=0;
	private Color color;
	int newGreen, newRed, newBlue, Green, Red, Blue;
	int color_sum_r=0;	int color_sum_g=0;	int color_sum_b=0;
	int color_avg_r=0;	int color_avg_g=0;	int color_avg_b=0;	int total=0;
	boolean press=false;	boolean shft=false;	boolean ctrl=false;	boolean tab=false;
	StringBuilder sb=new StringBuilder();	Pattern song=null;	Player player=null;	
	StringBuilder sb1=new StringBuilder();	Pattern song1=null;	Player player1=null;
		Pattern instrument=new Pattern();
	int once, twice, thrice, drag, sound;
	
	
	public void newFile(){
		player.close();
		player1.close();
		player = new Player();
		player1 = new Player();
		song = new Pattern();
		song1 = new Pattern();
		sb= new StringBuilder();
		sb1= new StringBuilder();
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		file = getFile();
		if(file != null){
			try{
				img = ImageIO.read(file);
			}
			catch(IOException exception){
				JOptionPane.showMessageDialog(this, exception);
			}
		}
		this.image=img;
		g2d = (Graphics2D) image.createGraphics();
		g2d.drawImage(image, 0, 0, 600, 600, null);
		g2d.dispose();	
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		song.add("I[Piano]");
		song1.add("I[Piano]");
		instrument.add("I[Piano]");
	}
	
 	public void setScale(int s){
		scale=s;
	}
	public int C(int q){
		z=q;
		double ds = ((double)z%12)/5;
		if((ds<1)&&(z%2==1)){
			return ++z;
		}
		else if((ds>1)&&(z%2==0)){
			return ++z;
		}
		return z;
	}

	public int E(int q){
		z=q;
		double ds = (double)z%12;
		if(ds==3 || ds==5 || ds==7 || ds==10){
			return ++z;
		}
		return z;
	}
	
	public int D(int q){
		z=q;
		double ds = (double)z%12;
		if(ds%2==1){
			if(ds==1 || ds==11){
				return ++z;
			}
		}
		else if(ds%2==0){
			if(ds==4 || ds==6 || ds==8){
				return ++z;
			}
		}
		return z;
	}
	
	private int readDrag(int xPos, int yPos){		
		color_sum_r=0;
		color_sum_g=0;
		color_sum_b=0;
		total=0;
		for(int r=0; r<10; r++){
			for(double theta=0; theta<2*Math.PI; theta+=.4){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				color_sum_g += color.getGreen();
				color_sum_r += color.getRed();
				color_sum_b += color.getBlue();
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				total++;
				color = new Color(newRed, newGreen, newBlue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		for(int r=9; r>=0; r--){
			for(double theta=0; theta<2*Math.PI; theta+=.4){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				color = new Color(newRed, newGreen, newBlue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		color_avg_r = color_sum_r/total;
		color_avg_g = color_sum_g/total;
		color_avg_b = color_sum_b/total;
		repaint();
		if(scale==0) value = C((int)Math.floor((color_avg_r+color_avg_g+color_avg_b)/6));
		else if(scale==1) value = E((int)Math.floor((color_avg_r+color_avg_g+color_avg_b)/6));
		else if(scale==2) value = D((int)Math.floor((color_avg_r+color_avg_g+color_avg_b)/6));
		else value = (int)Math.floor((color_avg_r+color_avg_g+color_avg_b)/6);
		return value;
	}
	private int readCircle(int xPos, int yPos){		
		color_sum_r=0;
		color_sum_g=0;
		color_sum_b=0;
		total=0;
		//timerOpen(
		for(int r=10; r>0; r--){
			for(double theta=0; theta<2*Math.PI; theta+=.2){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				color_sum_g += color.getGreen();
				color_sum_r += color.getRed();
				color_sum_b += color.getBlue();
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				Green=color.getGreen();
				Red=color.getRed();
				Blue=color.getBlue();
				total++;
				color = new Color(Red, Green, newBlue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		//)
		//timerClose()
		for(int r=9; r>=0; r--){
			for(double theta=0; theta<2*Math.PI; theta+=.2){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				Green=color.getGreen();
				Red=color.getRed();
				Blue=color.getBlue();
				color = new Color(Red, Green, newBlue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		color_avg_r = color_sum_r/total;
		color_avg_g = color_sum_g/total;
		color_avg_b = color_sum_b/total;
		if(scale==0) value = C(color_avg_b/2);
		else if(scale==1) value = E(color_avg_b/2);
		else if(scale==2) value = D(color_avg_b/2);
		else value = color_avg_b/2;
		return value;
	}	
	private int readDouble(int xPos, int yPos){		
		color_sum_r=0;
		color_sum_g=0;
		color_sum_b=0;
		total=0;
		for(int r=10; r<20; r++){
			for(double theta=0; theta<2*Math.PI; theta+=.1){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				color_sum_g += color.getGreen();
				color_sum_r += color.getRed();
				color_sum_b += color.getBlue();
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				total++;
				color = new Color(newRed, Green, Blue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		for(int r=19; r>=10; r--){
			for(double theta=0; theta<2*Math.PI; theta+=.1){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				//maybe delete
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				total++;
				color = new Color(newRed, Green, Blue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		color_avg_r = color_sum_r/total;
		color_avg_g = color_sum_g/total;
		color_avg_b = color_sum_b/total;
		if(scale==0) value = C(color_avg_r/2);
		else if(scale==1) value = E(color_avg_r/2);
		else if(scale==2) value = D(color_avg_r/2);
		else value = color_avg_r/2;
		return value;
	}
	private int readTriple(int xPos, int yPos){		
		color_sum_r=0;
		color_sum_g=0;
		color_sum_b=0;
		total=0;
		for(int r=20; r<30; r++){
			for(double theta=0; theta<2*Math.PI; theta+=.05){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				color_sum_g += color.getGreen();
				color_sum_r += color.getRed();
				color_sum_b += color.getBlue();
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				total++;
				color = new Color(Red, newGreen, Blue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		for(int r=29; r>=20; r--){
			for(double theta=0; theta<2*Math.PI; theta+=.05){
				colorX = (int) Math.floor(xPos + r*Math.cos(theta));
				colorY = (int) Math.floor(yPos + r*Math.sin(theta));
				if ((colorX<1)||(colorY<1)||(colorX>=image.getWidth())||(colorY>=image.getHeight())){
					colorX=xPos;
					colorY=yPos;
				}
				color = new Color(image.getRGB(colorX, colorY));
				newGreen = 255-color.getGreen();
				newRed = 255-color.getRed();
				newBlue=255-color.getBlue();
				total++;
				color = new Color(Red, newGreen, Blue);
				image.setRGB(colorX, colorY, color.getRGB());
			}
			repaint();
		}
		color_avg_r = color_sum_r/total;
		color_avg_g = color_sum_g/total;
		color_avg_b = color_sum_b/total;
		repaint();
		if(scale==0) value = C(color_avg_g/2);
		else if(scale==1) value = E(color_avg_g/2);
		else if(scale==2) value = D(color_avg_g/2);
		else value = color_avg_g/2;
		return value;
	}
	
	private File getFile(){
		File file = null;
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		return file;
	}
	
	public void play(){
		player.play(song);
	}
	public void pause(){
		player.pause();
	}
	public void resume(){
		player.resume();
	}
	public void stop(){
		player.close();
		player = new Player();
		song = new Pattern();
		sb= new StringBuilder();
		song.add("I[Piano]");
		song1.add("I[Piano]");
		instrument.add("I[Piano]");
	}
	public void sound(){
		String result = JOptionPane.showInputDialog("-0:PIANO-  -1:BRIGHT_ACOUSTIC-  -2:ELECTRIC_GRAND-  -3:HONKEY_TONK-  -4:ELECTRIC_PIANO-  -5:ELECTRIC_PIANO2-  -6:HARPISCHORD-  -7:CLAVINET-\n-8:CELESTA-  -9:GLOCKENSPIEL-  -10:MUSIC_BOX-  -11:VIBRAPHONE-  -12:MARIMBA-  -13:XYLOPHONE-  -14:TUBULAR_BELLS-  -15:DULCIMER-\n-16:DRAWBAR_ORGAN-  -17:PERCUSSIVE_ORGAN-  -18:ROCK_ORGAN-  -19:CHURCH_ORGAN-  -20:REED_ORGAN-  -21:ACCORIDAN-  -22:HARMONICA-  -23:TANGO_ACCORDIAN-\n-24:NYLON_STRING_GUITAR-  -25:STEEL_STRING_GUITAR-  -26:ELECTRIC_JAZZ_GUITAR-  -27:ELECTRIC_CLEAN_GUITAR-  -28:ELECTRIC_MUTED_GUITAR-  -29:OVERDRIVEN_GUITAR-\n-30:DISTORTION_GUITAR-  -31:GUITAR_HARMONICS-\n-32:ACOUSTIC_BASS-  -33:ELECTRIC_BASS_FINGER-  -34:ELECTRIC_BASS_PICK-  -35:FRETLESS_BASS-  -36:SLAP_BASS_1-  -37:SLAP_BASS_2-  -38:SYNTH_BASS_1-  -39:SYNTH_BASS_2-\n-40:VIOLIN-  -41:VIOLA-  -42:CELLO-  -43:CONTRABASS-  -44:TREMOLO_STRINGS-  -45:PIZZICATO_STRINGS-  -46:ORCHESTRAL_STRINGS-  -47:TIMPANI-\n-48:STRING_ENSEMBLE_1-  -49:STRING_ENSEMBLE_2-  -50:SYNTH_STRINGS_1-  -51:SYNTH_STRINGS_2-  -52:CHOIR_AAHS-  -53:VOICE_OOHS-  -54:SYNTH_VOICE-  -55:ORCHESTRA_HIT-\n-56:TRUMPET-  -57:TROMBONE-  -58:TUBA-  -59:MUTED_TRUMPET-  -60:FRENCH_HORN-  -61:BRASS_SECTION-  -62:SYNTHBRASS_1-  -63:SYNTHBRASS_2-\n-64:SOPRANO_SAX-  -65:ALTO_SAX-  -66:TENOR_SAX-  -67:BARITONE_SAX-  -68:OBOE-  -69:ENGLISH_HORN-  -70:BASSOON-  -71:CLARINET-\n-72:PICCOLO-  -73:FLUTE-  -74:RECORDER-  -75:PAN_FLUTE-  -76:BLOWN_BOTTLE-  -77:SKAKUHACHI-  -78:WHISTLE-  -79:OCARINA-\n-80:SQUARE-  -81:SAWTOOTH-  -82:CALLIOPE-  -83:CHIFF-  -84:CHARANG-  -85:VOICE-  -86:FIFTHS-  -87:BASSLEAD-\n-88:NEW_AGE-  -89:WARM-  -90:POYSYNTH-  -91:CHOIR-  -92:BOWED-  -93:METALLIC-  -94:HALO-  -95:SWEEP-\n-96:RAIN-  -97:SOUNDTRACK-  -98:CRYSTAL-  -99:ATMOSPHERE-  -100:BRIGHTNESS-  -101:GOBLINS-  -102:ECHOES-  -103:SCI-FI-\n-104:SITAR-  -105:BANJO-  -106:SHAMISEN-  -107:KOTO-  -108:KALIMBA-  -109:BAGPIPE-  -110:FIDDLE-  -111:SHANAI-\n-112:TINKLE_BELL-  -113:AGOGO-  -114:STEEL_DRUMS-  -115:WOODBLOCK-  -116:TAIKO_DRUM-  -117:MELODIC_TOM-  -118:SYNTH_DRUM-  -119:REVERSE_CYMBAL-\n-120:GUITAR_FRET_NOISE-  -121:BREATH_NOISE-  -122:SEASHORE-  -123:BIRD_TWEET-  -124:TELEPHONE_RING-  -125:HELICOPTER-  -126:APPLAUSE-  -127:GUNSHOT-\n\nEnter 0-127:");
		sound = Integer.parseInt(result);
		if(value<128 && value>=0){
			song.add(" I"+sound);
			song1.add(" I"+sound);
			instrument.add(" I"+sound);
		}
	}
	
	public MousePanel(){
		player = new Player();
		player1 = new Player();
		song = new Pattern();
		song1 = new Pattern();
		song.add("I[Piano]");
		song1.add("I[Piano]");
		instrument.add("I[Piano]");
		File file = new File("ImageChime.jpg");
		if(file != null){
			try{
				img = ImageIO.read(file);
			}
			catch(IOException exception){
				JOptionPane.showMessageDialog(this, exception);
			}
		}
		image=img;
		g2d = (Graphics2D) image.createGraphics();
		g2d.drawImage(image, 0, 0, 600, 600, null);
		g2d.dispose();	
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		
 		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==16){
					shft=true;
				}
				else if(e.getKeyCode()==17){
					ctrl=true;
				}
				else if(e.getKeyCode()==9){
					tab=true;
				}
			}
			public void keyReleased(KeyEvent e){
				shft=false;
				ctrl=false;
				tab=false;
			}
		});
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent event){
				if(tab==false) press=true;
			}
			public void mouseReleased(MouseEvent event){
				press=false;
			}
			public void mouseExited(MouseEvent event){
				press=false;
			}
			public void mouseClicked(MouseEvent event){
				int x = event.getX();
				int y = event.getY();
				if(event.getClickCount()==3){
					player1 = new Player();
					sb.append(" [");
					thrice = readTriple(x, y);
					sb.append(thrice);
					sb.append("]w");
					song.add(sb.toString());
					sb1=new StringBuilder();
					sb1.append(" [" + thrice + "]w");
					song1=new Pattern();
					song1.add("I"+sound);
					song1.add(sb.toString());
					sb.delete(0, sb.length());
					player1.play(song1);
					player1.close();
				}
				else if(event.getClickCount()==2){
					player1 = new Player();
					sb.append(" [");
					twice = readDouble(x, y);
					sb.append(twice);
					sb.append("]q");
					song.add(sb.toString());
					sb1=new StringBuilder();
					sb1.append(" [" + twice + "]q");
					song1=new Pattern();
					song1.add("I"+sound);
					song1.add(sb.toString());
					sb.delete(0, sb.length());
					player1.play(song1);
					player1.close();
				}
				else{
					if(shft){
						player1 = new Player();
						sb.append(" [");
						twice = readDouble(x, y);
						sb.append(twice);
						sb.append("]q");
						song.add(sb.toString());
						sb1=new StringBuilder();
						sb1.append(" [" + twice + "]q");
						song1=new Pattern();
						song1.add("I"+sound);
						song1.add(sb.toString());
						sb.delete(0, sb.length());
						player1.play(song1);
						player1.close();
					}
					else if(ctrl){
						player1 = new Player();
						sb.append(" [");
						thrice = readTriple(x, y);
						sb.append(thrice);
						sb.append("]w");
						song.add(sb.toString());
						sb1=new StringBuilder();
						sb1.append(" [" + thrice + "]w");
						song1=new Pattern();
						song1.add("I"+sound);
						song1.add(sb.toString());
						sb.delete(0, sb.length());
						player1.play(song1);
						player1.close();
					}
					else if(tab){
						player1 = new Player();
						sb.append(" [");
						once = readDrag(x, y);
						sb.append(once);
						sb.append("]s");
						song.add(sb.toString());
						sb1=new StringBuilder();
						sb1.append(" [" + once + "]s");
						song1=new Pattern();
						song1.add("I"+sound);
						song1.add(sb.toString());
						sb.delete(0, sb.length());
						player1.play(song1);
						player1.close();
					}
					else{
						player1 = new Player();
						sb.append(" [");
						once = readCircle(x, y);
						sb.append(once);
						sb.append("]s");
						song.add(sb.toString());
						sb1=new StringBuilder();
						sb1.append(" [" + once + "]s");
						song1=new Pattern();
						song1.add("I"+sound);
						song1.add(sb.toString());
						sb.delete(0, sb.length());
						player1.play(song1);
						player1.close();
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseAdapter(){
			public void mouseMoved(MouseEvent event){
				Point point = event.getPoint();
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			public void mouseDragged(MouseEvent event){
				if(press){
					if(!tab){
						int m = event.getX();
						int n = event.getY();
						player1 = new Player();
						sb.append(" [");
						drag = readDrag(m, n);
						sb.append(drag);
						sb.append("]t");
						song.add(sb.toString());
						sb1=new StringBuilder();
						sb1.append(" [" + drag + "]t");
						song1=new Pattern();
						song1.add("I"+sound);
						song1.add(sb.toString());
						sb.delete(0, sb.length());
						player1.play(song1);
						player1.close();
					}
				}
			}
		});
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}