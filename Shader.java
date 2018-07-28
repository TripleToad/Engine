
import java.awt.image.BufferedImage;
import java.util.Random;

public class Shader {
	
	BufferedImage shaderSource;
	int elevationChannels;
	double tick;
	static double tidespeed = 0.5;
	
	Random r;
	
	public Shader(BufferedImage shaderSource) {
		this.shaderSource = shaderSource;
	}
	
	public void setElevationChannelNum(int num) {
		elevationChannels = num;
	}
	
	public void tick() {
		tick += tidespeed;
	}
	
	public void reset() {
		r = new Random((long)tick);
	}
	
	public int get(double[] chan, double x, double y) {
		
		double elev = 0, humid = 0;
		for(int i = 0; i < chan.length; i++) {
			if(i < elevationChannels) {
				elev += chan[i];
			} else {
				humid += chan[i];
			}
		}
		
		if(elev < 0) {
			return 0x418baf;
		} else if(shaderSource.getHeight() - elev > 68) {
			humid = tick + r.nextDouble() * 8 + x * .2 + y * .1;
		}
		
		if(humid < 0) {
			return 0xFF0000;
		}
		
		humid += r.nextDouble() * .5;
		humid %= shaderSource.getWidth();
		
//		System.out.println("Humidity (x): " + humid + ", elevation (y): " + elev);
		return shaderSource.getRGB((int)(humid), (int)(shaderSource.getHeight() - elev));
	}
}
