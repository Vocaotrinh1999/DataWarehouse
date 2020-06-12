package LoadData;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertToLog {

	/*
	ConnectDataConfig connectToConfig;
	PreparedStatement pre;

	public InsertToLog() {
		connectToConfig = new ConnectDataConfig();
	}

	public void insertToLog() {
		File file = new File("data");
		File[] listFile = file.listFiles();
		Connection connection = null;
		try {
			connection = connectToConfig.connectConfigDatabase();
			String sql = "insert into datawarehouse_configuration.log('file_name','status') values(?,'ER')";
			for (File file2 : listFile) {
				pre = connection.prepareStatement(sql);
				pre.setString(1, file2.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
