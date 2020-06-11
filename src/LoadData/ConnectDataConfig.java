package LoadData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ConnectDataConfig {

	DBConnection db;
	PreparedStatement pre;

	public ConnectDataConfig() {
		db = new DBConnection();
	}

	public String readFileFromFolder(String file) {
		String result = "";
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), "UTF-8"));
			String line = null;
			while ((line = bf.readLine()) != null) {
				result += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public Connection connectConfigDatabase() {
		String result;
		Connection connection = null;
		try {
			result = readFileFromFolder("config/dataconfig.txt");
			String[] connectInfo = result.split("\n");
			String driver = connectInfo[0];
			String url = connectInfo[1];
			String username = connectInfo[2];
			String password = connectInfo[3];
			connection = db.getConnection(driver, url, username, password);
			if (connection != null) {
				System.out.println("connect sucess");
			} else {
				System.out.println("connect fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public ArrayList<ConnectInfor> getConnectionDataConfig() {
		ArrayList<ConnectInfor> listConnect = new ArrayList<ConnectInfor>();
		Connection connection = null;
		try {
			connection = connectConfigDatabase();
			String sql = "SELECT * FROM datawarehouse_configuration.database_connection_configuration;";
			pre = connection.prepareStatement(sql);
			ResultSet re = pre.executeQuery();
			while (re.next()) {
				String driver = re.getString(2);
				String url = re.getString(3);
				String username = re.getString(4);
				String password = re.getString(5);
				ConnectInfor connectInfo = new ConnectInfor(driver, url, username, password);
				listConnect.add(connectInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listConnect;
	}

	public Connection connectDataStaging() {
		ConnectInfor connectInfor = getConnectionDataConfig().get(2);
		System.out.println(connectInfor.getUrl());
		Connection connection = db.getConnection(connectInfor.getDriver(), connectInfor.getUrl(),
								connectInfor.getUsername(), connectInfor.getPassword());
		return connection;
	}
	
	public Connection connectDataWarehouse() {
		ConnectInfor connectInfor = getConnectionDataConfig().get(1);
		System.out.println(connectInfor.getUrl());
		Connection connection = db.getConnection(connectInfor.getDriver(), connectInfor.getUrl(),
								connectInfor.getUsername(), connectInfor.getPassword());
		return connection;
	}

	public static void main(String[] args) {
		ConnectDataConfig connect = new ConnectDataConfig();
		connect.connectConfigDatabase();
		connect.connectDataStaging();
		connect.connectDataWarehouse();
	}
}
