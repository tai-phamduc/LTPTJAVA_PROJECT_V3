package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Train") // Assuming table name is "Train" (adjust if different)
public class Train implements Serializable {

	@Id
	@Column(name = "TrainID") // Matches your database column name
	private String trainID;

	@Column(name = "TrainNumber", nullable = false) // Assuming trainNumber cannot be null
	private String trainNumber;

	@Column(name = "Status", nullable = false) // Assuming status cannot be null
	private String status;

	// Original constructors (unchanged)
	public Train(String trainID, String trainNumber, String status) {
		super();
		this.trainID = trainID;
		this.trainNumber = trainNumber;
		this.status = status;
	}

	public Train(String soHieuTau, String trangThai) {
		this.trainNumber = soHieuTau;
		this.status = trangThai;
	}

	public Train() {
	}

	// Original getters and setters (unchanged)
	public String getTrainID() {
		return trainID;
	}

	public void setTrainID(String trainID) {
		this.trainID = trainID;
	}

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// Original toString (unchanged)
	@Override
	public String toString() {
		return this.getTrainNumber();
	}
}