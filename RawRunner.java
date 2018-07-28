
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RawRunner extends NeoEngine {
	private static final long serialVersionUID = 1L;
	
	BufferedImage deepimage;
	
	int players = 1;
	ArrayList<PlayerView> maps = new ArrayList<>(players);
	
	ArrayList<Dot> dots = new ArrayList<>();
	ArrayList<Tether> tethers = new ArrayList<>();
	
	Map map;
	Shader shader;
	final static int MAPWIDTH = 630, MAPHEIGHT = 300;
//	double offx = 0, offy = 0;
	
	@Override
	public void first(Graphics2D g) {
		
		setDelay(1);
		
		Controller c = new Controller(5);
		c.add("up", 	KeyEvent.VK_W);
		c.add("left", 	KeyEvent.VK_A);
		c.add("down", 	KeyEvent.VK_S);
		c.add("right", 	KeyEvent.VK_D);
		c.add("jump", 	KeyEvent.VK_SPACE);
		controllers.add(c);
		
		Player p = new Player(10, 10, 0);
		p.c = c;
		dots.add(p);
		
//		sprites = readImage("asset/pacman_sprites.png");
		
//		m = new Map(readImage("asset/mapbits.png"));
		
		BufferedImage colorSource = readImage("assets/shader1.png");
		
		shader = new Shader(colorSource);
		
		double res = 9;
		
		map = new Map(
				10, 6  * res, 0, 11,
				11, 10 * res, 0, 22,
				12, 20 * res, -10, 22,
				12, 50 * res, -80, 33,
				13, 10 * res, 0, 33,
				14, 10 * res, 0, 33,
				15, 4 * res, 0, 34
				);
		
		shader.setElevationChannelNum(4);
		
		for(int i = 0; i < players; i++) {
			maps.add(new PlayerView(new BufferedImage(MAPWIDTH / players, MAPHEIGHT, BufferedImage.TYPE_INT_ARGB), Math.random() * 50, Math.random() * 50));
		}
		
		deepimage = new BufferedImage(MAPWIDTH, MAPHEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = deepimage.createGraphics();
		g2.setColor(new Color(colorSource.getRGB(0, colorSource.getHeight() - 1)));
		g2.fillRect(0, 0, deepimage.getWidth(), deepimage.getHeight());
		g2.setColor(g2.getColor().darker());
		for(int i = 0; i < deepimage.getHeight(); i += 2) {
			g2.drawLine(0, i, deepimage.getWidth(), i);
		}
		g2.dispose();
		
		Dot d = new Dot(60, 10, 0);
		d.ax = 0.1;
		d.mass = 2;
		dots.add(d);
		
		Dot lastdot = d;
		for(int i = 0; i < 10; i++) {
			Dot d2 = new Dot(60, 20 + i * 10, 0);
			dots.add(d2);
			tethers.add(new Tether(d2, lastdot));
			lastdot = d2;
		}
		
		lastdot.ax = -0.1;
		lastdot.mass = 2;
		
	}
	
	@Override
	public void loop(Graphics2D g) {
		
//		offx += 0.7;
//		offy += 0.8;
		
//		g.setColor(Color.decode("#418baf"));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		loadMaps();
		
		g.scale(3, 3);
//		g.translate(offx, offy);
		
		g.drawImage(deepimage, 0, 0, null);
		
		for(int i = 0; i < players; i++) {
			g.drawImage(maps.get(i).map, i * MAPWIDTH / players, 0, null);
		}
		
		for(Dot a : dots) {
			a.update();
			g.fill(new Ellipse2D.Double(a.x - 2, a.y - a.z - 2, 4, 4));
			g.fill(new Ellipse2D.Double(a.x - 2, a.y - 1, 4, 2));
		}
		
		for(Tether t : tethers) {
			t.update();
			g.draw(new Line2D.Double(t.a.x, t.a.y, t.b.x, t.b.y));
		}
		
	}
	
	public void loadMaps() {
		for(int i = 0; i < players; i++) {
			loadMap(i);
		}
	}
	
	public void loadMap(int mapindex) {
		shader.reset();
		BufferedImage mapimage = maps.get(mapindex).map;
		double[] chan = new double[map.getNumChannels()];
		for(int i = 0; i < mapimage.getWidth(); i++) {
			for(int j = 0; j < mapimage.getHeight(); j++) {
				map.getPointInChannels(chan, i + /*offx + */ maps.get(mapindex).offx, j + /* offy + */ maps.get(mapindex).offy);
				mapimage.setRGB(i, j, shader.get(chan, i, j));
			}
		}
	}

	@Override
	public void delay(Graphics2D g) {
		shader.tick();
	}
	
	@Override
	public void debug(Graphics2D g) {
		
	}
	
	@Override
	public void keyPressedChild(KeyEvent e) {
		
	}
	
	@Override
	public void keyReleasedChild(KeyEvent e) {
		
	}
	
}

class PlayerView {
	BufferedImage map;
	double offx, offy;
	
	public PlayerView(BufferedImage map, double offx, double offy) {
		this.map = map;
		this.offx = offx;
		this.offy = offy;
	}
}
