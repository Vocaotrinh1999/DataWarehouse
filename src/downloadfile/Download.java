package downloadfile;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

import GuiMail.SendMailTLS;
import LoadDataStaging.ConnectDataConfig;
import LoadDataStaging.InsertData;
import Model.LogModel;

public class Download {
	private ConnectDataConfig connectDataConfig;
	private SendMailTLS sendMail;

	public Download() {
		connectDataConfig = new ConnectDataConfig();
	}

	static {
		try {
			System.loadLibrary("chilkat");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public void downloadFileFromNas(Account account, FileProperties fileProperties) {
		String textSendMail = " ";
		CkGlobal glob = new CkGlobal();
		glob.UnlockBundle("Waiting . . .");
		CkSsh ssh = new CkSsh();
		String hostname = "drive.ecepvn.org";
		int port = 2227;
		boolean success = ssh.Connect(hostname, port);
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		ssh.put_IdleTimeoutMs(5000);
		success = ssh.AuthenticatePw(account.getUserName(), account.getPassword());
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		CkScp scp = new CkScp();
		success = scp.UseSsh(ssh);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		scp.put_SyncMustMatch(fileProperties.getFileFormat());
		String remoteDir = fileProperties.getRemoteDir();
		String localDir = fileProperties.getLocalDir();
		int mode = 2;
		boolean bRecurse = false;
		success = scp.SyncTreeDownload(remoteDir, localDir, mode, bRecurse);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		ssh.Disconnect();
		File file = new File(fileProperties.getLocalDir());
		File[] listFile = file.listFiles();
		int count = 0;
		for (File file2 : listFile) {
			if (file2.length() == 0) {
				System.out.println("File error :" + file2.getName());
				textSendMail = "File " + file2.getName() + " error";
				sendMail.sendMail("17130256@st.hcmuaf.edu.vn", "File Error", textSendMail);
			} else {
				LogModel logModel = new LogModel(file2.getName(), file2.getAbsolutePath(), "NR", "NR");
				if (checkDuplicate(file2.getName()) == false) {
					insertLog(logModel);
					count++;
				}
			}
		}

		System.out.println("Download File Success And Insert " + count + " File To Log");
		textSendMail = "Download File Success And Insert " + count + " File To Log";
		sendMail.sendMail("17130024@st.hcmuaf.edu.vn", "File Error", textSendMail);
	}

	public void insertLog(LogModel logModel) {
		String sql = "insert into datawarehouse_configuration.log(file_name,file_location,load_staging_status,load_datawarehouse_status) values('"
				+ logModel.getFileName() + "','" + logModel.getFileLocation() + "','" + logModel.getLoadStagingStatus()
				+ "','" + logModel.getLoadDataWarehouseStatus() + "');";
		try {
			connectDataConfig.perform(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkDuplicate(String fileNameNeedCheck) {
		String sql = "SELECT * FROM datawarehouse_configuration.log;";
		ArrayList<LogModel> listLog = new ArrayList<LogModel>();
		try {
			ResultSet rs = connectDataConfig.selectDatabase(sql);
			while (rs.next()) {
				String fileName = rs.getString(2);
				String fileLocation = rs.getString(3);
				String loadStagingStatus = rs.getString(4);
				String loadDataWarehouseStatus = rs.getString(5);
				LogModel logModel = new LogModel(fileName, fileLocation, loadStagingStatus, loadDataWarehouseStatus);
				listLog.add(logModel);
			}
			for (LogModel logModel : listLog) {
				if (fileNameNeedCheck.equalsIgnoreCase(logModel.getFileName()))
					return true;
			}
		} catch (Exception e) {
			sendMail.sendMail("17130024@st.hcmuaf.edu.vn", "File Error", "ERROR SQL");
		}
		return false;
	}

}
