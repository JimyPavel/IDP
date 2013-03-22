package classes;
import java.util.ArrayList;
import interfaces.*;

public class Mediator implements IGUIMediator{
	

	@Override
	public String signIn(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean signOut(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Product> loadProducts(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean acceptOffer(String username, Offer offer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean refuseOffer(String username, Offer offer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean makeOffer(String username, Offer offer, Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelRequest(String username, Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelOffer(String username, Offer offer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeStatus(String username, Offer offer, String status) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTransferStatus(String buyerUsername, String sellerUsername,
			Offer offer) {
		// TODO Auto-generated method stub
		return 0;
	}

}
