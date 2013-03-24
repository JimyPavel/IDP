package classes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import interfaces.IMediator;
import interfaces.IGui;

public class Gui extends JPanel implements IGui{

	private static final long 		serialVersionUID = 1L;
	private DefaultTableModel		model;			// custom table model
	private JTable 					table;			// table
	private JButton 				signOutB; 		// sign out button
	private JLabel					welcomeMessage; // welcome message label
	protected JTextField 			username;
	protected JPasswordField 		pass;
	
	protected String 				userType;
	protected String 				userName;
	protected IMediator 			mediator;
	public static ArrayList<Offer>  mainOffers;
	public static ArrayList<Product> mainProducts;
	
	
	public Gui(IMediator mediator)
	{
		this.mediator = mediator;		
		this.mediator.registerGui(this);
		initSignIn();
	}
		
	// init the components for sign in form
	public void initSignIn(){
		this.removeAll();
		
		// top, bottom, center
		JPanel top = new JPanel(new FlowLayout());			// a welcome message
		JPanel center = new JPanel();
		GridLayout gl = new GridLayout(3,1);
		gl.setVgap(50);
		JPanel centerGrid = new JPanel(gl);				    // the sign in form
		this.setLayout(new BorderLayout());
		this.add(top,BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		
		// components
		JButton but = new JButton("sign in");
		// action listener
		but.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// we check the username and the password
				// if these are correct , we can load the content
				String userText = ((JTextField)username).getText();
				char[] passChar = ((JPasswordField)pass).getPassword();
				String passText = "";
				for(int i=0 ; i < passChar.length; i++){
					passText += passChar[i];
				}
				
				userType = mediator.signIn(userText, passText);
				if(userType != null){
					userName = userText;
					initContent();
				}
				// else we change the message
				else{
					welcomeMessage.setText("username or password are wrong");
				}
				
			}
		});
		
		username = new JTextField("username");
		username.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				JTextField tf = (JTextField)arg0.getSource();
				if(tf.getText().equals("")){
					tf.setText("username");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				
				JTextField tf = (JTextField)arg0.getSource();
				tf.setText("");
			}
		});
		
		pass = new JPasswordField("password");
		pass.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				JTextField tf = (JTextField)e.getSource();
				if(tf.getText().equals("")){
					tf.setText("password");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				JTextField tf = (JTextField)e.getSource();
				tf.setText("");
				
			}
		});
		
		// adding components in JPanel
		welcomeMessage = new JLabel("Welcome, please sign in");
		top.add(welcomeMessage);
		centerGrid.add(username);
		centerGrid.add(pass);
		centerGrid.add(but);
		centerGrid.setPreferredSize(new Dimension(200,300));
		centerGrid.setBorder(new EmptyBorder(50,0,50,0));
		center.setPreferredSize(new Dimension(500,500));
		center.add(centerGrid);
		
		this.revalidate();
		this.repaint();
	}
	
	
	// method for loading the content panel
	public void initContent() {
		
		this.removeAll();
		// TODO: we create our custom Table Model
		// this personalized model is read-only (we can't edit a cell on double-click)
		model = new DefaultTableModel(0,4);
		
		// we save the comboboxes in order to add an editor for each row
		ArrayList<JComboBox> al = new ArrayList<JComboBox>();
		// we take the products from the mediator
		ArrayList<Product> products = mediator.loadProducts(userName, userType);
		
		if(products != null){
			// we save the products
			mainProducts = products;
			
			// for each products we create an entry in table
			for(int i=0; i< products.size(); i++){
				
				// service name
				String serviceName = products.get(i).getName();
				
				// sellers list (service is inactive)
				Object[] sellers = null;
				
				String offerValue = "null";
				JComboBox comboBox = new JComboBox();
				al.add(comboBox);
				
				// this is an active offer then we create the comboBox
				/*if(sellers != null){
					comboBox = new JComboBox(sellers);
					comboBox.setName("combo"+i);
					comboBox.addComponentListener(new ComponentAdapter() {
					      public void componentShown(ComponentEvent e) {
					        final JComponent c = (JComponent) e.getSource();
					        SwingUtilities.invokeLater(new Runnable() {
					          public void run() {
					            c.requestFocus();
					            System.out.println(c);
					            if (c instanceof JComboBox) {
					              System.out.println("a");
					            }
					          }
					        });
					      }
					    });
					
					// action listener for changing item
					comboBox.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							
							// we take the name of the comboBox
							// it's like "combo0" ,where 0 is the row number in table
							String comboName = (String)((JComboBox)arg0.getSource()).getName();
							int rowNo = Integer.parseInt(comboName.charAt(comboName.length()-1) +"");
							
							String sellerName =(String)((JComboBox)arg0.getSource()).getSelectedItem();
							String productName = mainProducts.get(rowNo);
							String tipText = getValue(mainOffers, sellerName, productName);
							System.out.println(tipText);
							((JComboBox)arg0.getSource()).setToolTipText(tipText);
							((JComboBox)arg0.getSource()).grabFocus();
						}
					});
					
					String selectedSeller = (String)comboBox.getSelectedItem();
					//offerValue = getValue(offers, selectedSeller, serviceName);
					comboBox.setToolTipText(offerValue);
				}*/
				
				
				// service status
				String serviceStatus = "INACTIVE";
				
				// service progress bar
				JProgressBar  serviceProgress = new JProgressBar();
				
				// we set the status
				if(offerValue.equals("no_offer")){
					serviceStatus = "NO OFFER";
				}
				else if(!offerValue.equals("null")){
					serviceStatus = "OFFER MADE";
				}
				// if the status is inactive, we make inactive the combobox
				// and the progressbar
				else{
					comboBox.setEnabled(false);
					serviceProgress.setEnabled(false);
				}
				
				
				// add row in table model
				Object[] rowData = new Object[4];
				rowData[0] = serviceName;
				rowData[1] = comboBox;
				rowData[2] = serviceStatus;
				rowData[3] = serviceProgress;
				model.addRow(rowData);
			}
		}
		
		String secondColumnName = "sellers";
		if(userType.equals(User.SELLER_TYPE)){
			secondColumnName = "buyers";
		}
		model.setColumnIdentifiers(new Object[]{"name",secondColumnName,"status","progress"});
		// initialize the table based on the model
		table = new JTable(model);
		table.setRowHeight(20);
		
		// we render the progress bar
		table.getColumnModel().getColumn(3).setCellRenderer(new ProgressBarRender());
		
		// we render the list of sellers
		EachRowEditor rowEditor = new EachRowEditor(table);
		for(int i =0; i < table.getRowCount();i++){
			 JComboBox comboBox = al.get(i);
			 rowEditor.setEditorAt(i, new DefaultCellEditor(comboBox));
			 table.getColumn(secondColumnName).setCellEditor(rowEditor);
		}
		table.getColumnModel().getColumn(1).setCellRenderer(new ListRender());
		
		table.setCellSelectionEnabled(true);
		
		// adding mouse listener on table
		table.addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseReleased(MouseEvent e) {
				
				if(e.isMetaDown()){
					final int r = table.rowAtPoint(e.getPoint());
		            final int c = table.columnAtPoint(e.getPoint());
		            if (r >= 0 && r < table.getRowCount()) {
		                table.setRowSelectionInterval(r, r);
		            } else {
		                table.clearSelection();
		            }
		            JPopupMenu popup = new JPopupMenu();
		            // if the user is a buyer
		            if(userType.equals(User.BUYER_TYPE)){
		            	// if he pressed on service name cell
		            	if(c == 0){
		            		popup.removeAll();
		            		
		            		// add launch offer request button
		            		JMenuItem launch = new JMenuItem("Launch offer request");
		            		launch.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent arg0) {
									String productName = (String)table.getValueAt(r, c);
									
									// we change the status in ACTIVE->NO OFFER
									table.setValueAt("NO OFFER", r, 2);
									
									// we make a request to mediator for sellers list
									ArrayList<String> sellers = 
										mediator.getSellers(productName);
									
									if(sellers != null){
										// we populate the comboBox
										JComboBox combo = (JComboBox)table.getModel().getValueAt(r, 1);
										for(int i =0; i<sellers.size();i++){
											combo.addItem(sellers.get(i));
										}
										combo.setEnabled(true);
										
										combo.setName("combo"+r);
										combo.addComponentListener(new ComponentAdapter() {
										      public void componentShown(ComponentEvent e) {
										        final JComponent c = (JComponent) e.getSource();
										        SwingUtilities.invokeLater(new Runnable() {
										          public void run() {
										            c.requestFocus();
										            System.out.println(c);
										            if (c instanceof JComboBox) {
										              System.out.println("a");
										            }
										          }
										        });
										      }
										    });
										
										// action listener for changing item
										combo.addActionListener(new ActionListener() {
											
											@Override
											public void actionPerformed(ActionEvent arg0) {
												
												// we take the name of the comboBox
												// it's like "combo0" ,where 0 is the row number in table
												String comboName = (String)((JComboBox)arg0.getSource()).getName();
												int rowNo = Integer.parseInt(comboName.charAt(comboName.length()-1) +"");
												
												String sellerName =(String)((JComboBox)arg0.getSource()).getSelectedItem();
												String productName = mainProducts.get(rowNo).getName();
											}
										});
									}
									
								}
							});
		            		
		            		// add drop offer request button
		            		JMenuItem drop = new JMenuItem("Drop offer request");
		            		drop.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									String serviceName = (String)table.getValueAt(r, c);
									System.out.println("drop offer request for " + serviceName);
								}
							});
		            		drop.setEnabled(false);
		            		
		            		popup.add(launch);
		            		popup.add(drop);
		            	}
		            	// else if he pressed on sellers list cell
		            	else if(c == 1){
		            		popup.removeAll();
		            		// add accept offer button
		            		JMenuItem acceptOffer = new JMenuItem("Accept offer");
		            		acceptOffer.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									System.out.println("accept offer");
								}
							});
		            		acceptOffer.setEnabled(false);
		            		
		            		// add refuse offer button
		            		JMenuItem refuseOffer = new JMenuItem("Refuse offer");
		            		refuseOffer.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									System.out.println("refuse offer");
								}
							});
		            		refuseOffer.setEnabled(false);
		            		
		            		popup.add(acceptOffer);
		            		popup.add(refuseOffer);
		            	}
		            }
		            // else, if he is a seller, we create other items in the menu
		            else if(userType.equals(User.SELLER_TYPE)){
		            	
		            	// if he pressed right click on buyers list
		            	if(c == 1){
		            		popup.removeAll();
		            		// add make offer button
		            		JMenuItem makeOffer = new JMenuItem("Make offer");
		            		makeOffer.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									
									String offerValue = 
										JOptionPane.showInputDialog("Insert the value");
								}
							});
		            		makeOffer.setEnabled(true);
		            		
		            		// add drop auction  button
		            		JMenuItem dropAuction = new JMenuItem("Drop auction");
		            		dropAuction.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									System.out.println("drop auction");
								}
							});
		            		dropAuction.setEnabled(false);
		            		
		            		popup.add(makeOffer);
		            		popup.add(dropAuction);
		            	}
		            }
		            
		            popup.show(e.getComponent(), e.getX(), e.getY());
		          //  System.out.println(r + " " + c);

		        /*    int rowindex = table.getSelectedRow();
		            if (rowindex < 0)
		                return;
		            if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
		                JPopupMenu popup = new JPopupMenu();
		                popup.show(e.getComponent(), e.getX(), e.getY());
		            }
		            */
				}
				
	            
			}
		});
		
		// top, bottom, center
		JPanel top = new JPanel(new FlowLayout());			// a welcome message	
		JPanel center = new JPanel(new FlowLayout());		// the table
		JPanel bottom = new JPanel(new FlowLayout());		// sign out button
		this.setLayout(new BorderLayout());
		this.add(top,BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(bottom,BorderLayout.SOUTH);
		
		// welcome message
		welcomeMessage = new JLabel("Welcome " + userName + "!");
		top.add(welcomeMessage);
		JScrollPane sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(800,300));
		center.add(sp);
		
		// sign out button
		signOutB = new JButton("sign out");
		signOutB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// if the sign out action went well, we load the sign in form
				if(mediator.signOut(userName)){
					initSignIn();
				}
			}
		});
		bottom.add(signOutB);
		
		this.revalidate();
		this.repaint();
	}
	
	
	// returns the value of the offer made by sellerName
	// for the productName
	public static String getValue(ArrayList<Offer> offers , String sellerName , String productName){
		String s  = null;
		
		for(int i=0; i<offers.size();i++){
			Offer offer = offers.get(i);
			if(offer.getSeller().equals(sellerName) && 
					offer.getProduct().equals(productName)){ 
				return offer.getValue();
			}
		}
		
		return s;
	}
	

	
	
}
