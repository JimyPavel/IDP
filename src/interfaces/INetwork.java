package interfaces;

import classes.*;

// this interfaces the communication between Network module and Mediator module
public interface INetwork {

	// method for checking if exists an user with username and password
	// registered in database
	// returns true if exists or false otherwise
	public boolean userValid(String username, String password);
	
	// method for checking if the user with username is logged in
	// returns true if he is or false otherwise
	public boolean isLogged(String username);
	
	// method for notifying the user with "username" the "status" of the "offer"
	// has been changed.
	public boolean notifyStatusChanged(String username, Offer offer, String status);
	
	public void setIpAndPort();
	
	public void addFileLogging(String fileName);
	
	public void announceOtherUsers();
}
