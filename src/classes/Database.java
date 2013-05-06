package classes;
import java.sql.*;

import static classes.Constants.*;

public class Database {
	
	private Connection			conn	= null;
	private PreparedStatement	stmt	= null;
	
	
	public Database(String url, String user, String pass) throws SQLException, ClassNotFoundException {
		//String url = "jdbc:mysql://121.0.0.1:3306/mydatabase";
		
		conn = DriverManager.getConnection(url, user, pass);
		// TODO 1.2: obtain connection
	}
	
	public void setCommand(String command) throws SQLException {
		
		// TODO 1.3: free existing statement, if any
		if(stmt != null){
			stmt.close();
		}
		
		// TODO 1.3: prepare statement based on given command
		stmt = conn.prepareStatement(command,ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
	}
	
	public ResultSet execute() throws SQLException {
		
		// TODO 1.4: execute query and return result
		return stmt.executeQuery();
	}
	
	public void close() throws SQLException {
		
		// TODO 1.5: close connection
		conn.close();
		// TODO 1.5: close statement
		stmt.close();
	}
	
}
