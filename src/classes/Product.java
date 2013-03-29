package classes;

// class for modeling the product object
public class Product {
	
	// product name
	private String name;
	
	// constructor
	public String getName() {
		return name;
	}

	// getter for product name
	public void setName(String name) {
		this.name = name;
	}

	// setter for product name
	public Product(String name){
		this.name = name;
	}
	
	
}
