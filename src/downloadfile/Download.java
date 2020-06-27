package downloadfile;

import java.io.File;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

import LoadDataStaging.ConnectDataConfig;
import LoadDataStaging.InsertData;

public class Download {
	private ConnectDataConfig connectDataConfig;
	private InsertData insert;
	public Download() {
		connectDataConfig = new ConnectDataConfig();
		insert = new InsertData();
	}
	static {
		try {
			System.loadLibrary("chilkat");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public void downloadFileFromNas(Account account,FileProperties fileProperties ) {
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
		success = ssh.AuthenticatePw(account.getUserName(),account.getPassword());
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
		insert.insertLog(file);
		for (File file2 : listFile) {
			if(file2.length() == 0) {
				//send mall
			}else {
				//insertLog(file2);
				count ++;
			}
		}
		System.out.println("Insert "+count+" file to log");
		System.out.println("Download File Success.");
	}
	//phương thức này còn bị lỗi
	public void insertLog(File file) {
		String sql = "insert into datawarehouse_configuration.log(file_name,file_location,load_staging_status,load_datawarehouse_status) values('"+file.getName()+"','"+file.getAbsolutePath()+"','"+"NR"+"','"+"NR"+"');";
		try {
			connectDataConfig.perform(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
