package entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Employee")
public class Employee implements Serializable {

	@Id
	@Column(name = "EmployeeID", length = 12, columnDefinition = "CHAR(12)")
	private String employeeID;

	@Column(name = "FullName", nullable = false, length = 100)
	private String fullName;

	@Column(name = "Gender", nullable = false)
	private boolean gender;

	@Column(name = "DateOfBirth", nullable = false)
	private LocalDate dateOfBirth;

	@Column(name = "Email", nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "PhoneNumber", length = 20)
	private String phoneNumber;

	@Column(name = "Role", nullable = false, length = 50)
	private String role;

	@Column(name = "StartingDate", nullable = false)
	private LocalDate startingDate;

	@Column(name = "ImageSource", length = 255)
	private String imageSource;

	@OneToOne(mappedBy = "employee")
	private Account account;

	// Original constructors (unchanged)
	public Employee(String employeeID, String fullName, boolean gender, LocalDate dateOfBirth, String email,
					String phoneNumber, String role, LocalDate startingDate) {
		super();
		this.employeeID = employeeID;
		this.fullName = fullName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.startingDate = startingDate;
	}

	public Employee(String fullName, boolean gender, LocalDate dateOfBirth, String email, String phoneNumber,
					String role, LocalDate startingDate) {
		super();
		this.fullName = fullName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.startingDate = startingDate;
	}

	public Employee(String employeeID, String fullName, boolean gender, LocalDate dateOfBirth, String email,
					String phoneNumber, String role, LocalDate startingDate, String imageSource) {
		super();
		this.employeeID = employeeID;
		this.fullName = fullName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.startingDate = startingDate;
		this.imageSource = imageSource;
	}

	public Employee(String employeeID, String fullName, boolean gender, LocalDate dateOfBirth, String email,
					String phoneNumber, String role) {
		super();
		this.employeeID = employeeID;
		this.fullName = fullName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	public Employee(String employeeID) {
		this.employeeID = employeeID;
	}

	// JPA-required no-arg constructor (added)
	public Employee() {
	}

	// Original getters and setters (unchanged)
	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

	public String getImageSource() {
		return imageSource;
	}

	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}

	// Added for JPA relationship (doesn't affect existing code)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	// Original toString (unchanged)
	@Override
	public String toString() {
		return "Employee [employeeID=" + employeeID + ", fullName=" + fullName + ", gender=" + gender + ", dateOfBirth="
				+ dateOfBirth + ", email=" + email + ", phoneNumber=" + phoneNumber + ", role=" + role
				+ ", startingDate=" + startingDate + ", imageSource=" + imageSource + "]";
	}
}