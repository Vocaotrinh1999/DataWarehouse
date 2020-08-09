package LoadDataStaging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model.DangKy;
import Model.LopHoc;
import Model.Student;
import Model.Subject;

public class ReadDataFromFile {

	public ReadDataFromFile() {

	}

	public String readFileFromFolder(String file) {
		String result = "";
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), "UTF-8"));
			String line = null;
			while ((line = bf.readLine()) != null) {
				result += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<Student> readStudentTxtOrCSV(String path) {
		String result = readFileFromFolder(path);
		String[] data = result.split("\n");
		List<Student> list = new ArrayList<Student>();

		for (int i = 1; i < data.length; i++) {
			String d = data[i];
			Student s = new Student();
			String[] o = d.split(",");
			s.setMssv(Integer.parseInt(o[0]));
			s.setFirstName(o[1]);
			s.setLastName(o[2]);
			s.setDateOfBirth(o[3]);
			s.setClassCode(o[4]);
			s.setClassName(o[5]);
			s.setPhoneNumber(o[6]);
			s.setEmail(o[7]);
			s.setCity(o[8]);
			s.setNote(o[9]);
			if (s != null) {
				list.add(s);
			}
		}
		return list;
	}

	public List<LopHoc> readClassTxtOrCSV(String path) {
		List<LopHoc> list = new ArrayList<LopHoc>();
		String result = readFileFromFolder(path);
		String[] data = result.split("\n");
		for (int i = 1; i < data.length; i++) {// doc dong dau tien loai cai header
			String d = data[i];
			LopHoc l = new LopHoc();
			String[] o = d.split(",");
			l.setMaLH(o[0]);
			l.setMaMH(Integer.parseInt(o[1]));
			l.setNamhoc(Integer.parseInt(o[2]));
			if (l != null) {
				list.add(l);
			}
		}
		return list;
	}

	public List<Subject> readSubjectTxtOrCsv(String path) {
		List<Subject> list = new ArrayList<Subject>();
		String result = readFileFromFolder(path);
		String[] data = result.split("\n");
		for (int i = 1; i < data.length; i++) {
			String d = data[i];
			String[] o = d.split(",");
			Subject s = new Subject();
			s.setStt(Integer.parseInt(o[0]));
			s.setMsmh(Integer.parseInt(o[1]));
			s.setTenmh(o[2]);
			s.setStc(Integer.parseInt(o[3]));
			s.setKhoaQly(o[4]);
			s.setKhoaSuDung(o[5]);
			s.setGhichu(o[6]);
			if (s != null) {
				list.add(s);
			}
		}
		return list;
	}

	public List<DangKy> readRegisterFromTxtOrCSV(String path) {
		List<DangKy> list = new ArrayList<DangKy>();
		String result = readFileFromFolder(path);
		String[] data = result.split("\n");
		for (int i = 1; i < data.length; i++) {
			String d = data[i];
			String[] o = d.split(",");
			DangKy dk = new DangKy();
			dk.setMaDK(o[0]);
			dk.setMssv(Integer.parseInt(o[1]));
			dk.setMaLH(o[2]);
			dk.setNgayDK(o[3]);
			if (dk != null) {
				list.add(dk);
			}
		}
		return list;
	}

	public List<Student> readStudentFromExcel(String filePath) {
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

	public List<Subject> readSubjectFromExcel(String path) {
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

	public List<LopHoc> readClassFromExcel(String path) {
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

	public List<DangKy> readRegisterFromExcel(String path) {
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

	public static void main(String[] args) {
		ReadDataFromFile r = new ReadDataFromFile();
		List<DangKy> l = r.readRegisterFromExcel("F:\\anh\\datawarehouse\\data\\dangky\\dangky_sang_nhom5.xlsx");
		for (DangKy d : l) {
			System.out.println(d.toString());
		}
	}
}
