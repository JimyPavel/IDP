package classes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Main extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultTableModel	model;			// custom table model
	private JTable 				table;			// table
	private JButton 			signOutB; 		// sign out button
	private JLabel				welcomeMessage; // welcome message label
	
	public Main() {
		init();
	}
	
	public void init() {
		
		// TODO: we create our custom Table Model
		// this personalized model is read-only (we can't edit a cell on double-click)
		model = new DefaultTableModel(0,4);
		
		JComboBox<Object> c = new JComboBox<Object>();
		// TODO: load items in model
		for(int i=0; i<10; i++){
			// service name
			String serviceName = "service name" + i;
			
			// sellers list
			Object[] sellers = new Object[2];
			sellers[0] = "furnizor 1";
			sellers[1] = "furnizor 2";
			
			c = new JComboBox<Object>(sellers);
			
			
			// service status
			String serviceStatus = "INACTIVE";
			
			// service progress bar
			JProgressBar  serviceProgress = new JProgressBar();
			
			// add row in table model
			Object[] rowData = new Object[4];
			rowData[0] = serviceName;
			rowData[1] = c;
			rowData[2] = serviceStatus;
			rowData[3] = serviceProgress;
			model.addRow(rowData);
		}
		
		// initialize the table based on the model
		table = new JTable(model);
		table.setRowHeight(20);
		
		// we render the progress bar
		table.getColumnModel().getColumn(3).setCellRenderer(new ProgressBarRender());
		
		// we render the list of sellers
		table.getColumnModel().getColumn(1).setCellEditor(new ComboBoxPaneledCellEditor(c));
		table.getColumnModel().getColumn(1).setCellRenderer(new ListRender());
		
		table.setCellSelectionEnabled(true);
		
		// top, bottom, center
		JPanel top = new JPanel(new FlowLayout());			// a welcome message	
		JPanel center = new JPanel(new FlowLayout());		// the table
		JPanel bottom = new JPanel(new FlowLayout());		// sign out button
		this.setLayout(new BorderLayout());
		this.add(top,BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(bottom,BorderLayout.SOUTH);
		
		// welcome message
		welcomeMessage = new JLabel("Welcome");
		top.add(welcomeMessage);
		JScrollPane sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(800,300));
		center.add(sp);
		
		// sign out button
		signOutB = new JButton("sign out");
		signOutB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("i'm out!");
			}
		});
		bottom.add(signOutB);
	}

	
	public static void buildGUI() {
		JFrame frame = new JFrame("Swing stuff"); // title
		frame.setContentPane(new Main()); // content: the JPanel above
		frame.setSize(1200, 600); // width / height
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// run on EDT (event-dispatching thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI();
			}
		});

	}

}
