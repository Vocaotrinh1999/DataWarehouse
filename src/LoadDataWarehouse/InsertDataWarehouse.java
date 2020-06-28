package LoadDataWarehouse;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import LoadDataStaging.ConnectDataConfig;
import Model.Student;

public class InsertDataWarehouse {

	ConnectDataConfig connectDataConfig;
	Connection connection;
	PreparedStatement pre;
	public InsertDataWarehouse() {
		super();
		this.connectDataConfig = new ConnectDataConfig();
	}
	//lay ds sinh vien
	public ArrayList<Student> getStudent(){
		ArrayList<Student> listStudent = new ArrayList<Student>();
		String sql = "select d1.* from datawarehouse_staging.data_student d1" ;
		try {
			connection = connectDataConfig.connectDataStaging();
			pre = connection.prepareStatement(sql);
			ResultSet result = pre.executeQuery();
			while(result.next()) {
				int mssv = result.getInt(2);
				String firstName = result.getString(3);
				String lastName = result.getString(4);
				String dateOfBirth = result.getString(5);
				String classCode = result.getString(6);
				String className = result.getString(7);
				String phoneNumber = result.getString(8);
				String email = result.getString(9);
				String city = result.getString(10);
				String note = result.getString(11);
				Student student = new Student(mssv, firstName, lastName, dateOfBirth, classCode, className, phoneNumber, email, city, note);
				listStudent.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listStudent;
	}
	//lay ra ds sinh vien trung nhau
	public ArrayList<Student> getStudentDuplicate(){
		ArrayList<Student> listStudent = new ArrayList<Student>();
		String sql = "select d1.* from datawarehouse_staging.data_student d1 join datawarehouse_staging.data_student d2" + 
				"where d1.mssv == d2.mssv;";
		try {
			connection = connectDataConfig.connectDataStaging();
			pre = connection.prepareStatement(sql);
			ResultSet result = pre.executeQuery();
			while(result.next()) {
				int mssv = result.getInt(2);
				String firstName = result.getString(3);
				String lastName = result.getString(4);
				String dateOfBirth = result.getString(5);
				String classCode = result.getString(6);
				String className = result.getString(7);
				String phoneNumber = result.getString(8);
				String email = result.getString(9);
				String city = result.getString(10);
				String note = result.getString(11);
				Student student = new Student(mssv, firstName, lastName, dateOfBirth, classCode, className, phoneNumber, email, city, note);
				listStudent.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listStudent;
	}
	
	public void insertStudentNotDuplicate() {
		ArrayList<Student> listStudent = getStudent();
		String sql = "insert into datawarehouse.data_warehouse(mssv,firstName_current,firstName_history,"
				+ "lastName_current,lastName_history,dateOfBirth_current,dateOfBirth_history,classCode_current,classCode_history,"
				+ "className_current,className_history,phoneNumber_current,phoneNumber_history,email_current,email_history,"
				+ "city_current,city_history,note_current,note_history,startDate,endDate,current_flag) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.of(9999, 12, 31);
		try {
			connection = connectDataConfig.connectDataWarehouse();
			pre = connection.prepareStatement(sql);
			for (Student student : listStudent) {
				
				pre.setInt(1, student.getMssv());
				pre.setString(2, student.getFirstName());
				pre.setString(3, student.getFirstName());
				pre.setString(4, student.getLastName());
				pre.setString(5, student.getLastName());
				pre.setString(6, student.getDateOfBirth());
				pre.setString(7, student.getDateOfBirth());
				pre.setString(8, student.getClassCode());
				pre.setString(9, student.getClassCode());
				pre.setString(10, student.getClassName());
				pre.setString(11, student.getClassName());
				pre.setString(12, student.getPhoneNumber());
				pre.setString(13, student.getPhoneNumber());
				pre.setString(14, student.getEmail());
				pre.setString(15, student.getEmail());
				pre.setString(16, student.getCity());
				pre.setString(17, student.getCity());
				pre.setString(18, student.getNote());
				pre.setString(19, student.getNote());
				pre.setDate(20, Date.valueOf(startDate));
				pre.setDate(21, Date.valueOf(endDate));
				pre.setString(22, "YES");
				pre.executeUpdate();
			}
			
			System.out.println("insert thanh cong");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		InsertDataWarehouse insertDateWarehouse = new InsertDataWarehouse();
		insertDateWarehouse.insertStudentNotDuplicate();
	}
}
