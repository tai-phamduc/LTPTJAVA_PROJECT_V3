package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "Customer")
public class Customer implements Serializable {

	@Id
	@Column(name = "CustomerID", length = 12)
	private String customerID;

	@Column(name = "fullName", nullable = false, length = 255)
	private String fullName;

	@Column(name = "phoneNumber", nullable = false, length = 15)
	private String phoneNumber;

	@Column(name = "email", nullable = false, length = 255, unique = true)
	private String email;

	@Column(name = "identificationNumber", nullable = false, length = 50)
	private String identificationNumber;

	public Customer() {
	}

	public Customer(String customerID, String fullName, String phoneNumber, String email, String identificationNumber) {
		super();
		this.customerID = customerID;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.identificationNumber = identificationNumber;
	}

	public Customer(String fullName, String phoneNumber, String email, String identificationNumber) {
		super();
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.identificationNumber = identificationNumber;
	}

	public Customer(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + ", identificationNumber=" + identificationNumber + "]";
	}
}
