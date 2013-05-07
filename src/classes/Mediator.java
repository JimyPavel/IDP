package classes;
import java.util.ArrayList;

import interfaces.*;

public class Mediator implements IMediator{
	
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
	public boolean signOut(String username) {
		return web.signOut(username);
	}

	@Override
	public ArrayList<Product> loadProducts(String username , String userType) {		
		return web.loadProducts(username, userType);
	}

	@Override
	public boolean acceptOffer(String username, Offer offer) {
		return true;
	}

	@Override
	public void refuseOffer(String buyer, String seller, String productName, String value) {
		network.refuseOffer(buyer, seller, productName, value);
	}

	@Override
	public void makeOffer(String username, Offer offer, String product) {
		network.makeOffer(username, offer, product);
	}
	
	@Override
	public void OfferReceived(String product, String value, String seller){
		gui.OfferReceived(product, value, seller);
	}

	@Override
	public boolean cancelRequest(String username, String product) {
		return false;
	}

	@Override
	public boolean cancelOffer(String username, Offer offer) {
		return false;
	}

	@Override
	public boolean changeStatus(String username, Offer offer, String status) {
		return false;
	}

	@Override
	public int getTransferStatus(String buyerUsername, String sellerUsername, Offer offer) {
		return 0;
	}

	@Override
	public ArrayList<String> getSellers(String productName) {
		return web.getSellers(productName);
	}

	@Override
	public void startTransfer(String buyer, String seller, String product, String value) {
		network.startTransfer(buyer,seller, product, value);
	}

	@Override
	public void setIpAndPort(String userType) {
		network.setIpAndPort(userType);
	}

	@Override
	public void setLoggerFile(String name) {
		network.addFileLogging(name+"Log.txt");
	}

	@Override
	public void LaunchOfferRequest(String product) {
		network.LaunchOfferRequest(product);
	}

	@Override
	public void DropOfferRequest(String productName){
		network.DropOfferRequest(productName);
	}
	
	@Override
	public void signOutAnnounce(String username){
		network.signOutAnnounce(username);
		// update database
		web.signOut(username);
	}
	
	@Override
	public String getUsername() {
		return gui.getUsername();
	}

	@Override
	public void OfferRequestReceived(String product, String buyer) {
		gui.OfferRequestReceived(product, buyer);
	}

	@Override
	public void OfferAccepted(String buyer, String product, String value) {
		gui.OfferAccepted(buyer, product, value);
	}
	
	@Override
	public void DropOffer(String productName, String buyer){
		gui.DropOffer(productName, buyer);
	}
	
	@Override
	public void OfferRefused(String buyer, String product, String value){
		gui.OfferRefused(buyer, product, value);
	}

	@Override
	public int getPort(String username){
		return web.getPort(username);
	}
	
	public void transfer(String user, String product)
	{
		gui.transfer(user, product);
	}
}
