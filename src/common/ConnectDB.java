package common;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

	public static Connection getConectionControl(String user, String pass) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/warehouse_control?useSSL=false&useTimezone=true&serverTimezone=UTC";
			return DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			System.out.println(e);
			// TODO Auto-generated catch block
			System.out.println("Conneciton connectionConfig failure!");
			return null;
		}

	}

	public static Connection getConnection(String urlBasic, String driver, String nameDatabase, String userName,
			String password) throws SQLException {
		try {
			String url = urlBasic + nameDatabase;
			Class.forName(driver);
			return DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Connection database failure");
			return null;
		}

	}
	
	public static Connection getConnection(String db, String username, String password) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/" + db + "?useSSL=false&useTimezone=true&serverTimezone=UTC";
			return DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Connection database failure");
			return null;
		}

	}
	
	public static Connection getConnect(String db, String username, String password) {
		Connection connection = null;
		
		String url = "jdbc:mysql://localhost:3306" + "/" + db +
					"?useSSL=false&useTimezone=true&serverTimezone=UTC&" +
					"useUnicode=true&characterEndcoding=utf-8";
		try {
			connection = DriverManager.getConnection(url, username, password);
			
		} catch(SQLException e) {	
			System.out.println("Opps, error!\n"+e.getMessage());
		}

		return connection;
	}

	public static void main(String[] args) throws SQLException {
		
		Connection conn = getConectionControl("root","" );
		
		if(conn != null) System.out.println("yes man");
		else System.out.println("not ok");

		

	}


//	public static void main(String[] args) throws SQLException {
//		if(getConectionControl("root", "chkdsk") != null) System.out.println("ok");;
//	}
}
