package classes;

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
	public void parseInformation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		
	}

}
