package interfaces;

import java.util.ArrayList;

import classes.Product;

public interface IWeb {

	// method for signing into the application
	// method receives the username and the password
	// and returns the type of the user : "buyer"/"seller"
	// or null if there is no user with this details
	public String signIn(String username, String password);
	
	// method for signing out
	// returns true if everything is fine, false otherwise
	public boolean signOut(String username);
	
	// method for loading the products from database
	// to Mediator module for a specific user
	// method receives the username for this user
	// and the userType
	// returns an ArrayList of products
	public ArrayList<Product> loadProducts(String username ,String userType);
	
	// this method will return a list of sellers who offer this product
	public ArrayList<String> getSellers(String productName);
	
	// this method will return a list of buyers who wants an offer for this product
	public ArrayList<String> getBuyers (String productName);
	
}
