package SubjectProcess;

import java.sql.Connection;
import java.sql.PreparedStatement;

import LoadDataStaging.*;
public class SubjectProcess {

	ConnectDataConfig connectDataConfig;
	Connection connection;
	PreparedStatement pre, pre2;
	public SubjectProcess() {
		super();
		this.connectDataConfig = new ConnectDataConfig();
	}
	
	public void insertToSubject() {
		String read = connectDataConfig.readFileFromFolder("F:\\anh\\datawarehouse\\sucess\\môn học.csv");
		connection = connectDataConfig.connectConfigDatabase();
		String insertSb2013 = "insert into datawarehouse.data_subject2013(stt,msmh,stc,khoaqly,khoasudung,ghichu) "
				+ "values(?,?,?,?,?,?)";
		String insertSb2014 = "insert into datawarehouse.data_subject2014(stt,msmh,stc,khoaqly,khoasudung,ghichu) "
				+ "values(?,?,?,?,?,?)";
		String[] data = read.split("\n");
		int stt = Integer.parseInt(data[0]);
		
	}
	public static void main(String[] args) {
		SubjectProcess s = new SubjectProcess();
		
	}
}
