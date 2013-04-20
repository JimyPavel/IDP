package classes;

import interfaces.IState;

public class WaitingOffer implements IState{

	private Network network;
	String details;
	
	public WaitingOffer(Network network)
	{
		this.network = network;
	}
	
	
	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		Network.logger.info("[WaitingOfferState] "+ details);
		String []info = details.split(":");
		if(info.length < 5)
		{
			return;
		}
		
		String message;
		if(info[4].equals("true"))
		{
			message = "[offerAccepted]" + info[0]+":"+info[1]+":"+info[2]+":"+info[3];
		}
		else
		{
			message = "[refusedOffer]" + info[0]+":"+info[1]+":"+info[2]+":"+info[3];
		}
		network.WriteToServer(message);
	}

	@Override
	public void parseInformation(String info) {
		// TODO Auto-generated method stub
		Network.logger.info("[WaitingOfferState] Message received: "+ info);
		String []infos = info.split(":");
		
		if(infos.length < 4)
		{
			Network.logger.warn("[WaitingOfferState] Wrong message received: " + info);
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
