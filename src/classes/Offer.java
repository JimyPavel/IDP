package classes;

// this class is modeling the offer object
public class Offer {

	// the product/service the offer is available for
	private Product product;
	
	// the seller who is making this offer
	private User seller;
	
	// public getter for product field
	public Product getProduct() {
		return product;
	}

	// public setter for product field
	public void setProduct(Product product) {
		this.product = product;
	}

	// public getter for seller field
	public User getSeller() {
		return seller;
	}

	// public setter for seller field
	public void setSeller(User seller) {
		this.seller = seller;
	}

	// TODO: other relevant fields for "offer" object
}
