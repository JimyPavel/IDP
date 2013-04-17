package classes;

import java.util.ArrayList;

// class for buyer type user
public class Buyer extends User {

	public Buyer(String username, String password, String userType) {
		super(username, password, userType);
		requests = new ArrayList<Request>();
	}

	// field for all offers he has made
	private ArrayList<Request> requests;

	// getter for offers
	public ArrayList<Request> getRequests() {
		return requests;
	}

	// setter for offers
	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}
	
	
}
