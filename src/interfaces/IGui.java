package interfaces;

public interface IGui {
	
	public String getUsername();
	
	public void OfferRequestReceived(String product, String buyer);
	
	public void OfferReceived(String product, String value, String seller);
	
	public void OfferAccepted(String buyer, String product, String value);
	
	public void DropOffer(String productName, String buyer);
	
	public void OfferRefused(String buyer, String product, String value);
}
