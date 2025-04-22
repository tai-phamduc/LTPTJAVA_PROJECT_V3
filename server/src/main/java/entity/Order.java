package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Order")
public class Order implements Serializable {

	@Id
	@Column(name = "OrderID", length = 12)
	private String orderID;

	@Column(name = "OrderDate", nullable = false)
	private LocalDateTime orderDate;

	@Column(name = "Note", length = 255)
	private String note;

	@Column(name = "TimeRemaining", nullable = false)
	private LocalTime timeRemaining;

	@Column(name = "OrderStatus", nullable = false, length = 30)
	private String orderStatus;

	@ManyToOne
	@JoinColumn(name = "CustomerID", nullable = false)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "TrainJourneyID", nullable = false)
	private TrainJourney trainJourney;

	@ManyToOne
	@JoinColumn(name = "EmployeeID", nullable = false)
	private Employee employee;

	@Column(name = "TaxID", length = 6, nullable = false)
	private String taxID;

	public Order() {
	}

	public Order(String orderID) {
		super();
		this.orderID = orderID;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalTime getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(LocalTime timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public TrainJourney getTrainJourney() {
		return trainJourney;
	}

	public void setTrainJourney(TrainJourney trainJourney) {
		this.trainJourney = trainJourney;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getTaxID() {
		return taxID;
	}

	public void setTaxID(String taxID) {
		this.taxID = taxID;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", orderDate=" + orderDate + ", note=" + note + ", timeRemaining="
				+ timeRemaining + ", orderStatus=" + orderStatus + ", customer=" + customer + ", trainJourney="
				+ trainJourney + ", employee=" + employee + "]";
	}
}
