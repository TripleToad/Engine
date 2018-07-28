import java.awt.Color;

public class Mover {
	int x, y, tx, ty, dir, spd;
	Color col;
	static Map map;
	
	public static void setMap(Map map) {
		Mover.map = map;
	}
	
	public Mover(int x, int y, int dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Mover(int x, int y) {
		this.x = x;
		this.y = y;
		this.dir = 0xF << (int)(Math.random() * 4);
	}
	
	public Mover(int x, int y, Color col) {
		this.x = x;
		this.y = y;
		this.col = col;
	}
	
	public void move(Map m) {
		
	}
}
