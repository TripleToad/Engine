import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ApplicationRunner extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		ApplicationRunner a = new ApplicationRunner();
		a.run();
	}
	
	@Override
	public void run() {
		
		// Edit the argument string to set the frame title, seen in the top left.
		JFrame j = new JFrame("Insert Title Here");
		
		// Edit these arguments to set a size for the application window.
		j.setSize(1200,1000);
		
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		RawRunner e = new RawRunner();
		
		e.app = true;
		j.add(e);
		e.init();
		j.setLayout( new BorderLayout() );
		j.getContentPane().add(e, BorderLayout.CENTER);
		j.setVisible(true);
	}
	
}
