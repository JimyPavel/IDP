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
								
								@Override
								public void actionPerformed(ActionEvent e) {
									String productName = (String)table.getValueAt(r, c);
									
									// we set inactive this product
									// in productsStatus hashtable
									productsStatus.put(productName, false);
									
									// we disable the combo box
									JComboBox<String> combo = new JComboBox<String>();
									combo.setEnabled(false);
									table.setValueAt(combo, r, 1);
									
									// we set the text "INACTIVE" on status column
									table.setValueAt("INACTIVE", r, 2);
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
									Offer offer = getOffer(productName, r);
									mediator.acceptOffer(userName, offer);
									
									
									// we change the status again in transfer started
									table.getModel().setValueAt("TRANSFER STARTED", r, 2);
									
									// we start the progress bar
									ExportTask task = new ExportTask();
									PropertyChangeListener listener = new PropertyChangeListener() {
										@Override
										public void propertyChange(PropertyChangeEvent evt) {
											JProgressBar progressBar = 
												(JProgressBar)table.getModel().getValueAt(r, 3);
											if ("progress".equals(evt.getPropertyName())) {
									        	Integer newValue = (Integer)evt.getNewValue();
									        	
									        	// if the transfer is on 2%, we change the status
									        	if(newValue == 2){
									        		table.getModel().setValueAt("TRANSFER IN PROGRESS", r, 2);
									        	}
									        	else if(newValue == progressBar.getMaximum()){
									        		table.getModel().setValueAt("TRANSFER COMPLETED", r, 2);
									        	}
									        	progressBar.setValue((Integer)evt.getNewValue());
									        	model.setValueAt(progressBar, r, 3);
									        	table.setModel(model);
											}
										}
									};
									task.addPropertyChangeListener(listener);
								    task.execute();
								    
								    // we tell to mediator to start transfer
								    mediator.startTransfer(userName, offer.getSeller(), offer);
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
									
									// we announce the mediator that the offer has been refused
									mediator.refuseOffer(userName, getOffer(productName, r));
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
		            		if(productsStatus.get(productName)){
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
									JComponent component = (JComponent)
														table.getModel().getValueAt(r, 1);
									component.setEnabled(false);
									revalidate();
									repaint();
									
								}
							});
		            		dropAuction.setEnabled(false);
		            		if(productsStatus.get(productName)){
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
					initSignIn();
				}
			}
		});
		bottom.add(signOutB);
		
		// if the user is a buyer we add the button for test
		if(userType == User.BUYER_TYPE){	
			// only for test
			// remove when you are done
			// he adds an offer for every service in list which is active
			// from the first seller
			JButton testOfferMade = new JButton("test Offer Made");
			testOfferMade.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(tableEntries != null){
						@SuppressWarnings("rawtypes")
						Set set = tableEntries.entrySet();
						if(set != null){
							@SuppressWarnings("rawtypes")
							Iterator it = set.iterator();
						    while (it.hasNext()) {
						      @SuppressWarnings("unchecked")
							  Map.Entry<String,ArrayList<User>> entry = (Map.Entry<String,ArrayList<User>>) it.next();
						      ArrayList<User> users = (ArrayList<User>)entry.getValue();
						      if(users != null){
						    	  Seller seller = (Seller) users.get(0);
						    	  Offer offer = new Offer((String)entry.getKey(),seller.getUsername(),"255");
						    	  ArrayList<Offer> offers = new ArrayList<Offer>();
						    	  offers.add(offer);
						    	  seller.setOffers(offers);
						    	  
						    	  // we change the status if the selected item in comboBox is the name
						    	  // of the seller
						    	  String selectedSellerName = "";
						    	  int rowNo = -1;
						    	  for(int i=0; i < mainProducts.size();i++){
						    		  if(mainProducts.get(i).getName().equals(entry.getKey())){
						    			  rowNo = i;
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
						    	  
						    	  if(seller.getUsername().equals(selectedSellerName)){
						    		  // we change status to OFFER MADE
						    		  table.getModel().setValueAt("OFFER MADE", rowNo, 2);
						    	  }
						      }
						    }
					    }
					}
					
					
				}
			});
			
			String text = "<html>This button simulates the action of receiving an offer from the sellers for each product in table."+
			"<br> The offer is received from the first seller in list. <br>" +
			" You have to launch at least an offer request before push this button</html>";
			testOfferMade.setToolTipText(text);
			bottom.add(testOfferMade);
		}
		else if(userType.equals(User.SELLER_TYPE)){
			
			// we add an button which simulates the fact that the buyers requested some offers
			JButton simOfferRequest = new JButton("Simulate offer requests");
			simOfferRequest.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					// we have to take the buyers for every product we have 
					for(int i=0; i<mainProducts.size();i++){
						String productName = mainProducts.get(i).getName();
						ArrayList<String> buyers = mediator.getBuyers(productName);
						
						// populate the proper comboBox with them
						@SuppressWarnings("unchecked")
						JComboBox<String> combo = (JComboBox<String>)table.getModel().getValueAt(i, 1);
						for(int j =0; j<buyers.size();j++){
							combo.addItem(buyers.get(j));
						}
						combo.setEnabled(true);
						revalidate();
						repaint();
						
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
						
						// we create the list of buyers
						ArrayList<User> buyersObject = new ArrayList<User>();
						ArrayList<Request> requests = new ArrayList<Request>();
						for(int k=0; k < buyers.size();k++){
							Request r = new Request(productName, buyers.get(k));
							requests.add(r);
						}
						for(int k=0; k < buyers.size();k++){
							Buyer s = new Buyer(buyers.get(k),null,User.BUYER_TYPE);
							s.setRequests(requests);
							buyersObject.add(s);
						}
						
						// we add an entry in hashtable
						if(tableEntries == null){
							tableEntries = new Hashtable<String,ArrayList<User>>();
						}
						tableEntries.put(productName, buyersObject);
						
						// we change the status to "NO OFFER" because he didn't make any offer
						table.getModel().setValueAt("NO OFFER", i, 2);
						// and change to active this product
						productsStatus.put(productName, true);
						
					}
				}
			});
			String text = "<html> This button simulates the event of receiving some offer requests <br> "+
			" We add some offer requests for each product we can produce <br>"+
			" After this button is pressed we can make an offer </html>";
			simOfferRequest.setToolTipText(text);
			
			// we add an button which simulates the fact that one buyer accepted an offer
			JButton simOfferAccepted = new JButton("simulate offers accepted");
			simOfferAccepted.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
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
						      String productName = (String)entry.getKey();
						      // and the list of buyers
						      ArrayList<User> buyers = (ArrayList<User>)entry.getValue();
						      
						      // we iterate in the list of buyers, searching for that buyers who "accepted" the offer
						      // for this productName
						      for(int i=0; i<buyers.size() ;i++){
						    	  Buyer buyer = (Buyer)buyers.get(i);
						    	  ArrayList<Request> requests = buyer.getRequests();
						    	  
						    	  // we iterate the list of requests and see if we made an offer for one of them
						    	  for(int j=0; j<requests.size();j++){
						    		  Request request = (Request)requests.get(j);
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
						    				  
						    				  // we change the status to OFFER ACCEPTED
						    				  table.getModel().setValueAt("OFFER ACCEPTED", r, 2);
						    				  
						    				  // we start the transfer
						    				  // we change the status again in transfer started
											  table.getModel().setValueAt("TRANSFER STARTED", r, 2);
												
											  // we start the progress bar
											  ExportTask task = new ExportTask();
											  PropertyChangeListener listener = new PropertyChangeListener() {
												@Override
												public void propertyChange(PropertyChangeEvent evt) {
													JProgressBar progressBar = 
														(JProgressBar)table.getModel().getValueAt(r, 3);
													if ("progress".equals(evt.getPropertyName())) {
											        	Integer newValue = (Integer)evt.getNewValue();
											        	
											        	// if the transfer is on 2%, we change the status
											        	if(newValue == 2){
											        		table.getModel().setValueAt("TRANSFER IN PROGRESS", r, 2);
											        	}
											        	else if(newValue == progressBar.getMaximum()){
											        		table.getModel().setValueAt("TRANSFER COMPLETED", r, 2);
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
						    		  }
						    	  }
						      }
						    }
						}
					}
					
				}
			});
			
			text = "<html> This button simulates the event of some buyers accepting your offers <br>"+
			" After this button is pressed , the transfer is started </html>";
			simOfferAccepted.setToolTipText(text);
			
			bottom.add(simOfferRequest);
			bottom.add(simOfferAccepted);
		}
		
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
		return false;
	}
	
	// this method will return true if an offer was made by the seller
	// who is selected in the comboBox from the rowNo, for productName
	public boolean offerMade(String productName, int rowNo){
		
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
									return true;
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
}
