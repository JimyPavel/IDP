package classes;

// this class is modeling the request object
public class Request {
	
	// constructor
	public Request(String productName, String buyerName) {
		this.productName = productName;
		this.buyerName = buyerName;
		this.offer = null;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	// field for product Name
	private String productName;
	// field for buyer Name
	private String buyerName;
	// field for the offer for this request
	private Offer offer;
	
	

}
