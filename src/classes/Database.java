package classes;
import java.sql.*;


public class Database {
	
	private Connection			conn	= null;
	private PreparedStatement	stmt	= null;
	
	
	public Database(String url, String user, String pass) throws SQLException, ClassNotFoundException {
		conn = DriverManager.getConnection(url, user, pass);
	}
	
	public void setCommand(String command) throws SQLException {
		if(stmt != null){
			stmt.close();
		}
		
		stmt = conn.prepareStatement(command,ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
	}
	
	public ResultSet execute() throws SQLException {
		return stmt.executeQuery();
	}
	
	public void close() throws SQLException {
		conn.close();
		stmt.close();
	}
	
}
