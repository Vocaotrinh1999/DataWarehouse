package Model;

public class Student {

	private int mssv;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String classCode;
	private String className;
	private String phoneNumber;// truong hop so dien thoai có chua so 0
	private String email;
	private String city;// ten dia chi sinh song
	private String note;// ghi chu ve sinh vien

	public Student() {

	}

	public Student(int mssv, String firstName, String lastName, String dateOfBirth, String classCode, String className,
			String phoneNumber, String email, String city, String note) {
		super();
		this.mssv = mssv;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.classCode = classCode;
		this.className = className;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.city = city;
		this.note = note;
	}

	public int getMssv() {
		return mssv;
	}

	public void setMssv(int mssv) {
		this.mssv = mssv;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return mssv + ", " + firstName + ", " + lastName + ", " + dateOfBirth + ", " + classCode + ", " + className
				+ ", " + phoneNumber + ", " + email + ", " + city + ", " + note;
	}

}
