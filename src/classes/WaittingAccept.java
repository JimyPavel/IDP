package classes;

import interfaces.IState;

public class WaittingAccept implements IState{

	private Network network;
	String details;
	
	public WaittingAccept(Network network)
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
		Network.logger.info("[WaittingAcceptState] Message received: "+ info);
		
		String []pieces = info.split("]");
		
		if(pieces.length < 2)
		{
			Network.logger.warn("[WaittingAcceptState] Wrong message received: " + info);
			return;
		}
		
		// drop offer
		if(pieces[0].equals("[dropOffer]")){
			String []infos = pieces[1].split(":");
			if(infos.length < 2)
			{
				Network.logger.warn("[WaittingAcceptState] Wrong message received: " + info);
				return;
			}
			// produc, buyer
			network.getMediator().DropOffer(infos[0], infos[1]);
		}
		else{
			
			String []infos = pieces[1].split(":");
			
			if(infos.length < 4)
			{
				Network.logger.warn("[WaittingAcceptState] Wrong message received: " + info);
				return;
			}
			
			// buyer, seller, product, value
			network.getMediator().OfferAccepted(infos[0], infos[2], infos[3]);
		}
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		
	}

	
}
