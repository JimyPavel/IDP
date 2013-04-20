package classes;

// this class is modeling the offer object
public class Offer {
	
	public Offer(String product , String seller , String value){
		this.product = product;
		this.seller = seller;
		this.value = value;
		this.isAccepted = false;
		this.transferFail = false;
		this.transferMade = false;
		this.transferInProgress = false;
	}

	// the product/service the offer is available for
	private String product;
	
	// the seller who is making this offer
	private String seller;
	
	// the value of the offer
	private String value;
	
	// the flag for an accepted offer or not
	private boolean isAccepted;
	
	// the flag for a refused offer
	private boolean isRefused;
	
	private boolean transferMade;
	
	private boolean transferFail;
	
	private boolean transferInProgress;

	public boolean isTransferMade(){
		return transferMade;
	}
	
	public void setTransferMade(boolean made){
		transferMade = made;
	}
	
	public boolean isTransferFailed()
	{
		return transferFail;
	}
	
	public void setTransferFailed(boolean fail)
	{
		transferFail = fail;
	}
	
	public boolean isTransferInProgress()
	{
		return transferInProgress;
	}
	
	public void setTransferInProgress(boolean progress)
	{
		transferInProgress = progress;
	}
	
	public boolean isRefused() {
		return isRefused;
	}

	public void setRefused(boolean isRefused) {
		this.isRefused = isRefused;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	// public getter for product field
	public String getProduct() {
		return product;
	}

	// public setter for product field
	public void setProduct(String product) {
		this.product = product;
	}

	// public getter for seller field
	public String getSeller() {
		return seller;
	}

	// public setter for seller field
	public void setSeller(String seller) {
		this.seller = seller;
	}
	
	public boolean getIsAccepted(){
		return isAccepted;
	}
	
	// public setter for isAccepted field
	public void setIsAccepted(boolean flag){
		this.isAccepted = flag;
	}

}
