package interfaces;

import java.util.ArrayList;

import classes.*;

// this interfaces the communication between GUI module and Mediator module
public interface IMediator {

	// method for signing into the application
	// method receives the username and the password
	// and returns the type of the user : "buyer"/"seller"
	// or null if there is no user with this details
	public String signIn(String username,String password);
	
	// method for signing out
	// returns true if everything is fine, false otherwise
	public boolean signOut(String username);
	
	// method for loading the products from database
	// to GUI module for a specific user
	// method receives the username for this user
	// and the userType
	// returns an ArrayList of products
	public ArrayList<Product> loadProducts(String username ,String userType);
	
	// method for accepting an offer
	// this method will be called by the Buyer with user name = "username"
	// he will accept the offer made by a Seller
	// returns true if the offer was successfully accepted, or false otherwise
	public boolean acceptOffer(String username, Offer offer);
	
	// method for refusing an offer
	// this method will be called by the Buyer with user name = "username"
	// he will refuse the offer made by a Seller
	public void refuseOffer(String buyer, String seller, String productName, String value);
	
	// method for making an offer for a specific product
	// this method will be called by a Seller identified by "username"
	// returns true if the offer was successfully made, or false otherwise
	public void makeOffer(String username, Offer offer, String product);
	
	// method for canceling the request for a specific product/service
	// this method will be called by a Buyer who requested a product/service
	// and now he is changing his mind.
	// the user is identified by his "username" 
	// the product is identified by the "product" parameter
	// returns true if the request for a product was successfully canceled
	// or false otherwise
	public boolean cancelRequest(String username, String product);
	
	// method for canceling an offer for a Product
	// this method will be called by a Seller
	// returns true if the offer was successfully canceled, or false otherwise
	public boolean cancelOffer(String username, Offer offer);
	
	// method for changing the status for an offer 
	// status can be No Offer , Offer Made,Offer Exceeded ,Offer Accepted
	// Offer Refused , Transfer started , Transfer in progress
	// Transfer completed, Transfer failed 
	// returns true if the status was successfully changed or false otherwise
	public boolean changeStatus(String username, Offer offer, String status);
	
	// method for inspecting the transfer status after an user accepted an offer
	// method returns a value between 0 and 100 , representing in percentages
	// the status of this transfer
	// buyerUsername = username of the buyer who accepted the offer
	// sellerUsername = username of the seller who made this offer
	// offer = offer object which is accepted by the Buyer
	public int getTransferStatus(String buyerUsername, String sellerUsername, Offer offer);
	
	// this method will register a gui object
	public void registerGui(IGui gui);
	
	// this method will register a web object
	public void registerWeb(IWeb web);
	
	// this method will register a web object
	public void registerNetwork(INetwork network);
	
	// this method will return a list of sellers who offer this product
	public ArrayList<String> getSellers(String productName);
	
	public void startTransfer(String buyer, String seller, String product, String value);
	
	// this method will create a file for logging, different for each user
	public void setLoggerFile(String name);
	
	public void setIpAndPort(String userType);
	
	public void LaunchOfferRequest(String product);
	
	public String getUsername();
	
	public void OfferRequestReceived(String product, String buyer);
	
	public void OfferReceived(String product, String value, String seller);
	
	public void OfferAccepted(String buyer, String product, String value);
	
	public void DropOfferRequest(String productName);
	
	public void DropOffer(String productName, String buyer);
	
	public void OfferRefused(String buyer, String product, String value);
	
	public void signOutAnnounce(String username);
	
	public int getPort(String username);
	
	public void transfer(String user, String product);
}
