package Model;

public class ControlModel {
	//
	private int id;
	private String fileName;
	private String fileLocation;
	private String targetTable;
	private String fileType;
	private String delimeter;
	private String importDir;
	private String sucessDir;
	private String errorDir;
	private String columList;
	
	public ControlModel(int id, String fileName, String fileLocation, String targetTable, String fileType,
			String delimeter, String importDir, String sucessDir, String errorDir, String columList) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileLocation = fileLocation;
		this.targetTable = targetTable;
		this.fileType = fileType;
		this.delimeter = delimeter;
		this.importDir = importDir;
		this.sucessDir = sucessDir;
		this.errorDir = errorDir;
		this.columList = columList;
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

	public void setFileocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getTargetTabel() {
		return targetTable;
	}

	public void setTargetTabel(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDelimeter() {
		return delimeter;
	}

	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}

	public String getImportDir() {
		return importDir;
	}

	public void setImportDir(String importDir) {
		this.importDir = importDir;
	}

	public String getSucessDir() {
		return sucessDir;
	}

	public void setSucessDir(String sucessDir) {
		this.sucessDir = sucessDir;
	}

	public String getErrorDir() {
		return errorDir;
	}

	public void setErrorDir(String errorDir) {
		this.errorDir = errorDir;
	}

	public String getColumList() {
		return columList;
	}

	public void setColumList(String columList) {
		this.columList = columList;
	}

	@Override
	public String toString() {
		return "ControlModel [id=" + id + ", fileName=" + fileName + ", fileLocation=" + fileLocation + ", targetTable="
				+ targetTable + ", fileType=" + fileType + ", delimeter=" + delimeter + ", importDir=" + importDir
				+ ", sucessDir=" + sucessDir + ", errorDir=" + errorDir + ", columList=" + columList + "]";
	}
}
