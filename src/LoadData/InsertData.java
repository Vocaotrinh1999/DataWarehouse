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
	ConnectDataConfig connectDataConfig;
	PreparedStatement pre, pre2,pre3;
	Connection connection, connection2;
	ResultSet result;

	public InsertData() {
		// connection = new DAO().openConnection();
		connectDataConfig = new ConnectDataConfig();
	}

	// lấy các file trong thư mục data đã dowload đem ra thêm vào log
	public void insertToLog() {
		String sql = "insert into datawarehouse_configuration.log(file_name,file_location,load_staging_status,load_datawarehouse_status) values(?,?,?,?);";
		connection = connectDataConfig.connectConfigDatabase();
		File file = new File("data");
		File[] listFile = file.listFiles();
		try {
			pre = connection.prepareStatement(sql);
			for (File file2 : listFile) {
				pre.setString(1,file2.getName());
				pre.setString(2, file2.getPath());
				pre.setString(3, "NR");//Not Ready for extract
				pre.setString(4, "NR");//Not Ready for extract
				pre.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("insert thanh cong");
	}

	// lấy nội dung các text lấy được đem vào lưu trong database stagging và cập
	// nhật log
	public void addText() {
		//lay noi dung file text co trng thai satus bang NR dem vao insert vao staging
		String sql = "insert into datawarehouse_staging.staging(text) value(?)";
		//lay ra thong tin ten va dia chi file text co trang thai NR
		String sql2 = "select * from datawarehouse_configuration.log where load_staging_status='NR';";
		//cap nhat trang thai bang cach them vao 1 dong tuong tu voi trang thai bang ER
		String sql3 = "update datawarehouse_configuration.log set load_staging_status =? where load_staging_status='NR';";
		connection = connectDataConfig.connectDataStaging();
		connection2 = connectDataConfig.connectConfigDatabase();
		try {
			pre = connection.prepareStatement(sql);
			pre2 = connection2.prepareStatement(sql2);
			result = pre2.executeQuery();
			String fileName = "" ;
			String fileLocation = "";
			while(result.next()) {
				fileName =  result.getString("file_name");
				fileLocation =  result.getString("file_location");
			}
			System.out.println("File Name:"+fileName+"  "+fileLocation);
			String text = connectDataConfig.readFileFromFolder(fileLocation);
			String[] data = text.split("\n");
			for (String d : data) {
				if (!d.startsWith("ID")) { // loai bo header
					pre.setString(1, d);
					pre.executeUpdate();
				}
			}
			pre3 = connection2.prepareStatement(sql3);
			pre3.setString(1, "ER");
			pre3.executeUpdate();
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
		System.out.println("insert to data warehouse sucess");
	}

	public static void main(String[] args) {
		InsertData insert = new InsertData();
		/*
		 * ArrayList<String> loadStaging = insert.loadTextFromStaging(); for (String st
		 * : loadStaging) { System.out.println(st); }
		 */
		//insert.insertToLog();
		//insert.addText();
	}
}
