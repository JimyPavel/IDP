package classes;

import java.util.ArrayList;

// class for seller type user
public class Seller extends User {

	public Seller(String username, String password, String userType) {
		super(username, password, userType);
	}

	// field for all offers he has made
	private ArrayList<Offer> offers;

	// getter for offers
	public ArrayList<Offer> getOffers() {
		return offers;
	}

	// setter for offers
	public void setOffers(ArrayList<Offer> offers) {
		this.offers = offers;
	}
	
}
