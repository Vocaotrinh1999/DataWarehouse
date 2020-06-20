package downloadfile;

public class FileProperties {
	private String fileFormat;
	private String remoteDir;
	private String localDir;

	public FileProperties(String fileFormat, String remoteDir, String localDir) {
		super();
		this.fileFormat = fileFormat;
		this.remoteDir = remoteDir;
		this.localDir = localDir;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public String getLocalDir() {
		return localDir;
	}

}
