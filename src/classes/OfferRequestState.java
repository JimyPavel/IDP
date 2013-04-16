package classes;

import interfaces.IState;

public class OfferRequestState implements IState{

	private Network network;
	private String details;
	
	public OfferRequestState(Network network)
	{
		this.network = network;
	}
	
	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		String message = "[offerRequest]" + details;
		network.WriteToServer(message);
	}

	@Override
	public void parseInformation(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		this.details = details;
	}

}
