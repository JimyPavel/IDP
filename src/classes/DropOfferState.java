package classes;

import interfaces.IState;

public class DropOfferState implements IState{

	private Network network;
	String details;
	
	public DropOfferState(Network network)
	{
		this.network = network;
	}
	
	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		String message = "[dropOffer]"+details+":"+network.getMediator().getUsername();
		network.WriteToServer(message);
		network.setState(network.getOfferRequestState());
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
