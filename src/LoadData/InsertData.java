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
	PreparedStatement pre , pre2 ;
	Connection connection, connection2;
	ResultSet result;
	public InsertData() {
		//connection = new DAO().openConnection();
		read = new ReadData();
		connectDataConfig = new ConnectDataConfig();
	}
	public String readFromDataFolder() {
		String result ="";
		File file = new File("data");
		File[] listFile = file.listFiles();
		for (File file2 : listFile) {
			result += connectDataConfig.readFileFromFolder(file2.getPath());
		}
		return result;
	}
	//lấy nội dung các text lấy được đem vào lưu trong database stagging và ghi log
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
			for (int i = 1; i < data.length; i++) { //loai bo header data[0]
				pre.setString(1, data[i]);
				pre.executeUpdate();//cho nay bi nham mai sua sau
			}
			File file = new File("data"); // ghi log
			File[] listFile = file.listFiles();
			for (File file2 : listFile) {
				pre2.setString(1, file2.getPath());
				pre2.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//lấy nội dung các đoạn text đã lưu trong csdl để chuyển đổi thành object
	public ArrayList<String> loadTextFromDatabase() {
		ArrayList<String> listST = new ArrayList<String>();
		String sql = "SELECT datatext FROM datawarehouse.datatext";
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
	
	//lưu các đối tượng đã xử lý vào database
	public int addObject() {
		ArrayList<String> listEmp = loadTextFromDatabase();
		String sql = "insert into datawarehouse.dataobject value(?,?,?,?,?,?,?,?)";
		int kq = 0;
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
				kq = pre.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("insert sucess");
		return kq;
	}

	public static void main(String[] args) {
		InsertData insert = new InsertData();
		 
		/*ArrayList<String> listST = insert.loadTextFromDatabase();
		for (String st : listST) {
			System.out.println(st);
		}
		System.out.println("\n------------------\n");
		System.out.println(insert.addObject());
		*/
		insert.addText();
		System.out.println(insert.readFromDataFolder());
	}
}
