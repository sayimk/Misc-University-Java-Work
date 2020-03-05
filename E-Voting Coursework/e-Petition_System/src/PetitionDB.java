import java.sql.*;


public class PetitionDB {
	
	//use this class for Instanciating the connection to the DB
	
	//local server Details
	
	//private String host="localhost";
	//private String database = "petition";
	//private String port = "3306";
	//private String user = "root";
	//private static String password ="";
	//private String connectionURL="";
	//Connection connection=null;

	//Uni Connection
	private String host="mysql.mcscw3.le.ac.uk";
	private String database = "sk619";
	private String port = "3307";// "3306";
	private String user = "sk619";
	private static String password ="theixahn";
	private String connectionURL="";
	Connection connection=null;
	
	
	public PetitionDB() {
		connectionURL="jdbc:mysql://"+host+":"+port+"/"+database;
	}
	
	public void makeNewConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection(connectionURL,user,password);
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void endConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Connection getConnection() {
		return connection;
	}
	
	
}
