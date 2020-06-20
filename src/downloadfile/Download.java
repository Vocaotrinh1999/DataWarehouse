package downloadfile;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

public class Download {

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
		// Connect to an SSH server:
		String hostname = "drive.ecepvn.org";
		int port = 2227;
		boolean success = ssh.Connect(hostname, port);
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		// Wait a max of 5 seconds when reading responses..
		ssh.put_IdleTimeoutMs(5000);
		// Authenticate using login/password:
		success = ssh.AuthenticatePw(account.getUserName(),account.getPassword());
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		// Once the SSH object is connected and authenticated, use it
		// in the SCP object.
		CkScp scp = new CkScp();
		success = scp.UseSsh(ssh);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		scp.put_SyncMustMatch(fileProperties.getFileFormat());
		String remoteDir = fileProperties.getRemoteDir();
		String localDir = fileProperties.getLocalDir();
		// Download synchronization modes:
		// mode=0: Download all files
		// mode=1: Download all files that do not exist on the local filesystem.
		// mode=2: Download newer or non-existant files.
		// mode=3: Download only newer files.
		// mode=5: Download only missing files or files with size differences.
		// mode=6: Same as mode 5, but also download newer files.
		int mode = 0;
		boolean bRecurse = false;
		success = scp.SyncTreeDownload(remoteDir, localDir, mode, bRecurse);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		System.out.println("Download File Success.");
		ssh.Disconnect();
	}
}
