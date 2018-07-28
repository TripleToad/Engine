import java.awt.Graphics2D;

public class Dot {
	double lx, ly, lz, x, y, z, ax, ay, az, waterdamp = .9, landdamp = .7, mass = 1;
	boolean inwater = false, canfloat = true, onscreen = true;
	
	public Dot(double x, double y, double z) {
		this.x = this.lx = x;
		this.y = this.ly = y;
		this.z = this.lz = z;
	}
	
	public void update() {
		double tx = x, ty = y, tz = z;
		x = x + (x - lx) + ax;
		y = y + (y - ly) + ay;
		z = z + (z - lz) + az;
		lx = tx;
		ly = ty;
		lz = tz;
		
//		DAMPENERS
//		
//		if(inwater) {
//			ax *= waterdamp;
//			ay *= waterdamp;
//			az *= waterdamp;
//		} else {
//			ax *= landdamp;
//			ay *= landdamp;
//			az *= landdamp;
//		}
		
	}
	
	public static double dist(Dot a, Dot b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
	}
	
	public static double angle(Dot a, Dot b) {
		return Math.atan2(b.y - a.y, b.x - a.x);
	}

	public static Dot center(Dot a, Dot b) {
		return new Dot((b.x + a.x) * .5, (b.y + a.y) * .5, (b.z + a.z) * .5);
	}

	public void draw(Graphics2D g) {
		
	}
}

class Tether {
	Dot a, b;
	double len, elasticity = 0;
	double min, max;
	
	public Tether(Dot a, Dot b) {
		this.a = a;
		this.b = b;
		this.len = min = max = Dot.dist(a, b);
	}
	
	public Tether(Dot a, Dot b, double min, double max) {
		this(a, b);
		this.min = min;
		this.max = max;
	}
	
	public void update() {
		
		// Find current length and angle to push along separation vector.
		double curlen = Dot.dist(a, b);
		double angle = Dot.angle(a, b);
		double push = curlen - len;
		
		// See if max and min bounds are crossed.
		boolean needspush = false;
		if(push > max) {
			needspush = true;
		} else if(push > -min) {
			needspush = true;
		}
		
		if(needspush) {
			
			// Calculate the mass ratio, so the lighter object move more. Simulates conservation of momentum.
			double ratio = a.mass / b.mass;
			
			// Multiply push factors. This preserves the target length.
			double pusha = .5 / ratio, pushb = .5 * ratio;
			
			// Move to positions. This isn't acceleration because that would be too slow to keep up with movement.
			a.x += push * Math.cos(angle) * pusha;
			a.y += push * Math.sin(angle) * pusha;
			b.x -= push * Math.cos(angle) * pushb;
			b.y -= push * Math.sin(angle) * pushb;
		}
		
	}
}
