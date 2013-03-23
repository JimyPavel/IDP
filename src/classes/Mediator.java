package classes;
import java.io.*;
import java.util.ArrayList;
import interfaces.*;

public class Mediator implements IGUIMediator{
	

	@Override
	// we return the type of the user
	// or null if there is no user with this details
	public String signIn(String username, String password) {
		// we search in database for username and password
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("config.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  // Read 3 lines for user details
			  // username
			  strLine = br.readLine();
			  
			  if(!strLine.equals(username)){
				  //Close the input stream
				  in.close();
				  return null;
			  }
			  
			  // password
			  strLine = br.readLine();
			  if(!strLine.equals(password)){
				  //Close the input stream
				  in.close();
				  return null;
			  }
			  
			  // type of the user
			  strLine = br.readLine();
			  if(strLine.equals("buyer")){
				  //Close the input stream
				  in.close();
				  return User.BUYER_TYPE;
			  }
			  else if(strLine.equals("seller")){
				  //Close the input stream
				  in.close();
				  return User.SELLER_TYPE;
			  }
			  
			  
		    }catch (Exception e){//Catch exception if any
		    	System.err.println("Error: " + e.getMessage());
		    }
		    
		return null;
	}

	@Override
	// for now this method will only return true
	public boolean signOut(String username) {
		return true;
	}

	@Override
	public ArrayList<Offer> loadOffers(String username , String userType) {
		
		ArrayList<Offer> offers = null;
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("config.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  // Ignore first 3 lines for user details
			  // username
			  br.readLine();
			  // password
			  br.readLine();
			  // userType
			  br.readLine();
			  
			  // we read the offers for the products
			  while((strLine=br.readLine()) != null){
				  // strLine is something like "service_2:name"
				  // where 2 is the number of sellers
				  // and name is the name of the service/product
				  String[] components = strLine.split(":");
				  String s = components[0];
				  char stringNo = s.charAt(s.length() -1);
				  int sellersNo = Integer.parseInt(stringNo + "");
				  String productName = components[1];
				  
				  // this offer is inactive (no offers were made)
				  // or the user didn't make an request for the product
				  if(sellersNo == 0){
					  Offer offer = new Offer(productName,null,null);
					  if(offers == null){
						  offers = new ArrayList<Offer>();
					  }
					  offers.add(offer);
				  }
				  else{
					// foreach seller we read the name of the seller
					  // and the value of the offer
					  for(int i =0; i< sellersNo ; i++){
						  // strLine is something like "seller_name value"
						  strLine = br.readLine();
						  String[] sellerComponents = strLine.split(" ");
						  String sellerName = sellerComponents[0];
						  String offerValue = sellerComponents[1];
						  
						  // we make a new offer
						  Offer offer = new Offer(productName, sellerName, offerValue);
						  if(offers == null){
							  offers = new ArrayList<Offer>();
						  }
						  offers.add(offer);
					  }
				  }
			  }
			  
			  
		    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		   return offers;
	}

	@Override
	public boolean acceptOffer(String username, Offer offer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean refuseOffer(String username, Offer offer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean makeOffer(String username, Offer offer, String product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelRequest(String username, String product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelOffer(String username, Offer offer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeStatus(String username, Offer offer, String status) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTransferStatus(String buyerUsername, String sellerUsername,
			Offer offer) {
		// TODO Auto-generated method stub
		return 0;
	}

}
