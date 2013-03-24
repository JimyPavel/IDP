package interfaces;

import java.util.ArrayList;

import classes.Offer;
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
}
