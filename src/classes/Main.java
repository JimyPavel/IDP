package classes;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// we create a mediator
		final Mediator m = new Mediator();
		new Web(m);
		new Network(m);
		// run on EDT (event-dispatching thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI(m);
			}
		});

	}
	
	public static void buildGUI(Mediator m) {
		JFrame frame = new JFrame("Decen Team"); // title
		frame.setContentPane(new Gui(m)); // content: the JPanel above
		frame.setSize(1200, 600); // width / height
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}
	
	

}
