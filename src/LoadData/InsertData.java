package LoadData;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class InsertData {
	ReadData read = null;
	ConnectDataConfig connectDataConfig;
	PreparedStatement pre, pre2;
	Connection connection, connection2;
	ResultSet result;

	public InsertData() {
		// connection = new DAO().openConnection();
		read = new ReadData();
		connectDataConfig = new ConnectDataConfig();
	}

	public String readFromDataFolder() {
		String result = "";
		File file = new File("data");
		File[] listFile = file.listFiles();
		for (File file2 : listFile) {
			result += connectDataConfig.readFileFromFolder(file2.getPath());
		}
		return result;
	}

	// lấy nội dung các text lấy được đem vào lưu trong database stagging và ghi log
	public void addText() { // add text from source to database stagging
		String result = readFromDataFolder();
		String sql = "insert into datawarehouse_staging.staging(text) value(?)";
		String sql2 = "insert into datawarehouse_configuration.log(file_name,status) values(?,'ER')";
		String[] data = result.split("\n");
		connection = connectDataConfig.connectDataStaging();
		connection2 = connectDataConfig.connectConfigDatabase();
		try {
			pre = connection.prepareStatement(sql);
			pre2 = connection2.prepareStatement(sql2);
			for (String d : data) {
				if (!d.startsWith("Emp")) { // loai bo header
					pre.setString(1, d);
					pre.executeUpdate();
				}
			}
			File file = new File("data"); // ghi log
			File[] listFile = file.listFiles();
			for (File file2 : listFile) {
				pre2.setString(1, file2.getPath());
				pre2.executeUpdate();
			}
			System.out.println("sucess");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// lấy nội dung các đoạn text đã lưu trong csdl để chuyển đổi thành object
	public ArrayList<String> loadTextFromStaging() {
		ArrayList<String> listST = new ArrayList<String>();
		connection = connectDataConfig.connectDataStaging();
		String sql = "SELECT text FROM datawarehouse_staging.staging";
		try {
			pre = connection.prepareStatement(sql);
			result = pre.executeQuery();
			while (result.next()) {
				listST.add(result.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listST;
	}

	// lưu các đối tượng đã xử lý vào database
	public void addObject() {
		ArrayList<String> listEmp = loadTextFromStaging();
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse.data_warehouse value(?,?,?,?,?,?,?,?)";
		try {
			pre = connection.prepareStatement(sql);
			for (String line : listEmp) {
				String[] arr = line.split(",");
				int id = Integer.parseInt(arr[0]);
				String firstName = arr[1];
				String lastName = arr[2];
				String email = arr[3];
				String[] date = arr[4].split("/");
				LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[0]),
						Integer.parseInt(date[1]));
				double salary = Double.parseDouble(arr[5]);
				String phoneNumber = arr[6];
				String city = arr[7];
				pre.setInt(1, id);
				pre.setString(2, firstName);
				pre.setString(3, lastName);
				pre.setString(4, email);
				pre.setDate(5, Date.valueOf(dateOfBirth));
				pre.setDouble(6, salary);
				pre.setString(7, phoneNumber);
				pre.setString(8, city);
				pre.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("insert sucess");
	}

	public static void main(String[] args) {
		InsertData insert = new InsertData();
		insert.addText();
		/*
		ArrayList<String> loadStaging = insert.loadTextFromStaging();
		for (String st : loadStaging) {
			System.out.println(st);
		}
		*/
		insert.addObject();
	}
}
