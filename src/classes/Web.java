package classes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import interfaces.IMediator;
import interfaces.IWeb;

public class Web implements IWeb {
	
	private IMediator mediator;
	
	public Web(IMediator mediator)
	{
		this.mediator = mediator;
		this.mediator.registerWeb(this);
	}

	@Override
	public String signIn(String username, String password) {
		// we search in database for username and password
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("config.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  // Read 3 lines for user details
			  // username
			  strLine = br.readLine();
			  while(strLine!=null && !strLine.equals(username)){
				  br.readLine();
				  br.readLine();
				  strLine = br.readLine();
			  }
			  
			  if(strLine != null){
				  // password
				  strLine = br.readLine();
				  if(!strLine.equals(password)){
					  //Close the input stream
					  in.close();
					  return null;
				  }
				  
				  // type of the user
				  strLine = br.readLine();
				  if(strLine.equals("buyer")){
					  //Close the input stream
					  in.close();
					  return User.BUYER_TYPE;
				  }
				  else if(strLine.equals("seller")){
					  //Close the input stream
					  in.close();
					  return User.SELLER_TYPE;
				  }
				  else {
					  return null;
				  }
			  }
			  else{
				  //Close the input stream
				  in.close();
				  return null;
			  }
			  
		    }catch (Exception e){//Catch exception if any
		    	System.err.println("Error: " + e.getMessage());			    
				return null;
		    }
	}

	@Override
	public boolean signOut(String username) {
		return true;
	}
	
	@Override
	public ArrayList<Product> loadProducts(String username , String userType) {
		
		ArrayList<Product> products = null;
		try{
			  String path = username +".txt";
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream(path);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  // we read the products
			  while((strLine=br.readLine()) != null){
				  // strLine is something like "service:name" or "offer:name"
				  // name is the name of the service/product
				  String[] components = strLine.split(":");
				  String productName = components[1];
				  
				  // this product is inactive (no offers were made)
				  // or the user didn't make an request for the product
				  Product product = new Product(productName);
				  if(products == null){
					  products = new ArrayList<Product>();
				  }
				  products.add(product);
			  }
			  
		    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		   return products;
	}

}
