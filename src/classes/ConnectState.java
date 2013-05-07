package classes;

import interfaces.IState;

public class ConnectState implements IState{

	private Network network;
	
	public ConnectState(Network network)
	{
		this.network = network;
	}
	
	@Override
	public void sendMessage() {
		String message = "[connect]" + network.getIp()+":"+ network.getPort()+":"+
						  network.getMediator().getUsername()+":"+network.getUserType();
		network.WriteToServer(message);
		if(network.getUserType().equals("Buyer"))
		{
			network.setState(network.getOfferRequestState());
		}
		else
		{
			network.setState(network.getMakeOfferState());
		}
	}
	
	@Override
	public void parseInformation(String info) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		
	}


}
