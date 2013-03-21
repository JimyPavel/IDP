package interfaces;

import java.util.ArrayList;
import classes.*;

// this interfaces the communication between GUI module and Mediator module
public interface IGUIMediator {

	// method for signing into the application
	// method receives the username and the password
	// and returns the type of the user : "buyer"/"seller"
	public String signIn(String username,String password);
	
	// method for signing out
	// returns true if everything is fine, false otherwise
	public boolean signOut(String username);
	
	// method for loading the products from database
	// to GUI module for a specific user
	// method receives the username for this user
	// returns an ArrayList of Products
	// if the user is a buyer , the products returned represents
	// the products for which he requested an offer
	// if the user is a seller, the products returned represents
	// the products for which he made an offer
	public ArrayList<Product> loadProducts(String username);
	
	// method for accepting an offer
	// this method will be called by the Buyer with user name = "username"
	// he will accept the offer made by a Seller
	// returns true if the offer was successfully accepted, or false otherwise
	public boolean acceptOffer(String username, Offer offer);
	
	// method for refusing an offer
	// this method will be called by the Buyer with user name = "username"
	// he will refuse the offer made by a Seller
	// returns true if the offer was successfully rejected, or false otherwise
	public boolean refuseOffer(String username, Offer offer);
	
	// method for making an offer for a specific product
	// this method will be called by a Seller identified by "username"
	// returns true if the offer was successfully made, or false otherwise
	public boolean makeOffer(String username, Offer offer, Product product);
	
	// method for canceling the request for a specific product/service
	// this method will be called by a Buyer who requested a product/service
	// and now he is changing his mind.
	// the user is identified by his "username" 
	// the product is identified by the "product" parameter
	// returns true if the request for a product was successfully canceled
	// or false otherwise
	public boolean cancelRequest(String username, Product product);
	
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
}