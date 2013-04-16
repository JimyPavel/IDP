package classes;

import java.util.ArrayList;

import interfaces.IState;

public class MakeOfferState implements IState{

	private Network network;
	
	public MakeOfferState(Network network)
	{
		this.network = network;
	}
	
	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseInformation(String info) {
		// TODO Auto-generated method stub
		Network.logger.info("[Seller] Offer request from one client" + info);
		ArrayList<Product> products = network.getMediator().loadProducts(network.getMediator().getUsername(), "Seller");
		String []pieces = info.split("]");
		
		if(pieces.length < 2)
		{
			Network.logger.warn("Wrong message received: " + info);
			return;
		}
		
		for (int i=0; i<products.size(); i++)
		{
			// ofer produsul cerut
			if (products.get(i).getName().equals(pieces[1]))
			{
				network.getMediator().OfferRequestReceived();
			}
		}
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		
	}

}
