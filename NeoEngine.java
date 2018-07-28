import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

//class is abstract so that it may use abstract methods.
public abstract class NeoEngine extends Applet implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener{
	private static final long serialVersionUID = 1L;

	int fps = 40;
	int timer = 0;
	int counter = 0;
	int camx = 0;
	int camy = 0;
	boolean initialized = false;
	boolean debug = false;
	boolean smooth = true;
	
	boolean app = true;
	
	public static final String ROOT_DIR = Paths.get(".").toAbsolutePath().normalize().toString();
	
	ArrayList<Controller> controllers = new ArrayList<Controller>();
	Point mouse = new Point(0, 0);
	boolean mouse_left, mouse_right, mouse_down;
	
	/** Sets the framerate of the game.<br> The framerate may be in seconds or milliseconds based on the system.
	 * @param n represents an arbitrary wait time, usually around 40-60.*/
	public final void setFramerate(int n){ fps = n; }
	public final int getFramerate(){ return fps; }

	public final void setDelay(int n){ timer=n-1; counter=0; }
	public final int getDelay(){ return timer; }

	public final void setDebug(boolean b){ debug=b; }
	public final boolean getDebug(){ return debug; }

	public final void moveCameraX(int mag){ camx+=mag; }
	public final void moveCameraY(int mag){ camy+=mag; }
	public final void setCameraX(int pos){ camx=pos; }
	public final void setCameraY(int pos){ camy=pos; }

	public final void setSmooth(boolean b){ smooth=b; }
	public final boolean getSmooth(){ return smooth; }
	
	public final void addController(Controller c) { controllers.add(c); }
	
	public boolean getMouseLeft() { return mouse_left; }
	public boolean getMouseRight() { return mouse_right; }

	//final stops children from attempting to override this method.
	@Override
	public final void init(){
		requestFocus();
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
	}
	
	public BufferedImage readImage(String location) {
		try {
			return imageToBufferedImage(ImageIO.read(new File(ROOT_DIR + "/../" + location)));
		} catch (IOException e) {
			System.out.println("Could not read file: " + location);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public final void paint(Graphics go){
		Graphics2D g = (Graphics2D)go;
		if(smooth){ g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); }
		long z = System.currentTimeMillis();
		if(!initialized){ first(g); initialized=true; }
		g.translate(camx,camy);
		loop(g);
		if(counter<timer){ counter++; }else{ counter=0; delay(g); }
		z = System.currentTimeMillis()-z;
		try{
			z = (fps-z>0)? fps-z:0;
			Thread.sleep(z);
		}catch(InterruptedException e){ e.printStackTrace(); }
		repaint();
	}

	//abstraction forces children to override the method.
	public abstract void first(Graphics2D g);
	public abstract void loop(Graphics2D g);
	public abstract void delay(Graphics2D g);
	public abstract void debug(Graphics2D g);

	@Override
	public final void keyPressed(KeyEvent e) {
		update_controllers(e.getKeyCode(), true);
		keyPressedChild(e);
	}
	
	public void keyPressedChild(KeyEvent e) {}
	
	@Override
	public final void keyReleased(KeyEvent e) {
		update_controllers(e.getKeyCode(), false);
		keyReleasedChild(e);
	}
	
	public void keyReleasedChild(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public final void mousePressed(MouseEvent e) {
		mouse_down = true;
		modifyMouse(e, true);
		mousePressedChild(e);
	}
	
	public void mousePressedChild(MouseEvent e) {}

	@Override
	public final void mouseReleased(MouseEvent e) {
		mouse_down = false;
		modifyMouse(e, false);
		mouseReleasedChild(e);
	}
	
	public void mouseReleasedChild(MouseEvent e) {}
	
	private void modifyMouse(MouseEvent e, boolean val) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			mouse_left = val;
		} else if(e.getButton() == MouseEvent.BUTTON2) {
			mouse_right = val;
		}
	}
	
	public void mouseDraggedChild(MouseEvent e) {}

	public void mouseMovedChild(MouseEvent e) {}
	
	@Override
	public final void mouseMoved(MouseEvent e) {
		mouse = e.getPoint();
		mouseMovedChild(e);
	}
	
