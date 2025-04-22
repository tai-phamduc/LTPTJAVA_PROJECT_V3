package entity;

import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.ColumnResult;

import java.io.Serializable;

@SqlResultSetMapping(
		name = "TrainDetailsMapping",
		classes = @ConstructorResult(
				targetClass = TrainDetails.class,
				columns = {
						@ColumnResult(name = "trainID", type = String.class),
						@ColumnResult(name = "trainNumber", type = String.class),
						@ColumnResult(name = "numberOfCoaches", type = Integer.class),
						@ColumnResult(name = "capacity", type = Integer.class),
						@ColumnResult(name = "numberOfCoachTypes", type = Integer.class),
						@ColumnResult(name = "coachTypes", type = String.class),
						@ColumnResult(name = "status", type = String.class)
				}
		)
)
public class TrainDetails implements Serializable {

	private String trainID;
	private String trainNumber;
	private int numberOfCoaches;
	private int capacity;
	private int numberOfCoachTypes;
	private String coachTypes;
	private String status;

	public TrainDetails(String trainID, String trainNumber, int numberOfCoaches, int capacity,
						int numberOfCoachTypes, String coachTypes, String status) {
		this.trainID = trainID;
		this.trainNumber = trainNumber;
		this.numberOfCoaches = numberOfCoaches;
		this.capacity = capacity;
		this.numberOfCoachTypes = numberOfCoachTypes;
		this.coachTypes = coachTypes;
		this.status = status;
	}

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

	public int getNumberOfCoaches() {
		return numberOfCoaches;
	}

	public void setNumberOfCoaches(int numberOfCoaches) {
		this.numberOfCoaches = numberOfCoaches;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getNumberOfCoachTypes() {
		return numberOfCoachTypes;
	}

	public void setNumberOfCoachTypes(int numberOfCoachTypes) {
		this.numberOfCoachTypes = numberOfCoachTypes;
	}

	public String getCoachTypes() {
		return coachTypes;
	}

	public void setCoachTypes(String coachTypes) {
		this.coachTypes = coachTypes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TrainDetails [trainID=" + trainID + ", trainNumber=" + trainNumber + ", numberOfCoaches="
				+ numberOfCoaches + ", capacity=" + capacity + ", numberOfCoachTypes=" + numberOfCoachTypes
				+ ", coachTypes=" + coachTypes + ", status=" + status + "]";
	}
}
