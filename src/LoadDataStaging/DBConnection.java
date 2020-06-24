package LoadDataStaging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	
	private static Connection connection = null;
	//
	//phương thức dùng để kết nối vào csdl
	//use mysql database
	public static Connection getConnection(String driver,String url,String username,String password) {
		if(connection != null) {
			return connection;
		}else {
			try {
				Class.forName(driver);
				try {
					connection = DriverManager.getConnection(url, username, password);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	public static void main(String[] args) {
		DBConnection db = new DBConnection();
	}
}
