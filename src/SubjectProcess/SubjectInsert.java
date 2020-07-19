package SubjectProcess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import LoadDataStaging.ConnectDataConfig;
import Model.DangKy;
import Model.LopHoc;
import Model.Subject;

public class SubjectInsert {

	ConnectDataConfig connectDataConfig;
	Connection connection;
	PreparedStatement pre, pre2;

	public SubjectInsert() {
		this.connectDataConfig = new ConnectDataConfig();
	}

	public ArrayList<Subject> readSubject() {
		ArrayList<Subject> listSubject = new ArrayList<Subject>();
		String read = connectDataConfig.readFileFromFolder("F:\\anh\\datawarehouse\\monhocnhap.csv");
		String[] data1 = read.split("\n");
		for (int i = 1; i < data1.length; i++) {
			String d = data1[i];
			String[] data = d.split(",");
			int stt = Integer.parseInt(data[0]);
			int msmh2013 = Integer.parseInt(data[1]);
			String tenmh2013 = data[2];
			int stc2013 = Integer.parseInt(data[3]);
			String khoaQly2013 = data[4];
			String khoaSudung = data[5];
			int msmh2014 = Integer.parseInt(data[6]);
			String tenmh2014 = data[7];
			int stc2014 = Integer.parseInt(data[8]);
			String khoaqly2014 = data[9];
			String ghichu = data[10];
			Subject subject = new Subject(stt, msmh2013, tenmh2013, stc2013, khoaQly2013, khoaSudung, msmh2014,
					tenmh2014, stc2014, khoaqly2014, ghichu);
			listSubject.add(subject);
		}
		return listSubject;
	}

	public void insertSubject2013() {
		ArrayList<Subject> listSubject = readSubject();
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse.data_subject(stt,msmh,tenmh,stc,khoaqly,khoasudung,ghichu,version,expriteDate)"
				+ " values (?,?,?,?,?,?,?,?,?)";

		try {
			pre = connection.prepareStatement(sql);
			pre2 = connection.prepareStatement(sql);
			for (Subject s : listSubject) {
				pre.setInt(1, s.getStt());
				pre.setInt(2, s.getMsmh2013());
				pre.setString(3, s.getTenmh2013());
				pre.setInt(4, s.getStc2013());
				pre.setString(5, s.getKhoaQly2013());
				pre.setString(6, s.getKhoaSuDung());
				pre.setString(7, s.getGhichu());
				pre.setInt(8, 1);
				LocalDate l = LocalDate.of(9999, 12, 31);
				pre.setDate(9, Date.valueOf(l));
				pre.executeUpdate();
			}
			connection.close();
			System.out.println("insert 2013 sucess");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertSubject2014() {
		ArrayList<Subject> listSubject = readSubject();
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse.data_subject(stt,msmh,tenmh,stc,khoaqly,khoasudung,ghichu,version,expriteDate)"
				+ " values (?,?,?,?,?,?,?,?,?)";
		String sql2 = "update datawarehouse.data_subject set expriteDate = ? where version = 1";
		try {
			pre = connection.prepareStatement(sql);
			pre2 = connection.prepareStatement(sql2);
			for (Subject s : listSubject) {
				if (s.getMsmh2013() != s.getMsmh2014() || s.getTenmh2013() != s.getTenmh2014()
						|| s.getStc2013() != s.getStc2014() || s.getKhoaQly2013() != s.getKhoaQly2014()) {
					pre.setInt(1, s.getStt());
					pre.setInt(2, s.getMsmh2014());
					pre.setString(3, s.getTenmh2014());
					pre.setInt(4, s.getStc2014());
					pre.setString(5, s.getKhoaQly2014());
					pre.setString(6, s.getKhoaSuDung());
					pre.setString(7, s.getGhichu());
					pre.setInt(8, 2);
					LocalDate l = LocalDate.of(9999, 12, 31);
					pre.setDate(9, Date.valueOf(l));

					LocalDate l1 = LocalDate.of(2013, 6, 1);
					pre2.setDate(1, Date.valueOf(l1));

					pre.executeUpdate();
					pre2.executeUpdate();
				}
			}
			connection.close();
			System.out.println("insert 2014 sucess");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<LopHoc> listLH() {
		String lh = connectDataConfig.readFileFromFolder("F:\\anh\\datawarehouse\\data\\lophoc_sang_nhom5.csv ");
		List<LopHoc> listLH = new ArrayList<LopHoc>();

		String[] s1 = lh.split("\n");
		for (int i = 1; i < s1.length; i++) {
			String s2 = s1[i];
			String[] ob = s2.split(",");
			String maLH = ob[0];
			int maMH = Integer.parseInt(ob[1]);
			int namhoc = Integer.parseInt(ob[2]);
			LopHoc lop = new LopHoc(maLH, maMH, namhoc);
			listLH.add(lop);
		}
		return listLH;
	}

	public List<DangKy> listDK(){
		List<DangKy> listDK = new ArrayList<DangKy>();
		String dk = connectDataConfig.readFileFromFolder("F:\\anh\\datawarehouse\\data\\dangky_sang_nhom5.csv");
		String[] s1 = dk.split("\n");
		for (int i = 1; i < s1.length; i++) {
			String s2 = s1[i];
			String[] ob = s2.split(",");
			String maDK = ob[0];
			int mssv = Integer.parseInt(ob[1]);
			String maLH = ob[2];
			String[] date = ob[3].split("/");
			LocalDate ld = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
			Date ngayDK = Date.valueOf(ld);
			DangKy dki = new DangKy(maDK, mssv, maLH, ngayDK);
			listDK.add(dki);
		}
		return listDK;
	}
	public void insertLH() {
		List<LopHoc> listLH = listLH();
		connection = connectDataConfig.connectDataWarehouse();
		
		String sql = "insert into datawarehouse.lophoc(maLH,maMH,namhoc) values(?,?,?)";
		int result = 0;
		try {
			connection.setAutoCommit(false);
			pre =  connection.prepareStatement(sql);
			for (LopHoc l : listLH) {
				pre.setString(1, l.getMaLH());
				pre.setInt(2, l.getMaMH());
				pre.setInt(3, l.getNamhoc());
				result += pre.executeUpdate();
			}
			if(result == listLH.size()) {
				connection.commit();
			}else {
				connection.rollback();
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("inssert sucesss");
	}

	public void insertDK() {
		List<DangKy> listDK = listDK();
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse.dangky(madk,mssv,maLH,ngayDK) values(?,?,?,?)";
		int result = 0;
		try {
			pre = connection.prepareStatement(sql);
			for (DangKy d : listDK) {
				pre.setString(1, d.getMaDK());
				pre.setInt(2, d.getMssv());
				pre.setString(3, d.getMaLH());
				pre.setDate(4, d.getNgayDK());
				result += pre.executeUpdate();
			}
			if(result == listDK.size()) {
				connection.commit();
			}else {
				connection.rollback();
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		SubjectInsert s = new SubjectInsert();
		/*
		 * ArrayList<Subject> listSubject = s.readSubject(); for (Subject subject :
		 * listSubject) { System.out.println(subject.toString()); }
		 */
		// s.insertSubject2013();
		// s.insertSubject2014();
		//s.insertLH();
		s.insertDK();
	}
}
