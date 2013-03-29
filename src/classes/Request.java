package classes;

// this class is modeling the request object
public class Request {
	
	// constructor
	public Request(String productName, String buyerName) {
		this.productName = productName;
		this.buyerName = buyerName;
		this.offer = null;
	}
	
	// getter for productName
	public String getProductName() {
		return productName;
	}
	
	// setter for productName
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	// getter for buyer name
	public String getBuyerName() {
		return buyerName;
	}
	
	// setter for buyer name
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	// getter for offer
	public Offer getOffer() {
		return offer;
	}

	// setter for offer
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
