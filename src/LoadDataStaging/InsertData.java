package LoadDataStaging;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import GuiMail.SendMailTLS;
import Model.ControlModel;
import Model.LogModel;

public class InsertData {
	ConnectDataConfig connectDataConfig;
	SendMailTLS sendMail;
	PreparedStatement pre, pre2, pre3;
	Connection connection, connection2;
	ResultSet result;
	String emailSendTo, subject,textMail;
	public InsertData() {
		// connection = new DAO().openConnection();
		connectDataConfig = new ConnectDataConfig();
		sendMail = new SendMailTLS();
		emailSendTo = "17130256@st.hcmuaf.edu.vn";
		subject ="";
		textMail ="";
	}

	public void insertToDBControl() {
		connection = connectDataConfig.connectConfigDatabase();
		String sql = "insert into datawarehouse_configuration.database_control(name,location,target_table,file_type,delimeter,import_dir,sucess_Dir,error_Dir)"
				+ "values(?,?,?,?,?,?,?,?)";
		String importDir = "F:\\anh\\datawarehouse\\data";
		String sucessDir = "F:\\anh\\datawarehouse\\sucess";
		String errorDir = "F:\\anh\\datawarehouse\\error";
		File file = new File(importDir);
		File[] listFile = file.listFiles();
		String textFileName = "";
		int result = 0;
		try {
			for (File f : listFile) {
				pre = connection.prepareStatement(sql);
				String fileName = f.getName();
				textFileName += fileName + "\n";
				String fileLocation = f.getAbsolutePath();
				String targetTable = fileName.split("_")[0];// cat theo _ ten file => sinhvien
				String[] splitFileType = fileName.split("\\.");
				String fileType = splitFileType[splitFileType.length - 1];
				String delimeter = ",";// gia su dieu la file csv
				pre.setString(1, fileName);
				pre.setString(2, fileLocation);
				pre.setString(3, targetTable);
				pre.setString(4, fileType);
				pre.setString(5, delimeter);
				pre.setString(6, importDir);
				pre.setString(7, sucessDir);
				pre.setString(8, errorDir);
				result = pre.executeUpdate();
			}
			if (result > 0) {
				System.out.println("insert sucess");
				textMail = "Đã thêm vào database control các file" + textFileName + "\n vào database control";
				sendMail.sendMail("17130256@st.hcmuaf.edu.vn", "Các file đã insert thành công vào db control", textMail);
			} else {
				System.out.println("insert fail");
				textMail = "Thêm vào database control bị lỗi cần kiểm tra lại";
				sendMail.sendMail("17130256@st.hcmuaf.edu.vn", "load vào db control bị lỗi cần kiểm tra lại", textMail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
				ControlModel model = new ControlModel(id, fileName, fileLocation, targetTable, fileType, delimeter,
						importDir, sucessDir, errorDir);
				controls.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controls;
	}

	// lấy các file đã dowload trong thư mục data đã lưu trong db control đem ra thêm vào log
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

	// xóa dữ liệu trùng trong data staging
	//hai dong du lieu giong nhau chi giu lai 1 dong
	public void deleteDuplicateInStaging() {
		connection = connectDataConfig.connectDataStaging();
		String sql = "DELETE s1 FROM datawarehouse_staging.staging s1 " + 
				"INNER JOIN datawarehouse_staging.staging s2 " + 
				"WHERE s1.id < s2.id AND s1.text = s2.text;";
		try {
			pre = connection.prepareStatement(sql);
			int soDongTrung = pre.executeUpdate();
			if(soDongTrung >0) {
				System.out.println("so dong trung da xoa la : "+ soDongTrung);
			}else {
				System.out.println("xoa dong trung bi loi hoac khong co dong nao trung can xem lai");
			}
		} catch (Exception e) {
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

	// lưu các đối tượng lấy từ staging đem transform rồi lưu vào data student trước khi qua bước data warehouse
	public void insertToStudent() {
		ArrayList<String> listEmp = loadTextFromStaging();
		connection = connectDataConfig.connectConfigDatabase();
		String sql1 = "insert into datawarehouse_staging.data_student(mssv,firstName,lastName,dateOfBirth,classCode,className,phoneNumber,email,city,note) "
				+ "value(?,?,?,?,?,?,?,?,?,?)";
		int rowInsert = 0;
		String textMail = "";
		try {
			for (String line : listEmp) {
				pre = connection.prepareStatement(sql1);
				String[] arr = line.split(",");
				int mssv = Integer.parseInt(arr[1]);
				String firstName = arr[2];
				String lastName = arr[3];
				String date = arr[4];
				String classCode = arr[5];
				String className = arr[6];
				String phoneNumber = arr[7];
				String email = arr[8];
				String city = arr[9];
				String note = arr[10];

				pre.setInt(1, mssv);
				pre.setString(2, firstName);
				pre.setString(3, lastName);
				pre.setString(4, date);
				pre.setString(5, classCode);
				pre.setString(6, className);
				pre.setString(7, phoneNumber);
				pre.setString(8, email);
				pre.setString(9, city);
				pre.setString(10, note);
				rowInsert += pre.executeUpdate();
			}
			if(rowInsert == listEmp.size()) {
				System.out.println("so dong insert vao la "+rowInsert);//thanh cong
				subject ="Insert to db student thành công";
				textMail = "Đã insert thành công : "+ rowInsert+" vào db student";
				sendMail.sendMail(emailSendTo, subject, textMail);
			}else if(rowInsert < listEmp.size()) {
				subject ="Insert to db có thể thiếu số dòng so vs staging cần kiểm tra";
				int conLai = listEmp.size() -rowInsert ;
				textMail = "Đã insert thành công : "+ rowInsert+" dòng vào db student và còn thiếu : "+conLai+" dòng" ;
				sendMail.sendMail(emailSendTo, subject, textMail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("insert to data warehouse sucess");
	}

	public static void main(String[] args) {
		InsertData insert = new InsertData();

		//ArrayList<ControlModel> controls = insert.getControlModel();
		//for (ControlModel controlModel : controls) {
		//	System.out.println(controlModel.toString());
		//}

		// ArrayList<String> loadStaging = insert.loadTextFromStaging(); for (String st
		// : loadStaging) { System.out.println(st); }

		// insert.insertToLog();
		// insert.addText();
		// insert.insertToDataWareHouse();
		// insert.insertToDBControl();
		insert.insertToStudent();
	}
}
