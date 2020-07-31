package LoadDataStaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class InsertData {
	ConnectDataConfig connectDataConfig;
	SendMailTLS sendMail;
	PreparedStatement pre, pre2, pre3;
	Connection connection, connection2;
	ResultSet result;
	String emailSendTo, subject, textMail;

	public InsertData() {
		// connection = new DAO().openConnection();
		connectDataConfig = new ConnectDataConfig();
		sendMail = new SendMailTLS();
		emailSendTo = "17130256@st.hcmuaf.edu.vn";
		subject = "";
		textMail = "";
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
						student.setLastName(cell.getStringCellValue());
						break;
					case 5:
						student.setDateOfBirth(cell.getStringCellValue());
						break;
					case 6:
						student.setClassCode(cell.getStringCellValue());
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
						student.setEmail(cell.getStringCellValue());
						break;
					case 10:
						student.setCity(cell.getStringCellValue());
						break;
					case 11:
						student.setNote(cell.getStringCellValue());
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
							subject.setMsmh2013(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setMsmh2013((int) cell.getNumericCellValue());
						}
						break;
					case 3:
						if (cell.getCellType() == CellType.STRING) {
							subject.setTenmh2013(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setTenmh2013(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 4:
						if (cell.getCellType() == CellType.STRING) {
							subject.setStc2013(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setStc2013((int) cell.getNumericCellValue());
						}
						break;
					case 5:
						subject.setKhoaQly2013(cell.getStringCellValue());
						break;
					case 6:
						subject.setKhoaSuDung(cell.getStringCellValue());
						break;
					case 7:
						if (cell.getCellType() == CellType.STRING) {
							subject.setMsmh2014(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setMsmh2014((int) cell.getNumericCellValue());
						}
					case 8:
						if (cell.getCellType() == CellType.STRING) {
							subject.setTenmh2014(cell.getStringCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setTenmh2014(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case 9:
						if (cell.getCellType() == CellType.STRING) {
							subject.setStc2014(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							subject.setStc2014((int) cell.getNumericCellValue());
						}
						break;
					case 10:
						subject.setKhoaQly2014(cell.getStringCellValue());
						break;
					case 11:
						subject.setGhichu(cell.getStringCellValue());
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
							dk.setMssv(Integer.parseInt(cell.getStringCellValue()));
						} else if (cell.getCellType() == CellType.NUMERIC) {
							dk.setMssv((int) cell.getNumericCellValue());
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

	public boolean insertToStudent(String filePath) {
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
				return false;
			}
			connection2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean insertSubject(String path) {
		List<Subject> listSubject = readSubject(path);
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse.data_subject(stt,msmh,tenmh,stc,khoaqly,khoasudung,ghichu,version,expriteDate)"
				+ " values (?,?,?,?,?,?,?,?,?)";
		String sql2 = "update datawarehouse.data_subject set expriteDate = ? where version = 1";
		try {
			pre = connection.prepareStatement(sql);
			pre2 = connection.prepareStatement(sql2);
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean insertClass(String path) {
		List<LopHoc> listLH = new ArrayList<LopHoc>();
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse.lophoc(maLH,maMH,namhoc) values(?,?,?)";
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
				return false;
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean insertRegister(String path) {
		List<DangKy> listDK = readRegister(path);
		connection = connectDataConfig.connectDataWarehouse();
		String sql = "insert into datawarehouse.dangky(madk,mssv,maLH,ngayDK) values(?,?,?,?)";
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
				connection.rollback();

			}
			if (rowInserted != listDK.size()) {
				connection.rollback();
				return false;
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void run(int id) {
		connection = connectDataConfig.connectConfigDatabase();
		String sql1 = "select * from datawarehouse_configuration.database_control where id = " + id;
		try {
			pre = connection.prepareStatement(sql1);
			result = pre.executeQuery();
			String location = "";
			String fileName = "";
			String targetTable = "";
			String sucessDir = "";
			String errorDir = "";
			while (result.next()) {
				location += result.getString(3);
				fileName += result.getString(2);
				targetTable += result.getString(4);
				sucessDir += result.getString(8) + "\\" + fileName;
				errorDir += result.getString(9) + "\\" + fileName;
			}
			System.out.println("Location : " + location);
			if (targetTable.equals("sinhvien")) {
				if (insertToStudent(location)) {
					moveFile(location, sucessDir);
				} else {
					moveFile(location, errorDir);
				}
			} else if (targetTable.equals("monhoc")) {
				if (insertSubject(location)) {
					moveFile(location, sucessDir);
				} else {
					moveFile(location, errorDir);
				}
			} else if (targetTable.equals("lophoc")) {
				if (insertClass(location)) {
					moveFile(location, sucessDir);
				} else {
					moveFile(location, errorDir);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		InsertData insert = new InsertData();
		String path = "F:\\anh\\datawarehouse\\data\\monhocnhap.xlsx";
		// System.out.println(insert.insertToStudent(path));
		// System.out.println(insert.insertSubject(path));
		insert.run(11);
	}
}
