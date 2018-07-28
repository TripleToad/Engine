import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends Dot {

	BufferedImage sprites;
	Controller c;
	double maxspd = 1.1;
	double gx = 0, gy = 0;
	double sprx = 0, spry = 0, sprs = 32;
	
	
	public Player(double x, double y, double z) {
		super(x, y, z);
	}
	
	@Override
	public void draw(Graphics2D g) {
//		g.draw(sprites.getSubimage(sprx * sprs, spry * sprs));
	}
	
	@Override
	public void update() {
		
		double spdx = 0, spdy = 0;
		
		spdy -= c.get("up")? 		maxspd : 0;
		spdy += c.get("down")? 		maxspd : 0;
		spdx -= c.get("left")? 		maxspd : 0;
		spdx += c.get("right")? 	maxspd : 0;
		
		gx += spdx;
		gy += spdy;
		
		gx *= .7;
		gy *= .7;
		
		x  += spdx + gx;
		lx += spdx + gx;
		y  += spdy + gy;
		ly += spdy + gy;
		
		super.update();
		
	}

}
