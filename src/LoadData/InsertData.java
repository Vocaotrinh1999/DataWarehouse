package LoadData;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import Model.ControlModel;
import Model.LogModel;

public class InsertData {
	ConnectDataConfig connectDataConfig;
	PreparedStatement pre, pre2, pre3;
	Connection connection, connection2;
	ResultSet result;

	public InsertData() {
		// connection = new DAO().openConnection();
		connectDataConfig = new ConnectDataConfig();
	}

	// lay thong tin databasecontrol table
	private ArrayList<ControlModel> getControlModel() {
		ArrayList<ControlModel> controls = new ArrayList<ControlModel>();
		String sql = "SELECT * FROM datawarehouse_configuration.database_control";
		connection = connectDataConfig.connectConfigDatabase();
		try {
			pre = connection.prepareStatement(sql);
			result = pre.executeQuery();
			while (result.next()) {
				int id = result.getInt(1);
				String fileName = result.getString(2);
				String fileLocation = result.getString(3);
				String targetTable = result.getString(4);
				String fileType = result.getString(5);
				String delimeter = result.getString(6);
				String importDir = result.getString(7);
				String sucessDir = result.getString(8);
				String errorDir = result.getString(9);
				String columList = result.getString(10);
				ControlModel model = new ControlModel(id, fileName, fileLocation, targetTable, fileType, delimeter,
						importDir, sucessDir, errorDir, columList);
				controls.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controls;
	}

	// lấy các file trong thư mục data đã dowload đem ra thêm vào log
	public void insertToLog() {
		String sql = "insert into datawarehouse_configuration.log(file_name,file_location,load_staging_status,load_datawarehouse_status) values(?,?,?,?);";
		connection = connectDataConfig.connectConfigDatabase();
		ArrayList<ControlModel> controls = getControlModel();
		try {
			pre = connection.prepareStatement(sql);
			for (ControlModel control : controls) {
				pre.setString(1, control.getFileName());
				pre.setString(2, control.getFileLocation());
				pre.setString(3, "NR");// Not Ready for extract
				pre.setString(4, "NR");// Not Ready for transform
				pre.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("insert thanh cong");
	}

	// lấy nội dung bảng log lên
	public ArrayList<LogModel> getLog() {
		ArrayList<LogModel> logs = new ArrayList<LogModel>();
		connection = connectDataConfig.connectConfigDatabase();
		String sql = "SELECT * FROM datawarehouse_configuration.log;";
		try {
			pre = connection.prepareStatement(sql);
			result = pre.executeQuery();
			while (result.next()) {
				int id = result.getInt(1);
				String fileName = result.getString(2);
				String fileLocation = result.getString(3);
				String loadStagingStatus = result.getString(4);
				String loadDataWarehouseStatus = result.getString(5);
				LogModel log = new LogModel(id, fileName, fileLocation, loadStagingStatus, loadDataWarehouseStatus);
				logs.add(log);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

	// lấy nội dung các text lấy được đem vào lưu trong database stagging và cập
	// nhật log
	public void addText() {
		// lay noi dung file text co trng thai satus bang NR dem vao insert vao staging
		String sql = "insert into datawarehouse_staging.staging(text) value(?)";
		// lay ra thong tin ten va dia chi file text co trang thai NR
		String sql2 = "select * from datawarehouse_configuration.log where load_staging_status='NR';";
		// cap nhat trang thai bang cach them vao 1 dong tuong tu voi trang thai bang ER
		String sql3 = "update datawarehouse_configuration.log set load_staging_status =? where load_staging_status='NR';";
		connection = connectDataConfig.connectDataStaging();
		connection2 = connectDataConfig.connectConfigDatabase();
		try {

			pre2 = connection2.prepareStatement(sql2);
			result = pre2.executeQuery();
			ArrayList<LogModel> log = getLog();
			for (LogModel logModel : log) {
				pre = connection.prepareStatement(sql);
				String text = connectDataConfig.readFileFromFolder(logModel.getFileLocation());
				String[] data = text.split("\n");
				for (String d : data) {
					if (!d.startsWith("STT") || !d.startsWith("")) {
						pre.setString(1, d);
						pre.executeUpdate();
					}
				}
				pre3 = connection2.prepareStatement(sql3);
				pre3.setString(1, "ER");
				// pre3.executeUpdate();
				System.out.println("sucess");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// xoa du lieu trung truoc khi load qua dw
	public void deleteDuplicate() {
		connection = connectDataConfig.connectDataStaging();
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

	// lưu các đối tượng lấy từ staging đem xử lý rồi lưu vào data warehouse
	// cập nhật lại trạng cho load_datawarehouse trong log là TR (Transform Ready)
	public void insertToDataWareHouse() {
		ArrayList<String> listEmp = loadTextFromStaging();
		connection = connectDataConfig.connectConfigDatabase();
		connection2 = connectDataConfig.connectDataWarehouse();
		//String sql0 = "select colum_list from datawarehouse_configuration.database_control where target_table = 'student';";
		String sql1 = "insert into datawarehouse.data_warehouse value(?,?,?,?,?,?,?,?,?,?)";
		String sql2 = "update datawarehouse_configuration.log set load_staging_status =? where load_datawarehouse_status='NR';";
		try {
			for (String line : listEmp) {
				pre = connection2.prepareStatement(sql1);
				String[] arr = line.split(",");
				int stt = Integer.parseInt(arr[0]);
				int mssv = Integer.parseInt(arr[1]);
				String firstName = arr[2];
				String lastName = arr[3];
				Date date = Date.valueOf(arr[4]);
				String className = arr[5];
				String phoneNumber = arr[6];
				String email = arr[7];
				String city = arr[8];
				String note = arr[9];
				
				pre.setInt(1, stt);
				pre.setInt(2, mssv);
				pre.setString(3, firstName);
				pre.setString(4, lastName);
				pre.setDate(5, date);
				pre.setString(6, className);
				pre.setString(7, phoneNumber);
				pre.setString(8, email);
				pre.setString(9, city);
				pre.setString(10, note);
				pre.executeUpdate();
			}
			pre2 = connection.prepareStatement(sql2);
			pre2.setString(1, "TR");
			pre2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("insert to data warehouse sucess");
	}

	public static void main(String[] args) {
		InsertData insert = new InsertData();
		/*
		 * ArrayList<ControlModel> controls = insert.getControlModel(); for
		 * (ControlModel controlModel : controls) {
		 * System.out.println(controlModel.toString()); }
		 */
		
		 //ArrayList<String> loadStaging = insert.loadTextFromStaging(); for (String st
		  //: loadStaging) { System.out.println(st); }
		 
		// insert.insertToLog();
		//insert.addText();
		insert.insertToDataWareHouse();
	}
}
