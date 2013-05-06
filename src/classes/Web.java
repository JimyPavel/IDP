package classes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import classes.Constants;
import classes.Database;

import interfaces.IMediator;
import interfaces.IWeb;

public class Web implements IWeb {
	
	private IMediator mediator;
	private Database db;
	
	public Web(IMediator mediator)
	{
		this.mediator = mediator;
		this.mediator.registerWeb(this);
	}

	@SuppressWarnings("finally")
	@Override
	public String signIn(String username, String password) {
		// we search in database for username and password
		try 
		{
			String query = "SELECT * from user"; /*where username=\""+ username + "\"" + 
			"AND password=\"" + password + "\"";*/
			db = new Database(Constants.url, Constants.USER, Constants.PASS);
			db.setCommand(query);
			ResultSet rs = db.execute();
			
			while(rs.next()){
				String us = rs.getString(2);
				String pass = rs.getString(3);
				String type = rs.getString(4);
				if(us.equals(username) && pass.equals(password)){
					// making this user active
					rs.updateString(5, "ACTIVE");
					rs.updateRow();
					
					// returning the type
					return type;
				}
			}
			
			return null;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			try {
				db.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			try {
				db.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		/*try{
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
		    }*/
	}

	@Override
	public boolean signOut(String username) {
		try 
		{
			String query = "SELECT * from user where username=\""+ username +"\"";
			db = new Database(Constants.url, Constants.USER, Constants.PASS);
			db.setCommand(query);
			ResultSet rs = db.execute();
			
			while(rs.next()){
				
				// making this user active
				rs.updateString(5, "INACTIVE");
				rs.updateRow();
				
				// returning true
				return true;
			}
			
			return false;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			try {
				db.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			try {
				db.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	@Override
	public ArrayList<Product> loadProducts(String username , String userType) {
		
		ArrayList<Product> products = null;
		/*try{
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
			  in.close();
			  
		    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			 }*/
		
			try{
				String query = "SELECT * from user_service where username=\""+ username + "\"";
				db = new Database(Constants.url, Constants.USER, Constants.PASS);
				db.setCommand(query);
				ResultSet rs = db.execute();
				
				while(rs.next()){
					String serviceName = rs.getString(3);
					Product product = new Product(serviceName);
				    if(products == null){
					  products = new ArrayList<Product>();
				    }
				    products.add(product);
				}
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
				try {
					db.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				try {
					db.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
			
		    return products;
	}

	@Override
	public ArrayList<String> getSellers(String productName) {
		
		ArrayList<String> sellers = null;
		ArrayList<String> userNames = new ArrayList<String>();
		
		try{
			String query = "SELECT * from user_service where servicename=\""+ productName + "\"";
			db = new Database(Constants.url, Constants.USER, Constants.PASS);
			db.setCommand(query);
			ResultSet rs = db.execute();
			
			// luam toate numele utilizatorilor ce doresc/ofera acest serviciu
			while(rs.next()){
				String userName = rs.getString(2);
				userNames.add(userName);
			}
			
			// luam utilizatorii care sunt vanzatori
			query = "SELECT * from user where type=\"seller\"";
			db.setCommand(query);
			rs = db.execute();
			
			while(rs.next()){
				// daca vanzatorul este in lista de useri ce detin/doresc acest serviciu
				// il adaugam
				String uName = rs.getString(2);
				if(userNames.contains(uName)){
					if(sellers == null){
						sellers = new ArrayList<String>();
					}
					sellers.add(uName);
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			try {
				db.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			try {
				db.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		/*
		// search in setup.txt for sellers
		FileInputStream fstream ;
		DataInputStream in;
		BufferedReader br;
		String strLine;
		
		try{
			fstream = new FileInputStream("setup.txt");
			// Get the object of DataInputStream
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			
			// citeste adresa serverului 
			strLine = br.readLine();
			while(strLine!=null && strLine != System.getProperty("line.separator") && strLine != " "){
				
				String pieces[] = strLine.split(" ");
				
				if(pieces.length == 4)
				{
					if(pieces[3].equals("Seller")){
						String sellerFile = pieces[2] + ".txt";
						sellersFiles.add(sellerFile);
					}
				}
				strLine = br.readLine();
			}
			br.close();
		}
		catch(IOException e){
			
		}
		
		
		// parse the sellers files 
		for(int i=0 ; i< sellersFiles.size() ; i++){
			try{
			  String path = sellersFiles.get(i);
			  // Open the file that is the first 
			  // command line parameter
			  fstream = new FileInputStream(path);
			  // Get the object of DataInputStream
			  in = new DataInputStream(fstream);
			  br = new BufferedReader(new InputStreamReader(in));
			  
			  
			  // we read the products 
			  while((strLine=br.readLine()) != null){
				  // strLine is something like "service:name" or "offer:name"
				  // name is the name of the service/product
				  String[] components = strLine.split(":");
				  String p = components[1];
				  
				  
				  // if we find the service that we are looking for
				  // then we add the seller to the list
				  if(p.equals(productName)){
					  String[] aux = path.split(".txt");
					  String sellerName = aux[0];
					  
					  if(sellers == null){
						  sellers = new ArrayList<String>();
					  }
					  sellers.add(sellerName);
					  break;
				  }
			  }
			  in.close();
		    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			 }
			    
		}*/
		
		return sellers;
		
	}
	
	@Override
	public ArrayList<String> getBuyers(String productName){
		
		// for first homework we return an array with known buyers
		ArrayList<String> buyers = new ArrayList<String>();
		buyers.add("jimy");
		
		return buyers;
	}


}
