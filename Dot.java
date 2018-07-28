import java.awt.Graphics2D;

/**
* Physics-based particle objects called Dots, using verlet integration.
*/
public class Dot {
	double lx, ly, lz, x, y, z, ax, ay, az, waterdamp = .9, landdamp = .7, mass = 1;
	boolean inwater = false, canfloat = true, onscreen = true;
	
	/**
	* Create a dot object at the specified location.
	*/
	public Dot(double x, double y, double z) {
		this.x = this.lx = x;
		this.y = this.ly = y;
		this.z = this.lz = z;
	}
	
	/**
	* Update the position of this dot
	*/
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
	
	/**
	* Get the distance between two dots. <br>
	* Implemented using the pythagorean theorem, a^2 + b^2 = c^2.
	@param a the first dot
	@param b the second dot
	@return the distance as a double
	*/
	public static double dist(Dot a, Dot b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
	}
	
	/**
	* Get the angle between two dots, from a to b.
	* @param a the first dot
	* @param b the second dot
	* @return the angle in radians, from -pi/2 to pi/2
	*/
	public static double angle(Dot a, Dot b) {
		return Math.atan2(b.y - a.y, b.x - a.x);
	}

	/**
	* Get the point between the two dots as a Dot object.
	* @param a the first dot
	* @param b the second dot
	* @return a new Dot object between the two parameter dot objects.
	*/
	public static Dot center(Dot a, Dot b) {
		return new Dot((b.x + a.x) * .5, (b.y + a.y) * .5, (b.z + a.z) * .5);
	}

	public void draw(Graphics2D g) {
		
	}
}

/**
* A tether for connecting Dot objects and maintaining their relative distances.
*/
class Tether {
	Dot a, b;
	double len, elasticity = 0;
	double min, max;
	
	/**
	* Create a tether object, restricting the relative distance between these dots <br>
	* to remain the value it currently is.
	* @param a the first dot
	* @param b the second dot
	*/
	public Tether(Dot a, Dot b) {
		this.a = a;
		this.b = b;
		this.len = min = max = Dot.dist(a, b);
	}
	
	/**
	* Create a tether object, restricting the relative distance between the provided <br>
	* two values.
	* @param a the first dot
	* @param b the second dot
	* @param min the minimum distance between these dots
	* @param max the maximum distance between these dots
	*/
	public Tether(Dot a, Dot b, double min, double max) {
		this(a, b);
		this.min = min;
		this.max = max;
	}
	
	/**
	* Update the tether, forcing the dots back to their relative distance. <br>
	* The amount a particular dot moves is based on the ratio between it's mass and <br>
	* the mass of the other dot. A heavier dot moves less to compensate for distance <br>
	* discrepancies than a lighter one.
	*/
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