	@Override
	public final void mouseDragged(MouseEvent e) {
		mouse = e.getPoint();
		mouseDraggedChild(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {}
	
	public final void update_controllers(int key, boolean value) {
		for(Controller c : controllers) {
			c.set(key, value);
		}
	}

	public final BufferedImage getImage(String loc, boolean web, boolean application){
		File f = null;
		URL u = null;
		BufferedImage b = null;
		if(!web){
			if(application) {
				Image img = new ImageIcon(this.getClass().getResource(loc)).getImage();
				b = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				Graphics2D gg = b.createGraphics();
			    gg.drawImage(img, 0, 0, null);
			    gg.dispose();
				/*
				 try {
					//b = ImageIO.read(getClass().getClassLoader().getResource(loc));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/

			}else {
				f = new File(loc);
				try {b = ImageIO.read(f);} catch (IOException e) { System.err.println("Failure to create image from file.\n File type should be .PNG, .JPEG, .JPG, .GIF or .TIFF"); e.printStackTrace();}
			}
		}else{
			try { u = new URL(loc); }catch(MalformedURLException e){ System.err.println("Failure to read image from internet.\n Possibly a bad URL?"); e.printStackTrace(); }
			try {b = ImageIO.read(u);} catch (IOException e) { System.err.println("Failure to create image from internet.\n File type should be .PNG, .JPEG, .JPG, .GIF or .TIFF"); e.printStackTrace();}
		}
		BufferedImage b2 = new BufferedImage(b.getWidth(),b.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics g = b2.createGraphics();
		g.drawImage(b,0,0,null);
		g.dispose();
		return b2;
	}
	
	public void getFilesDeeply(ArrayList<File> files, File directory) {
		if(directory == null || files == null) {
			throw new IllegalArgumentException("The directory and return list cannot by null.");
		}
		
		if(directory.isFile()) {
			files.add(directory);
			return;
		} else {
			File[] fileArray = directory.listFiles();
			if(fileArray != null) {
				for(File file : fileArray) {
					if(file != null) {
						getFilesDeeply(files, file);
					}
				}
			}
		}
	}
	
	public void getFiles(ArrayList<File> files, File directory) {
		if(directory == null || files == null) {
			throw new IllegalArgumentException("The directory and return list cannot by null.");
		}
		
		if(directory.isFile()) {
			files.add(directory);
			return;
		} else {
			File[] fileArray = directory.listFiles();
			if(fileArray != null) {
				for(File file : fileArray) {
					if(file != null && file.isFile()) {
						files.add(file);
					}
				}
			}
		}
	}
	
	public ArrayList<BufferedImage> getImages(File directory, String ... extensions) {
		ArrayList<File> files = new ArrayList<File>();
		getFiles(files, directory);
		
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		for(File file : files) {
			for(String extension : extensions) {
				if(file.getName().endsWith(extension)) {
					images.add(getImage(file.getName(), false, false));
				}
			}
		}
		
		
		return images;
	}
	
	public BufferedReader getReader(File file) {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedWriter getWriter(File file) {
		try {
			return new BufferedWriter(new FileWriter(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final static double getDistance(int a, int b){ return Math.abs(a-b); }
	public final static double getDistance(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(Math.abs(x1-x2),2)+Math.pow(Math.abs(y1-y2),2));
	}
	
	public static BufferedImage imageToBufferedImage(Image b) {
		System.out.println(b.getWidth(null));
		System.out.println(b.getHeight(null));
		BufferedImage res = new BufferedImage(b.getWidth(null), b.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = res.createGraphics();
		g.drawImage(b,0,0,null);
		g.dispose();
		return res;
	}
	
	public static void nameDocsInFile(File dir) {
		if(dir.isFile()) {
			System.out.println("File named " + dir.getName());
		} else {
			System.out.println("Folder named " + dir.getName() + " containing:");
			File[] fileArray = dir.listFiles();
			if(fileArray != null) {
				for(File file : fileArray) {
					if(file != null) {
						if(file.isFile()) {
							System.out.println("File named " + file.getName());
						} else {
							System.out.println("Folder named " + file.getName());
						}
					}
				}
			}
		}
	}

	private static boolean histogram = false;
	public static void setHistographicMode(boolean b) {
		histogram = b;
	}
	
	Image boi;
	Graphics bog;
	@Override
	public final void update(java.awt.Graphics g) {
		//thank you jGuru for this buffering code, which is used here to stop image flickering!
		//Link: http://www.jguru.com/article/client-side/double-buffering.html
//	     boi = createImage(getWidth(),getHeight());
	    if(histogram) {
	    	bog = this.getGraphics();
	    	paint(bog);
	    	g.drawImage(boi,0,0,this);
		} else {
			 boi = createImage(getWidth(),getHeight());
		     bog = boi.getGraphics();
			 bog.setColor(getBackground());
			 bog.fillRect(0,0,getWidth(),getHeight());
			 bog.setColor(getForeground());
			 paint(bog);
			 g.drawImage(boi,0,0,this);
		 }
	}

}