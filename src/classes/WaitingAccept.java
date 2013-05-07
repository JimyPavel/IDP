package classes;

import interfaces.IState;

public class WaitingAccept implements IState{

	private Network network;
	String details;
	
	public WaitingAccept(Network network)
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
		if(pieces[0].equals("[dropOffer")){
			String []infos = pieces[1].split(":");
			if(infos.length < 2)
			{
				Network.logger.warn("[WaittingAcceptState] Wrong message received: " + info);
				return;
			}
			// produc, buyer
			network.getMediator().DropOffer(infos[0], infos[1]);
		}
		else if(pieces[0].equals("[offerAccepted")){
			
			String []infos = pieces[1].split(":");
			
			if(infos.length < 4)
			{
				Network.logger.warn("[WaitingAcceptState] Wrong message received: " + info);
				return;
			}
			
			// buyer, seller, product, value
			// oferta acceptata
			if(network.getMediator().getUsername().equals(infos[1]))
			{
				network.getMediator().OfferAccepted(infos[0], infos[2], infos[3]);
				network.setState(network.getTransferState());
				
				//start transfer
				network.getState().addDetails(pieces[1]);
				network.getState().sendMessage();
			}
			// oferta neacceptata
			else
			{
				network.getMediator().OfferRefused(infos[0], infos[2], infos[3]);
			}
			
			
		}
		else if(pieces[0].equals("[refusedOffer"))
		{
			String []infos = pieces[1].split(":");
			
			if(infos.length < 4)
			{
				Network.logger.warn("[WaittingAcceptState] Wrong message received: " + info);
				return;
			}
			
			network.getMediator().OfferRefused(infos[0], infos[2], infos[3]);
		}
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		
	}

	
}
