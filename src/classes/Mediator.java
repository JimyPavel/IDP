package classes;
import java.util.ArrayList;

import interfaces.*;

public class Mediator implements IMediator{
	
	@SuppressWarnings("unused")
	private IGui gui;
	private IWeb web;
	private INetwork network;
		
	public void registerGui(IGui gui){
		this.gui = gui;
	}
	
	public void registerWeb(IWeb web){
		this.web = web;
	}
	
	public void registerNetwork(INetwork network){
		this.network = network;
	}
	
	@Override
	// we return the type of the user
	// or null if there is no user with this details
	public String signIn(String username, String password) {
		return web.signIn(username, password);
	}

	@Override
	// for now this method will only return true
	public boolean signOut(String username) {
		return web.signOut(username);
	}

	@Override
	public ArrayList<Product> loadProducts(String username , String userType) {		
		return web.loadProducts(username, userType);
	}

	@Override
	public boolean acceptOffer(String username, Offer offer) {
		// TODO:  network.acceptOffer() 
		return true;
	}

	@Override
	public boolean refuseOffer(String username, Offer offer) {
		// TODO:  network.acceptOffer() 
		return true;
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

	@Override
	public ArrayList<String> getSellers(String productName) {
		return web.getSellers(productName);
	}
	
	@Override
	public ArrayList<String> getBuyers(String productName){
		return web.getBuyers(productName);
	}

	@Override
	public void startTransfer(String buyer, String seller, Offer offer) {
		web.startTransfer(buyer,seller,offer);
		
	}

	@Override
	public void setIpAndPort(String userType) {
		// TODO Auto-generated method stub
		network.setIpAndPort(userType);
	}

	@Override
	public void setLoggerFile(String name) {
		// TODO Auto-generated method stub
		network.addFileLogging(name+"Log.txt");
	}

	@Override
	public void LaunchOfferRequest(String product) {
		// TODO Auto-generated method stub
		network.LaunchOfferRequest(product);
	}
	
	

}
