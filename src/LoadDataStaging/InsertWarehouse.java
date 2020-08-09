package LoadDataStaging;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import GuiMail.SendMailTLS;
import Model.DangKy;
import Model.LopHoc;
import Model.Student;
import Model.Subject;

public class InsertWarehouse {

	ConnectDataConfig connectDataConfig;
	SendMailTLS sendMail;
	PreparedStatement pre, pre2, pre3, pre4, pre5;
	Statement stm, stm2;
	Connection connection, connection2, connection3;
	ResultSet result, result2, result3;
	String emailSendTo, subject, textMail;
	int count = 0;

	public InsertWarehouse() {
		connectDataConfig = new ConnectDataConfig();
		sendMail = new SendMailTLS();
	}

	public List<Student> getStudentFromStaging() {
		String sql = "SELECT * FROM datawarehouse_staging.data_student;";
		connection = connectDataConfig.connectDataStaging();
		List<Student> list = new ArrayList<Student>();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				Student st = new Student();
				st.setMssv(result.getInt(1));
				st.setFirstName(result.getString(2));
				st.setLastName(result.getString(3));
				st.setDateOfBirth(String.valueOf(result.getString(4)));
				st.setClassCode(result.getString(5));
				st.setClassName(result.getString(6));
				st.setPhoneNumber(result.getString(7));
				st.setEmail(result.getString(8));
				st.setCity(result.getString(9));
				st.setNote(result.getString(10));
				if (st != null) {
					list.add(st);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<DangKy> getRegisterFromStaging() {
		List<DangKy> list = new ArrayList<DangKy>();
		String sql = "SELECT * FROM datawarehouse_staging.dangky;";
		connection = connectDataConfig.connectDataStaging();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				DangKy dk = new DangKy();
				dk.setMaDK(result.getString(1));
				dk.setMssv(result.getInt(2));
				dk.setMaLH(result.getString(3));
				dk.setNgayDK(String.valueOf(result.getDate(4)));
				if (dk != null) {
					list.add(dk);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Subject> readSubjectFromStaging() {
		List<Subject> list = new ArrayList<Subject>();
		String sql = "SELECT * FROM datawarehouse_staging.dangky;";
		connection = connectDataConfig.connectDataStaging();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				Subject s = new Subject();
				s.setStt(result.getInt(1));
				s.setMsmh(result.getInt(2));
				s.setTenmh(result.getString(3));
				s.setStc(result.getInt(4));
				s.setKhoaQly(result.getString(5));
				s.setKhoaSuDung(result.getString(6));
				s.setGhichu(result.getString(7));
				if (s != null) {
					list.add(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<LopHoc> readClassFromStaging() {
		List<LopHoc> list = new ArrayList<LopHoc>();
		String sql = "SELECT * FROM datawarehouse_staging.lophoc;";
		connection = connectDataConfig.connectDataStaging();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				LopHoc l = new LopHoc();
				l.setMaLH(result.getString(1));
				l.setMaMH(result.getInt(2));
				l.setNamhoc(result.getInt(3));
				if (l != null) {
					list.add(l);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Student> getStudentFromWarehouse() {
		String sql = "SELECT * FROM datawarehouse.data_student";
		connection = connectDataConfig.connectDataWarehouse();
		List<Student> list = new ArrayList<Student>();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				Student st = new Student();
				st.setMssv(result.getInt(2));
				st.setFirstName(result.getString(3));
				st.setLastName(result.getString(4));
				st.setDateOfBirth(String.valueOf(result.getString(5)));
				st.setClassCode(result.getString(6));
				st.setClassName(result.getString(7));
				st.setPhoneNumber(result.getString(8));
				st.setEmail(result.getString(9));
				st.setCity(result.getString(10));
				st.setNote(result.getString(11));
				if (st != null) {
					list.add(st);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<DangKy> getRegisterFromWarehouse() {
		List<DangKy> list = new ArrayList<DangKy>();
		String sql = "SELECT * FROM datawarehouse.dangky; ";
		connection = connectDataConfig.connectDataWarehouse();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				DangKy dk = new DangKy();
				dk.setMaDK(result.getString(2));
				dk.setMssv(result.getInt(3));
				dk.setMaLH(result.getString(4));
				dk.setNgayDK(String.valueOf(result.getDate(5)));
				if (dk != null) {
					list.add(dk);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Subject> readSubjectFromWarehouse() {
		List<Subject> list = new ArrayList<Subject>();
		String sql = "SELECT * FROM datawarehouse_staging.dangky;";
		connection = connectDataConfig.connectDataWarehouse();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				Subject s = new Subject();
				s.setStt(result.getInt(2));
				s.setMsmh(result.getInt(3));
				s.setTenmh(result.getString(4));
				s.setStc(result.getInt(5));
				s.setKhoaQly(result.getString(6));
				s.setKhoaSuDung(result.getString(7));
				s.setGhichu(result.getString(8));
				if (s != null) {
					list.add(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<LopHoc> readClassFromWarehouse() {
		List<LopHoc> list = new ArrayList<LopHoc>();
		String sql = "SELECT * FROM datawarehouse.lophoc;";
		connection = connectDataConfig.connectDataWarehouse();
		try {
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				LopHoc l = new LopHoc();
				l.setMaLH(result.getString(2));
				l.setMaMH(result.getInt(3));
				l.setNamhoc(result.getInt(4));
				if (l != null) {
					list.add(l);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean insertStudentToWarehouse() {
		List<Student> listStaging = getStudentFromStaging();
		List<Student> listWarehouse = getStudentFromWarehouse();
		String sql = "insert into datawarehouse.data_student(mssv,firstName,lastName,dateOfBirth,classCode,className,phoneNumber,email,city,note,dt_expired,dt_last_change) "
				+ "value(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			connection = connectDataConfig.connectDataWarehouse();
			pre = connection.prepareStatement(sql);
			for (Student s1 : listWarehouse) {
				for (Student s2 : listStaging) {
					pre.setInt(1, s2.getMssv());
					pre.setString(2, s2.getFirstName());
					pre.setString(3, s2.getLastName());
					pre.setDate(4, Date.valueOf(s2.getDateOfBirth()));
					pre.setString(5, s2.getClassCode());
					pre.setString(6, s2.getClassName());
					pre.setString(7, s2.getPhoneNumber());
					pre.setString(8, s2.getEmail());
					pre.setString(9, s2.getCity());
					pre.setString(10, s2.getNote());
					if (!s1.equals(s2)) {

						// pre.setTimestamp(11, x);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String[] args) {
		InsertWarehouse i = new InsertWarehouse();
	}
}
