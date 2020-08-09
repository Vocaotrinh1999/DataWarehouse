<<<<<<< HEAD
package Model;

public class LogModel {
	//
	private int id;
	private String fileName;
	private String fileLocation;
	private String loadStagingStatus;
	private String loadDataWarehouseStatus;

	public LogModel(int id, String fileName, String fileLocation, String loadStagingStatus,
			String loadDataWarehouseStatus) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileLocation = fileLocation;
		this.loadStagingStatus = loadStagingStatus;
		this.loadDataWarehouseStatus = loadDataWarehouseStatus;
	}
	public LogModel(String fileName, String fileLocation, String loadStagingStatus,
			String loadDataWarehouseStatus) {
		super();
		this.fileName = fileName;
		this.fileLocation = fileLocation;
		this.loadStagingStatus = loadStagingStatus;
		this.loadDataWarehouseStatus = loadDataWarehouseStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getLoadStagingStatus() {
		return loadStagingStatus;
	}

	public void setLoadStagingStatus(String loadStagingStatus) {
		this.loadStagingStatus = loadStagingStatus;
	}

	public String getLoadDataWarehouseStatus() {
		return loadDataWarehouseStatus;
	}

	public void setLoadDataWarehouseStatus(String loadDataWarehouseStatus) {
		this.loadDataWarehouseStatus = loadDataWarehouseStatus;
	}
}
=======
package Model;

public class LogModel {
	//
	private int id;
	private String fileName;
	private String fileLocation;
	private String loadStagingStatus;
	private String loadDataWarehouseStatus;

	public LogModel(int id, String fileName, String fileLocation, String loadStagingStatus,
			String loadDataWarehouseStatus) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileLocation = fileLocation;
		this.loadStagingStatus = loadStagingStatus;
		this.loadDataWarehouseStatus = loadDataWarehouseStatus;
	}
	public LogModel(String fileName, String fileLocation, String loadStagingStatus,
			String loadDataWarehouseStatus) {
		super();
		this.fileName = fileName;
		this.fileLocation = fileLocation;
		this.loadStagingStatus = loadStagingStatus;
		this.loadDataWarehouseStatus = loadDataWarehouseStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getLoadStagingStatus() {
		return loadStagingStatus;
	}

	public void setLoadStagingStatus(String loadStagingStatus) {
		this.loadStagingStatus = loadStagingStatus;
	}

	public String getLoadDataWarehouseStatus() {
		return loadDataWarehouseStatus;
	}

	public void setLoadDataWarehouseStatus(String loadDataWarehouseStatus) {
		this.loadDataWarehouseStatus = loadDataWarehouseStatus;
	}
}
>>>>>>> branch 'master' of https://github.com/Vocaotrinh1999/DataWarehouse
