package classes;

import interfaces.IState;

public class WaittingOffer implements IState{

	private Network network;
	String details;
	
	public WaittingOffer(Network network)
	{
		this.network = network;
	}
	
	
	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		Network.logger.info("[Client] "+ details);
		String message = "[offerAccepted]" + details;
		network.WriteToServer(message);
	}

	@Override
	public void parseInformation(String info) {
		// TODO Auto-generated method stub
		Network.logger.info("[Client] "+ info);
		String []infos = info.split(":");
		
		if(infos.length < 4)
		{
			Network.logger.warn("Wrong message received: " + info);
			return;
		}
		
		// buyer, product, value, seller
		network.getMediator().OfferReceived(infos[1], infos[2], infos[3]);
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		this.details = details;
	}

}
