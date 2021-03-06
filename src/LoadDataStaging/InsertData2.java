package LoadDataStaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.List;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import GuiMail.SendMailTLS;
import Model.DangKy;
import Model.LopHoc;
import Model.Student;
import Model.Subject;
import Schedule.Schedule;

public class InsertData2 {
	ConnectDataConfig connectDataConfig;
	ReadDataFromFile readDataFromFile;
	SendMailTLS sendMail;
	PreparedStatement pre, pre2, pre3, pre4, pre5;
	Statement stm, stm2;
	Connection connection, connection2;
	ResultSet result, result2, result3;
	String emailSendTo, subject, textMail;
	Schedule schedule;
	int count = 0;

	public InsertData2() {
		connectDataConfig = new ConnectDataConfig();
		readDataFromFile = new ReadDataFromFile();
		sendMail = new SendMailTLS();
		schedule = new Schedule();
	}

	public void moveFile(String sourcePath, String destinationPath) {
		try {
			File sourceFile = new File(sourcePath);
			File destFile = new File(destinationPath);
			Files.copy(sourceFile.toPath(), destFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int insertToStudent(String filePath, String fileType) {
		List<Student> listST = null;
		if (fileType.equals("xlsx")) {
			listST = readDataFromFile.readStudentFromExcel(filePath);
		} else if (fileType.equals("txt") || fileType.equals("csv")) {
			listST = readDataFromFile.readStudentTxtOrCSV(filePath);
		}
		connection2 = connectDataConfig.connectDataStaging();
		String sql1 = "insert into datawarehouse_staging.data_student(mssv,firstName,lastName,dateOfBirth,classCode,className,phoneNumber,email,city,note) "
				+ "value(?,?,?,?,?,?,?,?,?,?)";
		int rowInsert = 0;
		try {
			connection2.setAutoCommit(false);
			pre = connection2.prepareStatement(sql1);
			for (Student st : listST) {
				pre.setInt(1, st.getMssv());
				pre.setString(2, st.getFirstName());
				pre.setString(3, st.getLastName());
				String date = st.getDateOfBirth();
				String[] date2 = date.split("/");
				LocalDate date3 = LocalDate.of(Integer.parseInt(date2[2]), Integer.parseInt(date2[1]),
						Integer.parseInt(date2[0]));
				pre.setDate(4, Date.valueOf(date3));
				pre.setString(5, st.getClassCode());
				pre.setString(6, st.getClassName());
				pre.setString(7, st.getPhoneNumber());
				pre.setString(8, st.getEmail());
				pre.setString(9, st.getCity());
				pre.setString(10, st.getNote());
				rowInsert += pre.executeUpdate();
				connection2.commit();
			}
			if (rowInsert != listST.size()) {
				connection2.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowInsert;
	}

	public int insertSubject(String path, String fileType) {
		List<Subject> listSubject = null;
		if (fileType.equals("xlsx")) {
			listSubject = readDataFromFile.readSubjectFromExcel(path);
		} else if (fileType.equals("txt") || fileType.equals("csv")) {
			listSubject = readDataFromFile.readSubjectTxtOrCsv(path);
		}
		int rowInsert = 0;
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse_staging.data_subject(stt,msmh,tenmh,stc,khoaqly,khoasudung,ghichu)"
				+ " values (?,?,?,?,?,?,?)";
		try {
			connection.setAutoCommit(false);
			pre = connection.prepareStatement(sql);
			for (Subject s : listSubject) {
				pre.setInt(1, s.getStt());
				pre.setInt(2, s.getMsmh());
				pre.setString(3, s.getTenmh());
				pre.setInt(4, s.getStc());
				pre.setString(5, s.getKhoaQly());
				pre.setString(6, s.getKhoaSuDung());
				pre.setString(7, s.getGhichu());
				rowInsert += pre.executeUpdate();
				connection.commit();
			}
			if (rowInsert != listSubject.size()) {
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowInsert;
	}

	public int insertClass(String path, String fileType) {
		List<LopHoc> listLH = null;
		if (fileType.equals("xlsx")) {
			listLH = readDataFromFile.readClassFromExcel(path);
		} else if (fileType.equals("txt") || fileType.equals("csv")) {
			listLH = readDataFromFile.readClassTxtOrCSV(path);
		}
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse_staging.lophoc(maLH,maMH,namhoc) values(?,?,?)";
		int rowInserted = 0;
		try {
			connection.setAutoCommit(false);
			pre = connection.prepareStatement(sql);
			for (LopHoc l : listLH) {
				pre.setString(1, l.getMaLH());
				pre.setInt(2, l.getMaMH());
				pre.setInt(3, l.getNamhoc());
				rowInserted += pre.executeUpdate();
				connection.commit();
			}
			if (rowInserted != listLH.size()) {
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowInserted;
	}

	public int insertRegister(String path, String fileType) {
		List<DangKy> listDK = null;
		if (fileType.equals("xlsx")) {
			listDK = readDataFromFile.readRegisterFromExcel(path);
		} else if (fileType.equals("txt") || fileType.equals("csv")) {
			listDK = readDataFromFile.readRegisterFromTxtOrCSV(path);
		}
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse_staging.dangky(madk,mssv,maLH,ngayDK) values(?,?,?,?)";
		int rowInserted = 0;
		try {
			connection.setAutoCommit(false);
			pre = connection.prepareStatement(sql);
			for (DangKy d : listDK) {
				pre.setString(1, d.getMaDK());
				pre.setInt(2, d.getMssv());
				pre.setString(3, d.getMaLH());
				String[] date = d.getNgayDK().split("/");
				LocalDate ngayDK = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]),
						Integer.parseInt(date[0]));
				pre.setDate(4, Date.valueOf(ngayDK));
				rowInserted += pre.executeUpdate();
				connection.commit();
			}
			if (rowInserted != listDK.size()) {
				connection.rollback();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowInserted;
	}

	public int insertDate_Dim(String location) {
		String readDate = connectDataConfig.readFileFromFolder(location);
		Connection connection = connectDataConfig.connectDataStaging();
		String sql = "insert into datawarehouse.date_dim_without_quarter "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int rowInserted = 0;
		try {
			connection.setAutoCommit(false);
			PreparedStatement pre = connection.prepareStatement(sql);
			String[] data = readDate.split("\n");
			for (String d : data) {
				String[] date = d.split(",");
				pre.setInt(1, Integer.parseInt(date[0]));
				String[] date2 = date[1].split("/");
				LocalDate localDate = LocalDate.of(Integer.parseInt(date2[2]), Integer.parseInt(date2[0]),
						Integer.parseInt(date2[1]));
				pre.setDate(2, Date.valueOf(localDate));
				pre.setInt(3, Integer.parseInt(date[2]));
				pre.setInt(4, Integer.parseInt(date[3]));
				pre.setString(5, date[4]);
				pre.setString(6, date[5]);
				pre.setString(7, date[6]);
				pre.setString(8, date[7]);
				pre.setInt(9, Integer.parseInt(date[8]));
				pre.setInt(10, Integer.parseInt(date[9]));
				pre.setInt(11, Integer.parseInt(date[10]));
				pre.setString(12, date[11]);
				pre.setString(13, date[12]);
				pre.setInt(14, Integer.parseInt(date[13]));
				pre.setString(15, date[14]);
				pre.setString(16, date[15]);
				pre.setString(17, date[16]);
				pre.setString(18, date[17]);
				rowInserted += pre.executeUpdate();
				connection.commit();
			}
			if (rowInserted != data.length) {
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Insert Date dim sucess");
		return rowInserted;
	}

	public void updateLog(int id) {
		String sql5 = "update datawarehouse_configuration.`control.data_file` set file_status ='ST' where data_file_id = "
				+ id;
		try {
			connection = connectDataConfig.connectConfigDatabase();
			stm.executeUpdate(sql5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mainProcessStaging(int id) {// truyền vào tham số là id của process

		String sql = "select ct.data_file_id,ct.file_name,ct.file_status,cf.data_file_config_name,cf.import_dir,cf.sucess_dir,cf.error_dir,cf.data_file_target_table,ct.data_file_type"
				+ " from datawarehouse_configuration.`control.data_file` as ct "
				+ "join datawarehouse_configuration.`control.data_file_configuration` as cf "
				+ "on ct.data_config_id = cf.data_file_config_id where ct.data_config_id = " + id
				+ " and file_status='DL' " + "limit 1";

		String sql2 = "SELECT p.status FROM datawarehouse_configuration.`control.process` as p where process_id = "
				+ id;
		String sql3 = "update datawarehouse_configuration.`control.process` set status = 'start'  where process_name = 'staging';";
		String sql4 = "update datawarehouse_configuration.`control.process` set status = 'finish' where process_name = 'staging';";

		try {
			Properties props = new Properties();// doc thong tin de gui mail
			InputStream input = new FileInputStream("config/mailconfig.properties");
			props.load(input);
			String toAdmin = props.getProperty("to.staging.admin");
			String subject = props.getProperty("to.staging.error.subject");
			String text = props.getProperty("to.staging.error.text");

			connection = connectDataConfig.connectConfigDatabase();
			pre = connection.prepareStatement(sql);
			pre2 = connection.prepareStatement(sql2);
			stm = connection.createStatement();
			pre4 = connection.prepareStatement(sql4);
			result = pre.executeQuery();

			int data_file_id = 0;
			String file_name = "";
			String file_status = "";
			String config_name = "";
			String import_dir = "";
			String sucess_dir = "";
			String error_dir = "";
			String data_file_target_table = "";
			String data_file_type = "";
			int count = 0;
			while (result.next()) {
				data_file_id = result.getInt(1);
				file_name = result.getString(2);
				file_status = result.getString(3);
				config_name = result.getString(4);
				import_dir = result.getString(5);
				sucess_dir = result.getString(6) + "/" + file_name;
				error_dir = result.getString(7) + "/" + file_name;
				data_file_target_table = result.getString(8);
				data_file_type = result.getString(9);
			}
			System.out.println("config name : " + config_name);
			String sql5 = "update datawarehouse_configuration.`control.data_file` as ct SET ct.dt_file_extracted = ?, staging_load_count=?"
					+ " where ct.file_status='ST' and ct.data_file_id = " + data_file_id;
			String sql6 = "truncate table " + data_file_target_table;
			String sql7 = "update datawarehouse_configuration.`control.data_file` set file_status ='ERR' where data_file_id = "
					+ data_file_id;
			pre5 = connection.prepareStatement(sql5);

			String path = import_dir + "\\" + file_name;
			result2 = pre2.executeQuery();
			String status = "";
			while (result2.next()) {
				status = result2.getString(1);
			}
			stm = connection.createStatement();
			if (status.equals("finish")) {
				stm.executeUpdate(sql3);// select
				stm.executeUpdate(sql6);// truncate
				System.out.println("truncate table " + data_file_target_table);
				if (config_name.equals("sinhvien")) {
					int rowInsert = insertToStudent(path, data_file_type);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert student sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						pre4.executeUpdate();
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}

				} else if (config_name.equals("lophoc")) {
					int rowInsert = insertClass(path, data_file_type);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert class sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				} else if (config_name.equals("dangky")) {
					int rowInsert = insertRegister(path, data_file_type);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert class sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				} else if (config_name.equals("datedim")) {
					int rowInsert = insertDate_Dim(path);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert datetime sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				}
			} else {
				if (config_name.equals("monhoc")) {
					int rowInsert = insertSubject(path, data_file_type);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert subject sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				} else {
					System.out.println("tien trinh hien dang chay khong tim rafile moi nen tam dung");

					Trigger trigger = schedule.getTrigger();
					JobDetail job = schedule.getJob(id);
					Scheduler scheduler = new StdSchedulerFactory().getScheduler();
					System.out.println("End --------------- process");
					TriggerKey triggerKey = trigger.getKey();
					scheduler.pauseTrigger(triggerKey);
					scheduler.unscheduleJob(triggerKey);
					scheduler.deleteJob(job.getKey());
					scheduler.shutdown();
					System.out.println("Shut dow : " + scheduler.isShutdown());

					connection.close();
				}

			}
			if (status.equals("start")) {
				System.out.println("co tien trinh khac dang chay");
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		InsertData2 insert = new InsertData2();
=======
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import GuiMail.SendMailTLS;
import Model.DangKy;
import Model.LogModel;
import Model.LopHoc;
import Model.Student;
import Model.Subject;

public class InsertData2 {
	ConnectDataConfig connectDataConfig;
	SendMailTLS sendMail;
	PreparedStatement pre, pre2, pre3, pre4, pre5;
	Statement stm, stm2;
	Connection connection, connection2;
	ResultSet result, result2, result3;
	String emailSendTo, subject, textMail;
	int count = 0;

	public InsertData2() {
		connectDataConfig = new ConnectDataConfig();
		sendMail = new SendMailTLS();
	}

	// lấy các file đã dowload trong thư mục data đã lưu trong db control đem ra
	// thêm vào log
	public void insertLog(File file) {
		String sql = "insert into datawarehouse_configuration.log(file_name,file_location,load_staging_status,load_datawarehouse_status) "
				+ "values(?,?,'NR','NR');";
		Connection connection = connectDataConfig.connectConfigDatabase();
		try {
			PreparedStatement pre = connection.prepareStatement(sql);
			File[] listFile = file.listFiles();
			for (File f2 : listFile) {
				System.out.println(f2.getAbsolutePath());
				pre.setString(1, f2.getName());
				pre.setString(2, f2.getAbsolutePath());
				pre.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("insert thanh cong");
	}

	public void moveFile(String sourcePath, String destinationPath) {
		try {
			File sourceFile = new File(sourcePath);
			File destFile = new File(destinationPath);
			Files.copy(sourceFile.toPath(), destFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// lấy ra danh sach cac file trong log co trang thai NR
	public ArrayList<LogModel> getLog() {
		ArrayList<LogModel> logs = new ArrayList<LogModel>();
		connection = connectDataConfig.connectConfigDatabase();
		String sql = "SELECT * FROM datawarehouse_configuration.log where load_staging_status='NR';";
		try {
			pre = connection.prepareStatement(sql);
			result = pre.executeQuery();
			while (result.next()) {
				int id = result.getInt(1);
				String fileName = result.getString(2);
				String fileLocation = result.getString(3);
				String loadStagingStatus = result.getString(4);
				String loadDataWarehouseStatus = result.getString(5);
				LogModel log = new LogModel(id, fileName, fileLocation, loadStagingStatus, loadDataWarehouseStatus);
				logs.add(log);
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

	public List<Student> readStudent(String filePath) {
		List<Student> students = new ArrayList<Student>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(filePath));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);

			Iterator iterator = sheet.iterator();
			while (iterator.hasNext()) {
				Row nextRow = (Row) iterator.next();
				// Not creating student object for header
				if (nextRow.getRowNum() == 0)
					continue;

				Student student = new Student();
				Iterator cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex + 1) {
					case 1:
						break;
					case 2:
						if (cell.getCellType() == CellType.STRING) {
							student.setMssv(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setMssv((int) cell.getNumericCellValue());
						}
						break;
					case 3:
						if (cell.getCellType() == CellType.STRING) {
							student.setFirstName(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setFirstName(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 4:
						if (cell.getCellType() == CellType.STRING) {
							student.setLastName(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setLastName(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 5:
						if (cell.getCellType() == CellType.STRING) {
							student.setDateOfBirth(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setDateOfBirth(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 6:
						if (cell.getCellType() == CellType.STRING) {
							student.setClassCode(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setClassCode(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 7:
						student.setClassName(cell.getStringCellValue());
						break;
					case 8:
						if (cell.getCellType() == CellType.STRING) {
							student.setPhoneNumber(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setPhoneNumber(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 9:
						if (cell.getCellType() == CellType.STRING) {
							student.setEmail(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setEmail(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 10:
						if (cell.getCellType() == CellType.STRING) {
							student.setCity(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setCity(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 11:
						if (cell.getCellType() == CellType.STRING) {
							student.setNote(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							student.setNote(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					}
				}
				if (student.getMssv() != 0) {
					students.add(student);
				}
			}
			workbook.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return students;
	}

	public List<Subject> readSubject(String path) {
		List<Subject> subjects = new ArrayList<Subject>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(path));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);

			Iterator iterator = sheet.iterator();
			while (iterator.hasNext()) {
				Row nextRow = (Row) iterator.next();
				// Not creating student object for header
				if (nextRow.getRowNum() == 0)
					continue;

				Subject subject = new Subject();
				Iterator cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex + 1) {
					case 1:
						if (cell.getCellType() == CellType.STRING) {
							subject.setStt(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setStt((int) cell.getNumericCellValue());
						}
						break;
					case 2:
						if (cell.getCellType() == CellType.STRING) {
							subject.setMsmh(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setMsmh((int) cell.getNumericCellValue());
						}
						break;
					case 3:
						if (cell.getCellType() == CellType.STRING) {
							subject.setTenmh(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setTenmh(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 4:
						if (cell.getCellType() == CellType.STRING) {
							subject.setStc(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setStc((int) cell.getNumericCellValue());
						}
						break;
					case 5:
						if (cell.getCellType() == CellType.STRING) {
							subject.setKhoaQly(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setKhoaQly(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 6:
						if (cell.getCellType() == CellType.STRING) {
							subject.setKhoaSuDung(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setKhoaSuDung(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 7:
						if (cell.getCellType() == CellType.STRING) {
							subject.setGhichu(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setGhichu(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					}
				}
				if (subject.getStt() != 0) {
					subjects.add(subject);
				}
			}
			workbook.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return subjects;
	}

	public List<LopHoc> readClass(String path) {
		List<LopHoc> listClass = new ArrayList<LopHoc>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(path));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);

			Iterator iterator = sheet.iterator();
			while (iterator.hasNext()) {
				Row nextRow = (Row) iterator.next();
				// Not creating student object for header
				if (nextRow.getRowNum() == 0)
					continue;

				LopHoc lophoc = new LopHoc();
				Iterator cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex + 1) {
					case 1:
						if (cell.getCellType() == CellType.STRING) {
							lophoc.setMaLH(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							lophoc.setMaLH(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 2:
						if (cell.getCellType() == CellType.STRING) {
							lophoc.setMaMH(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							lophoc.setMaMH((int) cell.getNumericCellValue());
						}
						break;
					case 3:
						if (cell.getCellType() == CellType.STRING) {
							lophoc.setNamhoc(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							lophoc.setNamhoc((int) (cell.getNumericCellValue()));
						}
						break;

					}
				}
				if (lophoc.getMaMH() != 0) {
					listClass.add(lophoc);
				}
			}
			workbook.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listClass;
	}

	public List<DangKy> readRegister(String path) {
		List<DangKy> registers = new ArrayList<DangKy>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(path));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);

			Iterator iterator = sheet.iterator();
			while (iterator.hasNext()) {
				Row nextRow = (Row) iterator.next();
				// Not creating student object for header
				if (nextRow.getRowNum() == 0)
					continue;

				DangKy dk = new DangKy();
				Iterator cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex + 1) {
					case 1:
						if (cell.getCellType() == CellType.STRING) {
							dk.setMaDK(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							dk.setMaDK(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 2:
						if (cell.getCellType() == CellType.STRING) {
							dk.setMssv(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							dk.setMssv((int) cell.getNumericCellValue());
						}
						break;
					case 3:
						if (cell.getCellType() == CellType.STRING) {
							dk.setMaLH(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							dk.setMaLH(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 4:
						if (cell.getCellType() == CellType.STRING) {
							dk.setNgayDK(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							dk.setNgayDK(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					}

				}
				if (dk.getMssv() != 0) {
					registers.add(dk);
				}
			}
			workbook.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return registers;
	}

	public int insertToStudent(String filePath) {
		List<Student> listST = readStudent(filePath);
		connection2 = connectDataConfig.connectDataStaging();
		String sql1 = "insert into datawarehouse_staging.data_student(mssv,firstName,lastName,dateOfBirth,classCode,className,phoneNumber,email,city,note) "
				+ "value(?,?,?,?,?,?,?,?,?,?)";
		int rowInsert = 0;
		try {
			connection2.setAutoCommit(false);
			pre = connection2.prepareStatement(sql1);
			for (Student st : listST) {
				pre.setInt(1, st.getMssv());
				pre.setString(2, st.getFirstName());
				pre.setString(3, st.getLastName());
				String date = st.getDateOfBirth();
				String[] date2 = date.split("/");
				LocalDate date3 = LocalDate.of(Integer.parseInt(date2[2]), Integer.parseInt(date2[1]),
						Integer.parseInt(date2[0]));
				pre.setDate(4, Date.valueOf(date3));
				pre.setString(5, st.getClassCode());
				pre.setString(6, st.getClassName());
				pre.setString(7, st.getPhoneNumber());
				pre.setString(8, st.getEmail());
				pre.setString(9, st.getCity());
				pre.setString(10, st.getNote());
				rowInsert += pre.executeUpdate();
				connection2.commit();
			}
			if (rowInsert != listST.size()) {
				connection2.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowInsert;
	}

	public int insertSubject(String path) {
		List<Subject> listSubject = readSubject(path);
		int rowInsert = 0;
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse_staging.data_subject(stt,msmh,tenmh,stc,khoaqly,khoasudung,ghichu)"
				+ " values (?,?,?,?,?,?,?)";
		try {
			connection.setAutoCommit(false);
			pre = connection.prepareStatement(sql);
			for (Subject s : listSubject) {
				pre.setInt(1, s.getStt());
				pre.setInt(2, s.getMsmh());
				pre.setString(3, s.getTenmh());
				pre.setInt(4, s.getStc());
				pre.setString(5, s.getKhoaQly());
				pre.setString(6, s.getKhoaSuDung());
				pre.setString(7, s.getGhichu());
				rowInsert += pre.executeUpdate();
				connection.commit();
			}
			if (rowInsert != listSubject.size()) {
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowInsert;
	}

	public int insertClass(String path) {
		List<LopHoc> listLH = readClass(path);
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse_staging.lophoc(maLH,maMH,namhoc) values(?,?,?)";
		int rowInserted = 0;
		try {
			connection.setAutoCommit(false);
			pre = connection.prepareStatement(sql);
			for (LopHoc l : listLH) {
				pre.setString(1, l.getMaLH());
				pre.setInt(2, l.getMaMH());
				pre.setInt(3, l.getNamhoc());
				rowInserted += pre.executeUpdate();
				connection.commit();
			}
			if (rowInserted != listLH.size()) {
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowInserted;
	}

	public int insertRegister(String path) {
		List<DangKy> listDK = readRegister(path);
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse_staging.dangky(madk,mssv,maLH,ngayDK) values(?,?,?,?)";
		int rowInserted = 0;
		try {
			connection.setAutoCommit(false);
			pre = connection.prepareStatement(sql);
			for (DangKy d : listDK) {
				pre.setString(1, d.getMaDK());
				pre.setInt(2, d.getMssv());
				pre.setString(3, d.getMaLH());
				String[] date = d.getNgayDK().split("/");
				LocalDate ngayDK = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]),
						Integer.parseInt(date[0]));
				pre.setDate(4, Date.valueOf(ngayDK));
				rowInserted += pre.executeUpdate();
				connection.commit();
			}
			if (rowInserted != listDK.size()) {
				connection.rollback();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowInserted;
	}

	public int insertDate_Dim(String location) {
		String readDate = connectDataConfig.readFileFromFolder(location);
		Connection connection = connectDataConfig.connectDataStaging();
		String sql = "insert into datawarehouse.date_dim_without_quarter "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int rowInserted = 0;
		try {
			connection.setAutoCommit(false);
			PreparedStatement pre = connection.prepareStatement(sql);
			String[] data = readDate.split("\n");
			for (String d : data) {
				String[] date = d.split(",");
				pre.setInt(1, Integer.parseInt(date[0]));
				String[] date2 = date[1].split("/");
				LocalDate localDate = LocalDate.of(Integer.parseInt(date2[2]), Integer.parseInt(date2[0]),
						Integer.parseInt(date2[1]));
				pre.setDate(2, Date.valueOf(localDate));
				pre.setInt(3, Integer.parseInt(date[2]));
				pre.setInt(4, Integer.parseInt(date[3]));
				pre.setString(5, date[4]);
				pre.setString(6, date[5]);
				pre.setString(7, date[6]);
				pre.setString(8, date[7]);
				pre.setInt(9, Integer.parseInt(date[8]));
				pre.setInt(10, Integer.parseInt(date[9]));
				pre.setInt(11, Integer.parseInt(date[10]));
				pre.setString(12, date[11]);
				pre.setString(13, date[12]);
				pre.setInt(14, Integer.parseInt(date[13]));
				pre.setString(15, date[14]);
				pre.setString(16, date[15]);
				pre.setString(17, date[16]);
				pre.setString(18, date[17]);
				rowInserted += pre.executeUpdate();
				connection.commit();
			}
			if (rowInserted != data.length) {
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Insert Date dim sucess");
		return rowInserted;
	}

	public void updateLog(int id) {
		String sql5 = "update datawarehouse_configuration.`control.data_file` set file_status ='ST' where data_file_id = "
				+ id;
		try {
			connection = connectDataConfig.connectConfigDatabase();
			stm.executeUpdate(sql5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mainProcessStaging(int id) {// truyền vào tham số là id của process

		String sql = "select ct.data_file_id,ct.file_name,ct.file_status,cf.data_file_config_name,cf.import_dir,cf.sucess_dir,cf.error_dir,cf.data_file_target_table "
				+ "from datawarehouse_configuration.`control.data_file` as ct "
				+ "join datawarehouse_configuration.`control.data_file_configuration` as cf "
				+ "on ct.data_config_id = cf.data_file_config_id where ct.data_config_id = " + id
				+ " and file_status='DL' " + "limit 1";

		String sql2 = "SELECT p.status FROM datawarehouse_configuration.`control.process` as p where process_id = "
				+ id;
		String sql3 = "update datawarehouse_configuration.`control.process` set status = 'start'  where process_name = 'staging';";
		String sql4 = "update datawarehouse_configuration.`control.process` set status = 'finish' where process_name = 'staging';";

		try {
			Properties props = new Properties();// doc thong tin de gui mail
			InputStream input = new FileInputStream("config/mailconfig.properties");
			props.load(input);
			String toAdmin = props.getProperty("to.staging.admin");
			String subject = props.getProperty("to.staging.error.subject");
			String text = props.getProperty("to.staging.error.text");

			connection = connectDataConfig.connectConfigDatabase();
			pre = connection.prepareStatement(sql);
			pre2 = connection.prepareStatement(sql2);
			stm = connection.createStatement();
			pre4 = connection.prepareStatement(sql4);
			result = pre.executeQuery();

			int data_file_id = 0;
			String file_name = "";
			String file_status = "";
			String config_name = "";
			String import_dir = "";
			String sucess_dir = "";
			String error_dir = "";
			String data_file_target_table = "";
			int count = 0;
			while (result.next()) {
				data_file_id = result.getInt(1);
				file_name = result.getString(2);
				file_status = result.getString(3);
				config_name = result.getString(4);
				import_dir = result.getString(5);
				sucess_dir = result.getString(6) + "/" + file_name;
				error_dir = result.getString(7) + "/" + file_name;
				data_file_target_table = result.getString(8);
			}
			System.out.println("config name : " + config_name);
			String sql5 = "update datawarehouse_configuration.`control.data_file` as ct SET ct.dt_file_extracted = ?, staging_load_count=?"
					+ " where ct.file_status='ST' and ct.data_file_id = " + data_file_id;
			String sql6 = "truncate table " + data_file_target_table;
			String sql7 = "update datawarehouse_configuration.`control.data_file` set file_status ='ERR' where data_file_id = "
					+ data_file_id;
			pre5 = connection.prepareStatement(sql5);

			String path = import_dir + "\\" + file_name;
			result2 = pre2.executeQuery();
			String status = "";
			while (result2.next()) {
				status = result2.getString(1);
			}
			stm = connection.createStatement();
			if (status.equals("finish")) {
				stm.executeUpdate(sql3);// select
				stm.executeUpdate(sql6);// truncate
				System.out.println("truncate table " + data_file_target_table);
				if (config_name.equals("sinhvien")) {
					int rowInsert = insertToStudent(path);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert student sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						pre4.executeUpdate();
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}

				} else if (config_name.equals("lophoc")) {
					int rowInsert = insertClass(path);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert class sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				} else if (config_name.equals("dangky")) {
					int rowInsert = insertRegister(path);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert class sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				} else if (config_name.equals("datedim")) {
					int rowInsert = insertDate_Dim(path);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert datetime sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				}
			} else {
				if (config_name.equals("monhoc")) {
					int rowInsert = insertSubject(path);
					if (rowInsert > 0) {
						connection.setAutoCommit(false);
						pre4.executeUpdate();
						updateLog(data_file_id);
						pre5.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
						pre5.setInt(2, rowInsert);
						pre5.executeUpdate();
						System.out.println("insert subject sucess");
						connection.commit();
						moveFile(path, sucess_dir);
					} else {
						sendMail.sendMail(toAdmin, subject, text);
						moveFile(path, error_dir);
					}
				} else {
					System.out.println("co tien trinh khac dang chay");
					connection.close();
				}

			}
			if (status.equals("start")) {
				System.out.println("co tien trinh khac dang chay");
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		InsertData2 insert = new InsertData2();
		String path = "F:\\anh\\datawarehouse\\data\\dangky\\dangky_sang_nhom5.xlsx ";

		// System.out.println(insert.insertSubject(path));
		insert.mainProcessStaging(3);
>>>>>>> branch 'master' of https://github.com/Vocaotrinh1999/DataWarehouse
	}

}
