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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

	private static final long 					serialVersionUID = 1L;
	private DefaultTableModel					model;			// custom table model
	private JTable 								table;			// table
	private JButton 							signOutB; 		// sign out button
	private JLabel								welcomeMessage; // welcome message label
	protected JTextField 						username;
	protected JPasswordField 					pass;
	
	protected String 							userType;
	protected String 							userName;
	protected IMediator 						mediator;
	private Hashtable<String,ArrayList<User>> 	tableEntries;	// key = productName
	private Hashtable<String, Boolean>			productsStatus; // key = productName, value = isActive
	public static ArrayList<Product> 			mainProducts;
	
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
					mediator.setLoggerFile(userName);
					mediator.setIpAndPort(userType);
				}
				// else we change the message
				else{
					welcomeMessage.setText("username or password is wrong");
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
		// this personalized model should be read-only (we can't edit a cell on double-click)
		model = new DefaultTableModel(0,4);
		
		// we save the comboboxes in order to add an editor for each row
		ArrayList<JComboBox<String>> al = new ArrayList<JComboBox<String>>();
		// we take the products from the mediator
		ArrayList<Product> products = mediator.loadProducts(userName, userType);
		
		if(products != null){
			// we save the products
			mainProducts = products;
			
			// we initialize the status of all products with "INACTIVE"
			productsStatus = new Hashtable<String, Boolean>();
			for(int i=0; i< products.size() ; i++){
				String productName = products.get(i).getName();
				productsStatus.put(productName, false);
			}
			
			// for each products we create an entry in table
			for(int i=0; i< products.size(); i++){
				
				// service name
				String serviceName = products.get(i).getName();
				
				String offerValue = "null";
				JComboBox<String> comboBox = new JComboBox<String>();
				al.add(comboBox);
				
				// service status
				String serviceStatus = "INACTIVE";
				
				// service progress bar
				JProgressBar  serviceProgress = new JProgressBar(0,10);
				
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
			 JComboBox<String> comboBox = al.get(i);
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
									
									// launch request in network
									mediator.LaunchOfferRequest(productName);
									
									// we change the status in ACTIVE->NO OFFER
									table.setValueAt("NO OFFER", r, 2);
									
									// we make a request to mediator for sellers list
									ArrayList<String> sellers = 
										mediator.getSellers(productName);
									
									if(sellers != null){
										// we populate the comboBox
										@SuppressWarnings("unchecked")
										JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(r, 1);
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
										            if (c instanceof JComboBox) {
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
												@SuppressWarnings("unchecked")
												String comboName = (String)((JComboBox<String>)arg0.getSource()).getName();
												int rowNo = Integer.parseInt(comboName.charAt(comboName.length()-1) +"");
												
												@SuppressWarnings("unchecked")
												String userName1 =(String)((JComboBox<String>)arg0.getSource()).getSelectedItem();
												String productName = mainProducts.get(rowNo).getName();
												
												// we search in hashtable the user with "userName" for key = "productName"
												// we change the status
												ArrayList<User> users = tableEntries.get(productName);
												if(users != null){
													User user = null;
													for(int i=0; i<users.size();i++){
														if(users.get(i).getUsername().equals(userName1)){
															user = users.get(i);
															break;
														}
													}
													
													// if we found that user (from the list)
													if(user != null){
														
														// if the user who is logged in is a buyer
														// then the "user" from above is a seller
														if(userType.equals(User.BUYER_TYPE)){
															Seller seller = (Seller) user;
															
															// we search in offers array for an offer for this product
															// if the offer array is null we change status to "NO OFFER"
															if(seller.getOffers() == null){
																table.setValueAt("NO OFFER", rowNo, 2);
															}
															else{
																ArrayList<Offer> offers = seller.getOffers();
																for(int i=0 ; i<offers.size();i++){
																	Offer offer = offers.get(i);
																	if(offer.getProduct().equals(productName)){
																		
																		// we change the status for this service
																		if(offer.isTransferMade()){
																			table.setValueAt("TRANSFER COMPLETED", rowNo, 2);
																		}
																		else if(offer.isTransferFailed()){
																			table.setValueAt("TRANSFER FAILED", rowNo, 2);
																		}
																		else if(offer.isTransferInProgress()){
																			table.setValueAt("TRANSFER IN PROGRESS", rowNo, 2);
																		}
																		else if(offer.isRefused()){
																			table.setValueAt("OFFER REFUSED", rowNo, 2);
																			break;
																		}
																		table.setValueAt("OFFER MADE", rowNo, 2);
																			
																	}
																}
															}
														}
														
													}
												}
											}
										});
										
										// we create the list of sellers
										ArrayList<User> sellersObject = new ArrayList<User>();
										for(int i=0; i<sellers.size();i++){
											Seller s = new Seller(sellers.get(i),null,User.SELLER_TYPE);
											s.setOffers(null);
											sellersObject.add(s);
										}
										
										// we add an entry in hashtable
										if(tableEntries == null){
											tableEntries = new Hashtable<String,ArrayList<User>>();
										}
										tableEntries.put(productName, sellersObject);
									}
									
									// we enable the "drop offer request button"
									productsStatus.put(productName, true);
									
								}
							});
		            		String productName = (String)table.getValueAt(r, c);
		            		
		            		// if this product is active we disable this button
		            		if(productsStatus != null &&
		            				productsStatus.get(productName)){
		            			launch.setEnabled(false);
		            		}
		            		
		            		// add drop offer request button
		            		JMenuItem drop = new JMenuItem("Drop offer request");
		            		// action listener for drop offer request
		            		drop.addActionListener(new ActionListener() {
								
								@SuppressWarnings("unchecked")
								@Override
								public void actionPerformed(ActionEvent e) {
									String productName = (String)table.getValueAt(r, c);
									
									mediator.DropOfferRequest(productName);
									// we set inactive this product
									// in productsStatus hashtable
									productsStatus.put(productName, false);
									
									// we disable the combo box
									JComboBox<String> combo = null;
									try{
										 combo = (JComboBox<String>)
														table.getModel().getValueAt(r, 1);
										combo.removeAllItems();
										combo.removeAll();
										combo.setEnabled(false);
										table.getModel().setValueAt(combo, r, 1);
									}catch(ClassCastException ex){
										JComboBox<Object> c = new JComboBox<Object>();
										c.setEnabled(false);
										table.getModel().setValueAt(c, r, 1);
									}
									final Offer offer = getOffer(productName, r);
									if(offer != null)
										offer.setRefused(true);
									
									// we set the text "INACTIVE" on status column
									table.getModel().setValueAt("INACTIVE", r, 2);
									
									revalidate();
									repaint();
								}
							});
		            		
		            		drop.setEnabled(false);
		            		
		            		// if this product is ACTIVE but not accepted or not refused
		            		// we enable the drop button
		            		if(tableEntries != null &&
		            				productsStatus != null &&
		            				productsStatus.get(productName) &&
		            				!isAccepted(productName)){
		            			drop.setEnabled(true);
		            		}
		            		
		            		
		            		popup.add(launch);
		            		popup.add(drop);
		            	}
		            	// else if he pressed on sellers list cell
		            	else if(c == 1){
		            		popup.removeAll();
		            		// add accept offer button
		            		JMenuItem acceptOfferB = new JMenuItem("Accept offer");
		            		acceptOfferB.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									
									// we change the state of the product
									table.getModel().setValueAt("OFFER ACCEPTED", r, 2);
									
									// we set the offer as accepted
									String productName = (String)table.getModel().getValueAt(r, 0);
									acceptOffer(productName, r);
									
									// we announce the mediator that we accepted the offer
									final Offer offer = getOffer(productName, r);
									offer.setIsAccepted(true);
									//mediator.acceptOffer(userName, offer);
									//System.out.println("Offer: " +offer.getProduct()+" "+ offer.getValue());
									
									// we change the status again in transfer started
									table.getModel().setValueAt("TRANSFER STARTED", r, 2);								
									offer.setTransferInProgress(true);
									 // we tell to mediator to start transfer
								    mediator.startTransfer(userName, offer.getSeller(), offer.getProduct(), offer.getValue());
								    
									// we start the progress bar
									ExportTask task = new ExportTask(Integer.parseInt(offer.getValue()));
									PropertyChangeListener listener = new PropertyChangeListener() {
										@Override
										public void propertyChange(PropertyChangeEvent evt) {
											JProgressBar progressBar = 
												(JProgressBar)table.getModel().getValueAt(r, 3);
											progressBar.setMaximum(Integer.parseInt(offer.getValue()));
											if ("progress".equals(evt.getPropertyName())) {
									        	Integer newValue = (Integer)evt.getNewValue();
									        	
									        	// if the transfer is on 2%, we change the status
									        	if(newValue == 2){
									        		table.getModel().setValueAt("TRANSFER IN PROGRESS", r, 2);
									        	}
									        	else if(newValue == progressBar.getMaximum()){
									        		table.getModel().setValueAt("TRANSFER COMPLETED", r, 2);
									        		offer.setTransferMade(true);
									        	}
									        	progressBar.setValue((Integer)evt.getNewValue());
									        	model.setValueAt(progressBar, r, 3);
									        	table.setModel(model);
											}
										}
									};
									task.addPropertyChangeListener(listener);
								    task.execute();
								    
								   
								}
							});
		            		acceptOfferB.setEnabled(false);
		            		
		            		String productName = (String)table.getModel().getValueAt(r,0);
		            		// if this product is active 
		            		// and the selected seller in comboBox made an offer
		            		// then we enable the button
		            		if(productsStatus.get(productName) &&
		            				offerMade(productName,r) &&
		            				!isAccepted(productName) &&
		            				!isRefused(productName)){
		            			acceptOfferB.setEnabled(true);
		            		}
		            		
		            		// add refuse offer button
		            		JMenuItem refuseOfferB = new JMenuItem("Refuse offer");
		            		refuseOfferB.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									String productName = (String)table.getModel().getValueAt(r, 0);
									
									// we refuse the offer
									refuseOffer(productName, r);
									
									// and change the status to "OFFER REFUSED"
									table.getModel().setValueAt("OFFER REFUSED", r, 2);
									
									ArrayList<User> users = tableEntries.get(productName);
									for(int i=0; i <users.size() ; i++){
										Seller seller = (Seller) users.get(i);
										ArrayList<Offer> offers = seller.getOffers();
										
										if(offers != null){
											for (int j=0 ; j<offers.size(); j++){
												Offer offer = offers.get(j);
												if(offer.getProduct().equals(productName)){
													offer.setRefused(true);
												}
											}
										}
									}
									
									// we announce the mediator that the offer has been refused
									Offer o = getOffer(productName, r);
									mediator.refuseOffer(userName, o.getSeller(), o.getProduct(), o.getValue());
								}
							});
		            		refuseOfferB.setEnabled(false);
		            		
		            		// if this product is active 
		            		// and the selected seller in comboBox made an offer
		            		// and the buyer didn't accept the offer or didn't refused it
		            		// then we enable the button
		            		if(productsStatus.get(productName) &&
		            				offerMade(productName,r) &&
		            				!isAccepted(productName) &&
		            				!isRefused(productName)){
		            			refuseOfferB.setEnabled(true);
		            		}
		            		
		            		popup.add(acceptOfferB);
		            		popup.add(refuseOfferB);
		            	}
		            }
		            // else, if he is a seller, we create other items in the menu
		            else if(userType.equals(User.SELLER_TYPE)){
		            	final String productName = (String)table.getModel().getValueAt(r,0);
		            	
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
									
									String buyerName = "";
									try{
										@SuppressWarnings("unchecked")
										JComboBox<String> com = (JComboBox<String>)table.getModel().getValueAt(r, 1);
										buyerName = (String)com.getSelectedItem();
									}
									catch(ClassCastException event){
										buyerName = (String)table.getModel().getValueAt(r, 1);
									}
									
									ArrayList<User> buyers = tableEntries.get(productName);
									if(buyers != null){
										// we find the selected buyer
										for(int i=0 ;i< buyers.size();i++){
											Buyer buyer = (Buyer)buyers.get(i);
											// if we found it , we take the proper request
											if(buyer.getUsername().equals(buyerName)){
												ArrayList<Request> requests = buyer.getRequests();
												for(int j=0; j<requests.size(); j++){
													Request request = (Request)requests.get(j);
													// once we found the proper request we set the offer
													if(request.getProductName().equals(productName)){
														Offer offer = new Offer(productName,userName,offerValue);
														request.setOffer(offer);
														offer.setIsAccepted(true);
														// nume buyer, oferta, produs
														mediator.makeOffer(buyer.getUsername(), offer, productName);
														// and we change the status
														table.getModel().setValueAt("OFFER MADE", r, 2);
													}
												}
											}
										}
									}
									
								}
							});
		            		makeOffer.setEnabled(false);
		            		
		            		// we enable this button only if this offer request is active
		            		if(productsStatus.get(productName) &&
		            				!offerMade(productName,r)){
		            			makeOffer.setEnabled(true);
		            		}
		            		
		            		// add drop auction  button
		            		JMenuItem dropAuction = new JMenuItem("Drop auction");
		            		dropAuction.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									
									// we make this product inactive
									productsStatus.put(productName, false);
									
									table.getModel().setValueAt("INACTIVE", r, 2);
									JComponent component = null;
									try{
										component = (JComponent)
														table.getModel().getValueAt(r, 1);
									}
									catch(java.lang.ClassCastException ex){
										String why = (String)table.getModel().getValueAt(r, 1);
										Object[] v = new Object[1];
										v[0] = why;
										component = new JComboBox<Object>(v);
									}
									component.removeAll();
									component.setEnabled(false);
									revalidate();
									repaint();
									
								}
							});
		            		dropAuction.setEnabled(false);
		            		if(productsStatus.get(productName) && 
		            				!offerMade(productName, r) &&
		            				!isRefused(productName)){
		            			dropAuction.setEnabled(true);
		            		}
		            		
		            		popup.add(makeOffer);
		            		popup.add(dropAuction);
		            	}
		            }
		            
		            popup.show(e.getComponent(), e.getX(), e.getY());
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
					mediator.signOutAnnounce(userName);
					userName = null;
					initSignIn();
				}
			}
		});
		bottom.add(signOutB);
		
		this.revalidate();
		this.repaint();
	}
	
	// metoda pt seller, in care se populeaza gui-ul cu acel cumparator care vrea un produs
	public  void OfferRequestReceived(String productName, String buyer)
	{
		System.out.println("Lista de produse: "+mainProducts.size());
		for(int i=0; i<mainProducts.size();i++){
			
			if(mainProducts.get(i).getName().equals(productName)){
				System.out.println("Produs dorit: "+ mainProducts.get(i).getName());
				// populate the proper comboBox with the buyer
				@SuppressWarnings("unchecked")
				JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(i, 1);
			
				combo.addItem(buyer);
			
				if (!combo.isEnabled()){
					combo.setEnabled(true);
					
					combo.setName("combo"+i);
					combo.addComponentListener(new ComponentAdapter() {
				      public void componentShown(ComponentEvent e) {
				        final JComponent c = (JComponent) e.getSource();
				        SwingUtilities.invokeLater(new Runnable() {
				          public void run() {
				            c.requestFocus();
				            if (c instanceof JComboBox) {
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
							@SuppressWarnings("unchecked" )
							String comboName = (String)((JComboBox<String>)arg0.getSource()).getName();
							int rowNo = Integer.parseInt(comboName.charAt(comboName.length()-1) +"");
							
							@SuppressWarnings("unchecked")
							String userName1 =(String)((JComboBox<String>)arg0.getSource()).getSelectedItem();
							String productName = mainProducts.get(rowNo).getName();
							
							// we search in hashtable the user with "userName" for key = "productName"
							// we change the status
							ArrayList<User> users = tableEntries.get(productName);
							if(users != null){
								User user = null;
								for(int i=0; i<users.size();i++){
									if(users.get(i).getUsername().equals(userName1)){
										user = users.get(i);
										break;
									}
								}
								
								// if we found that user (from the list)
								if(user != null){
									
									// if the user who is logged in is a seller
									// then the "user" from above is a buyer
									if(userType.equals(User.SELLER_TYPE)){
										Buyer buyer = (Buyer) user;
										
										// we search in his requests array for an request offer for this product
										// if the requests array is null we change status to "NO OFFER"
										if(buyer.getRequests() == null){
											table.setValueAt("NO OFFER", rowNo, 2);
										}
										else{
											ArrayList<Request> requests = buyer.getRequests();
											for(int i=0 ; i<requests.size();i++){
												Request request = requests.get(i);
												// if this is an request for the proper product
												// and we made an offer for it
												// we change the status in OFFER MADE
												if(request.getProductName().equals(productName) &&
														request.getOffer() != null){
													
													// we change the status for this service
													table.setValueAt("OFFER MADE", rowNo, 2);
												}
											}
										}
									}
									
								}
							}
						}
					
					});
				}
				
				Buyer s = new Buyer(buyer,null,User.BUYER_TYPE);
				Request r = new Request(productName, buyer);
				s.getRequests().add(r);
					
				// we add an entry in hashtable				
				if(tableEntries == null){
					tableEntries = new Hashtable<String,ArrayList<User>>();
				}
				if(tableEntries.get(productName) == null){
					ArrayList<User> arr = new ArrayList<User>();
					arr.add(s);
					tableEntries.put(productName, arr);
				}
				else{
					tableEntries.get(productName).add(s);
				}
				
				// we change the status to "NO OFFER" because he didn't make any offer
				table.getModel().setValueAt("NO OFFER", i, 2);
				// and change to active this product
				productsStatus.put(productName, true);
				revalidate();
				repaint();
			}
		}
	}
	
	// metoda pt seller, in care incepe transferul
	public void OfferAccepted(String buyer, String product, String value)
	{
		// we parse every entry from the "tableEntries"
		if(tableEntries != null){
			@SuppressWarnings("rawtypes")
			Set set = tableEntries.entrySet();
			if(set != null){
				@SuppressWarnings("rawtypes")
				Iterator it = set.iterator();
			    while (it.hasNext()) {
			      @SuppressWarnings({ "rawtypes", "unchecked" })
				  Map.Entry<String,ArrayList<User>> entry = (Map.Entry) it.next();
			      
			      // we take the product name
			      String productName = product;
			      // and the list of buyers
			      ArrayList<User> buyers = (ArrayList<User>)entry.getValue();
			      
			      // we iterate in the list of buyers, searching for that buyers who "accepted" the offer
			      // for this productName
				  for(int i=0; i<buyers.size() ;i++){
					  if(((Buyer)buyers.get(i)).getUsername().equals(buyer))
					  {
				    	  ArrayList<Request> requests = ((Buyer)buyers.get(i)).getRequests();
				    	  
				    	  // we iterate the list of requests and see if we made an offer for one of them
				    	  for(int j=0; j<requests.size();j++){
				    		 final Request request = (Request)requests.get(j);
				    		  if(request.getProductName().equals(productName)){
				    			  // once we found the offer we made , we start the transfer
				    			  if(request.getOffer() != null){
				    				  // we search the row in table , having the productName
				    				  int rowNo = -1;
				    				  for(int k=0; k < mainProducts.size() ;k++){
				    					  if(mainProducts.get(k).getName().equals(productName)){
				    						  rowNo = k;
				    						  break;
				    					  }
				    				  }
				    				  final int r = rowNo;
				    				  request.getOffer().setIsAccepted(true);
				    				  // we change the status to OFFER ACCEPTED
				    				  table.getModel().setValueAt("OFFER ACCEPTED", r, 2);
				    				  
				    				  // we start the transfer
				    				  // we change the status again in transfer started
				    				  request.getOffer().setTransferInProgress(true);
									  table.getModel().setValueAt("TRANSFER STARTED", r, 2);
										
									  // we start the progress bar
									  ExportTask task = new ExportTask(Integer.parseInt(request.getOffer().getValue()));
									  PropertyChangeListener listener = new PropertyChangeListener() {
										@Override
										public void propertyChange(PropertyChangeEvent evt) {
											JProgressBar progressBar = 
												(JProgressBar)table.getModel().getValueAt(r, 3);
											progressBar.setMaximum(Integer.parseInt(request.getOffer().getValue()));
											if ("progress".equals(evt.getPropertyName())) {
									        	Integer newValue = (Integer)evt.getNewValue();
									        	
									        	// if the transfer is on 2%, we change the status
									        	if(newValue == 2){
									        		table.getModel().setValueAt("TRANSFER IN PROGRESS", r, 2);
									        	}
									        	else if(newValue == progressBar.getMaximum()){
									        		request.getOffer().setTransferMade(true);
									        		table.getModel().setValueAt("TRANSFER COMPLETED", r, 2);
									        	}
									        	int x = (Integer)evt.getNewValue();
									        	
									        	progressBar.setValue(x);
									        	model.setValueAt(progressBar, r, 3);
									        	table.setModel(model);
											}
										}
									  };
										task.addPropertyChangeListener(listener);
									    task.execute();
				    			  }
				    		  }
				    	  }
				      }
				    }
			    }
			}
		}
	}

	// metoda pt seller, in care este refuzata oferta sa
	public void OfferRefused(String buyer, String product, String value){
		
		if(tableEntries != null){
			@SuppressWarnings("rawtypes")
			Set set = tableEntries.entrySet();
			if(set != null){
				@SuppressWarnings("rawtypes")
				Iterator it = set.iterator();
			    while (it.hasNext()) {
			      @SuppressWarnings({ "rawtypes", "unchecked" })
				  Map.Entry<String,ArrayList<User>> entry = (Map.Entry) it.next();
			      
			      // we take the product name
			      String productName = product;
			      // and the list of buyers
			      ArrayList<User> buyers = (ArrayList<User>)entry.getValue();
			      
			      // we iterate in the list of buyers, searching for that buyers who "accepted" the offer
			      // for this productName
				  for(int i=0; i<buyers.size() ;i++){
					  if(((Buyer)buyers.get(i)).getUsername().equals(buyer))
					  {
				    	  ArrayList<Request> requests = ((Buyer)buyers.get(i)).getRequests();
				    	  
				    	  // we iterate the list of requests and see if we made an offer for one of them
				    	  for(int j=0; j<requests.size();j++){
				    		 final Request request = (Request)requests.get(j);
				    		  if(request.getProductName().equals(productName)){
				    			  // once we found the offer we made , we start the transfer
				    			  if(request.getOffer() != null){
				    				  
				    				  // we search the row in table , having the productName
				    				  int rowNo = -1;
				    				  for(int k=0; k < mainProducts.size() ;k++){
				    					  if(mainProducts.get(k).getName().equals(productName)){
				    						  rowNo = k;
				    						  break;
				    					  }
				    				  }
				    				  final int r = rowNo;
				    				  
				    				  // we delete the offer made from the list
				    				  requests.remove(j);
				    				  // we change the status to OFFER REFUSED
				    				  table.getModel().setValueAt("OFFER REFUSED", r, 2);		
				    				  break;
				    			  }
				    		  }
				    	  }
				      }
				    }
			    }
			}
		}
		
	
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
	
	// this method returns true if an offer for "productName" is accepted
	// or false otherwise
	public boolean isAccepted(String productName){
		
		ArrayList<User> users = tableEntries.get(productName);
		
		for(int i=0; i <users.size() ; i++){
			Seller seller = (Seller) users.get(i);
			ArrayList<Offer> offers = seller.getOffers();
			
			if(offers != null){
				for (int j=0 ; j<offers.size(); j++){
					Offer offer = offers.get(j);
					if(offer.getProduct().equals(productName)){
						if(offer.getIsAccepted()){
							return true;
						}
						else{
							return false;
						}
					}
				}
			}
		}
		return false;
	}
	
	// this method returns true if an offer for "productName" is refused
	// or false otherwise
	public boolean isRefused(String productName){
		
		ArrayList<User> users = tableEntries.get(productName);
		
		for(int i=0; i <users.size() ; i++){
			if(userType.equals(User.BUYER_TYPE)){
				Seller seller = (Seller) users.get(i);
				ArrayList<Offer> offers = seller.getOffers();
				
				if(offers != null){
					for (int j=0 ; j<offers.size(); j++){
						Offer offer = offers.get(j);
						if(offer.getProduct().equals(productName)){
							if(offer.isRefused()){
								return true;
							}
							else{
								return false;
							}
						}
					}
				}
			}
			else
			{
				Buyer buyer = (Buyer) users.get(i);
				ArrayList<Request> offers = buyer.getRequests();
				
				if(offers != null){
					for (int j=0 ; j<offers.size(); j++){
						Offer offer = offers.get(j).getOffer();
						if(offer != null && offer.getProduct().equals(productName)){
							if(offer.isRefused()){
								return true;
							}
							else{
								return false;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	// this method will return true if an offer was made by the seller
	// who is selected in the comboBox from the rowNo, for productName
	public boolean offerMade(String productName, int rowNo){
		
		String selectedUserName = "";
		try{
			@SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(rowNo, 1);
			selectedUserName = (String)combo.getSelectedItem();
		}
		catch(java.lang.ClassCastException e){
			selectedUserName = (String)table.getModel().getValueAt(rowNo, 1);
		}
		
			 
		if(tableEntries != null){
			ArrayList<User> users = tableEntries.get(productName);
			if(users != null){
				for(int i=0; i<users.size();i++){
					if(userType.equals(User.BUYER_TYPE)){
						Seller seller = (Seller)users.get(i);
						if(seller.getUsername().equals(selectedUserName)){
							
							// we check if this seller made an offer for the given product
							ArrayList<Offer> offers = seller.getOffers();
							if(offers != null){
								for(int j=0 ; j<offers.size(); j++){
									Offer offer = offers.get(j);
									if(offer.getProduct().equals(productName)){
										return true;
									}
								}
							}
						}
					}
					else{
						Buyer buyer = (Buyer)users.get(i);
						if(buyer.getUsername().equals(selectedUserName)){
							ArrayList<Request> requests = buyer.getRequests();
							for(int j=0; j <requests.size();j++){
								Request curr = requests.get(j);
								if(curr.getProductName().equals(productName)){
									if(curr.getOffer() != null){
										return true;
									}
									else{
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		return false;
	}
	
	// this method will set as accepted the offer of the seller
	// who is selected in the comboBox from the rowNo, for productName
	public void acceptOffer(String productName, int rowNo){
		
		String selectedSellerName = "";
		try{
			@SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(rowNo, 1);
			selectedSellerName = (String)combo.getSelectedItem();
		}
		catch(java.lang.ClassCastException e){
			selectedSellerName = (String)table.getModel().getValueAt(rowNo, 1);
		}
		
			 
		if(tableEntries != null){
			ArrayList<User> sellers = tableEntries.get(productName);
			if(sellers != null){
				for(int i=0; i<sellers.size();i++){
					Seller seller = (Seller)sellers.get(i);
					if(seller.getUsername().equals(selectedSellerName)){
						
						// we check if this seller made an offer for the given product
						ArrayList<Offer> offers = seller.getOffers();
						if(offers != null){
							for(int j=0 ; j<offers.size(); j++){
								Offer offer = offers.get(j);
								if(offer.getProduct().equals(productName)){
									offer.setIsAccepted(true);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	// this method will refuse the offer from the seller who made it
	public void refuseOffer(String productName, int rowNo){
		String selectedSellerName = "";
		try{
			@SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(rowNo, 1);
			selectedSellerName = (String)combo.getSelectedItem();
		}
		catch(java.lang.ClassCastException e){
			selectedSellerName = (String)table.getModel().getValueAt(rowNo, 1);
		}
		
			 
		if(tableEntries != null){
			ArrayList<User> sellers = tableEntries.get(productName);
			if(sellers != null){
				for(int i=0; i<sellers.size();i++){
					Seller seller = (Seller)sellers.get(i);
					if(seller.getUsername().equals(selectedSellerName)){
						
						// we check if this seller made an offer for the given product
						ArrayList<Offer> offers = seller.getOffers();
						if(offers != null){
							for(int j=0 ; j<offers.size(); j++){
								Offer offer = offers.get(j);
								if(offer.getProduct().equals(productName)){
									offer.setRefused(true);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	// this method will return the offer which is accepted
	public Offer getOffer(String productName, int rowNo){
		
		String selectedSellerName = "";
		try{
			@SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(rowNo, 1);
			selectedSellerName = (String)combo.getSelectedItem();
		}
		catch(java.lang.ClassCastException e){
			selectedSellerName = (String)table.getModel().getValueAt(rowNo, 1);
		}
		
			 
		if(tableEntries != null){
			ArrayList<User> sellers = tableEntries.get(productName);
			if(sellers != null){
				for(int i=0; i<sellers.size();i++){
					Seller seller = (Seller)sellers.get(i);
					if(seller.getUsername().equals(selectedSellerName)){
						
						// we check if this seller made an offer for the given product
						ArrayList<Offer> offers = seller.getOffers();
						if(offers != null){
							for(int j=0 ; j<offers.size(); j++){
								Offer offer = offers.get(j);
								if(offer.getProduct().equals(productName)){
									return offer;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getUsername() {
		return userName;
	}
	
	//metoda pentru buyers unde primesc ofertele
	@Override
	public void OfferReceived(String product, String value, String seller) {
	
		if(tableEntries != null){
			
			ArrayList<User> users = tableEntries.get(product);
			
			for(int i=0; i< users.size() ; i++){
				Seller s = (Seller)users.get(i);
				if(s.getUsername().equals(seller)){
					Offer offer = new Offer(product,seller,value);
			    	ArrayList<Offer> offers = new ArrayList<Offer>();
			    	offers.add(offer);
			    	s.setOffers(offers);
			    	
			    	// we change the status if the selected item in comboBox is the name
		    	  // of the seller
		    	  String selectedSellerName = "";
		    	  int rowNo = -1;
		    	  for(int j=0; j < mainProducts.size();j++){
		    		  if(mainProducts.get(j).getName().equals(product)){
		    			  rowNo = j;
		    			  break;
		    		  }
		    	  }
		    	  int colNo = 1;
		    	  try{
		    		  
		    		  @SuppressWarnings("unchecked")
					  JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(rowNo, colNo);
		    		  selectedSellerName = (String)combo.getSelectedItem();
		    	  }
		    	  catch(ClassCastException e){
		    		  selectedSellerName = (String)table.getModel().getValueAt(rowNo, colNo);
		    	  }
		    	  
		    	  if(seller.equals(selectedSellerName)){
		    		  // we change status to OFFER MADE
		    		  table.getModel().setValueAt("OFFER MADE", rowNo, 2);
		    	  }
					break;
			    	  
				}
			}
			
		}
	
	}

	public void DropOffer(String productName, String buyer) {
		
		Set<Map.Entry<String,ArrayList<User>>> entries = tableEntries.entrySet();
		Iterator<Map.Entry<String,ArrayList<User>>> it =  entries.iterator();
		
		while(it.hasNext()){
			Map.Entry<String,ArrayList<User>> entry = it.next();
			String pName = entry.getKey();
			
			if(pName.equals(productName)){
				
				// we remove the user from the list corresponding to the product name
				ArrayList<User> u = entry.getValue();
			
				for(int i=0; i < u.size();i++){
					if(u.get(i).getUsername().equals(buyer)){
						u.remove(i);
						break;
					}
				}
				break;
			}
		}
		
		for(int i=0; i<tableEntries.size();i++){
			String pName = (String)table.getModel().getValueAt(i, 0);
			if(pName.equals(productName)){
				@SuppressWarnings("unchecked")
				JComboBox<Object> c = (JComboBox<Object>)table.getModel().getValueAt(i, 1);
				for(int j=0; j < c.getItemCount() ; j++){
					String s = (String)c.getItemAt(j);
					if(s.equals(buyer)){
						c.removeItemAt(j);
						
						table.getModel().setValueAt(c, i, 1);
						revalidate();
						repaint();
					}
				}
				
				if(c.getItemCount() == 0){
					table.getModel().setValueAt("INACTIVE", i, 2);
					try{
						@SuppressWarnings("unchecked")
						JComboBox<Object> combo = (JComboBox<Object>)table.getModel().getValueAt(i, 1);
						combo.setEnabled(false);
						table.getModel().setValueAt(combo, i, 1);
					}
					catch(java.lang.ClassCastException ex){
						JComboBox<Object> combo = new JComboBox<Object>();
						combo.setEnabled(false);
						table.getModel().setValueAt(combo, i, 1);
					}
					
					revalidate();
					repaint();
				}
				break;
			}
		}
	}	

}
