package classes;

import java.util.ArrayList;

import interfaces.IState;

public class MakeOfferState implements IState{

	private Network network;
	String details;
	
	public MakeOfferState(Network network)
	{
		this.network = network;
	}
	
	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		
		// buyer, product, value, seller
		String message = "[offerMade]" + details;
		network.WriteToServer(message);
		network.setState(network.getWaitingAcceptState());
	}

	@Override
	public void parseInformation(String info) {
		// TODO Auto-generated method stub
		Network.logger.info("[MakeOfferState] Offer request from one client " + info);
		ArrayList<Product> products = network.getMediator().loadProducts(network.getMediator().getUsername(), "Seller");
		String []pieces = info.split("]");
		
		if(pieces.length < 2)
		{
			Network.logger.warn("[MakeOfferState] Wrong message received: " + info);
			return;
		}
		
		String []details = pieces[1].split(":");
		
		if(details.length < 2)
		{
			Network.logger.warn("[MakeOfferState] Wrong message received: " + info);
			return;
		}
		
		for (int i=0; i<products.size(); i++)
		{
			//Network.logger.debug("Product: " + products.get(i).getName());
			// ofer produsul cerut
			if (products.get(i).getName().equals(details[0]))
			{
				//Network.logger.debug("Am gasit produs "+details[0]+ " buyer "+ details[1]);
				network.getMediator().OfferRequestReceived(details[0], details[1]);
			}
		}
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		this.details = details;
	}

}
