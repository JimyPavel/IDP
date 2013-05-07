package classes;

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
		try {
			db = new Database(Constants.url, Constants.USER, Constants.PASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String signIn(String username, String password) {
		// we search in database for username and password
		try 
		{
			String query = "SELECT * from user"; 
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
				e1.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public boolean signOut(String username) {
		try 
		{
			String query = "SELECT * from user where username=\""+ username +"\"";
			db.setCommand(query);
			ResultSet rs = db.execute();
			
			while(rs.next()){
				
				// making this user active
				rs.updateString(5, "INACTIVE");
				rs.updateRow();
				db.close();
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
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	@Override
	public ArrayList<Product> loadProducts(String username , String userType) {
		
		ArrayList<Product> products = null;
		
		try{
			String query = "SELECT * from user_service where username=\""+ username + "\"";
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
				e1.printStackTrace();
			}
			return null;
		}
		
		return sellers;
		
	}
	
	public int getPort(String username){

		int port = 40000;
	
		try{
			String query = "SELECT * from user where username=\""+ username + "\"";
			db.setCommand(query);
			ResultSet rs = db.execute();
			
			while(rs.next()){
				port = rs.getInt(7);
				return port;
			}
			return port;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			try {
				db.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return port;
		}
	}
}
