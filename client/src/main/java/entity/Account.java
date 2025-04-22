package entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Account")
public class Account implements Serializable {

	@Id
	@Column(name = "Username", length = 50, nullable = false)
	private String username;

	@Column(name = "Password", length = 255, nullable = false)
	private String password;

	@OneToOne
	@JoinColumn(name = "EmployeeID", referencedColumnName = "EmployeeID", nullable = false)
	private Employee employee;

	// Original constructors (unchanged)
	public Account() {
	}

	public Account(String username, String password, Employee employee) {
		super();
		this.username = username;
		this.password = password;
		this.employee = employee;
	}

	// Original getters and setters (unchanged)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	// Original toString (unchanged)
	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + ", employee=" + employee + "]";
	}
}