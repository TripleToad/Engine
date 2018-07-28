
public class Map {
	
	int channelct;
	OpenSimplexNoise[] channels;
	double[][] channelConfig;
	
	/**
	 * Map constructor using presets
	 * presets are comprised of four doubles:
	 * seed, octave size, minimum value, maximum value
	 * @param presets
	 */
	public Map(double ... presets) {
		int parts = (int)(presets.length / 4.0);
//		System.out.println("Parts: " + parts);
		channelct = parts;
		channels = new OpenSimplexNoise[parts];
		channelConfig = new double[parts][3];
		for(int i = 0; i < channels.length; i++) {
			channels[i] = 
					new OpenSimplexNoise(
							(long)presets[i * 4]);
			channelConfig[i] = new double[3];
			channelConfig[i][0] = presets[i * 4 + 1];
			channelConfig[i][1] = presets[i * 4 + 2];
			channelConfig[i][2] = presets[i * 4 + 3];
		}
	}
	
	public int getNumChannels() {
		return channelct;
	}
	
	public double getPoint(double x, double y) {
		double val = 0;
		for(int i = 0; i < channels.length; i++) {
			val += (channels[i].eval(
//					stretch out
					x / channelConfig[i][0], y / channelConfig[i][0]) 
//					scale 0 to 1
					/ 0.8643664404879083 * .5 + .5)
//					scale min to max
					* (channelConfig[i][2] - channelConfig[i][1]) + channelConfig[i][1];
		}
		return val;
	}
	
	public void getPointInChannels(double[] chan, double x, double y) {
		for(int i = 0; i < channels.length; i++) {
			chan[i] = (channels[i].eval(
//					stretch out
					x / channelConfig[i][0], y / channelConfig[i][0]) 
//					scale 0 to 1
					/ 0.8643664404879083 * .5 + .5)
//					scale min to max
					* (channelConfig[i][2] - channelConfig[i][1]) + channelConfig[i][1];
		}
	}
	
}
