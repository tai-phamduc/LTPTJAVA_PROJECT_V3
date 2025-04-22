package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Stop")
public class Stop implements Serializable {

	@Id
	@Column(name = "StopID", length = 12)
	private String stopID;

	@ManyToOne
	@JoinColumn(name = "trainJourneyID", nullable = false)
	private TrainJourney trainJourney;

	@ManyToOne
	@JoinColumn(name = "stationID", nullable = false)
	private Station station;

	@Column(name = "stopOrder", nullable = false)
	private int stopOrder;

	@Column(name = "distance", nullable = false)
	private int distance;

	@Column(name = "departureDate", nullable = false)
	private LocalDate departureDate;

	@Column(name = "arrivalTime", nullable = false)
	private LocalTime arrivalTime;

	@Column(name = "departureTime", nullable = false)
	private LocalTime departureTime;

	public Stop() {
	}

	public Stop(Station station, int stopOrder, int distance, LocalDate departureDate, LocalTime arrivalTime,
				LocalTime departureTime) {
		this.station = station;
		this.stopOrder = stopOrder;
		this.distance = distance;
		this.departureDate = departureDate;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
	}

	public Stop(String stopID, TrainJourney trainJourney, Station station, int stopOrder, int distance,
				LocalDate departureDate, LocalTime arrivalTime, LocalTime departureTime) {
		this.stopID = stopID;
		this.trainJourney = trainJourney;
		this.station = station;
		this.stopOrder = stopOrder;
		this.distance = distance;
		this.departureDate = departureDate;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
	}

	public Stop(String stopID) {
		super();
		this.stopID = stopID;
	}

	public Stop(String stopID, LocalDate departureDate, LocalTime arrivalTime, LocalTime departureTime) {
		this.stopID = stopID;
		this.departureDate = departureDate;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
	}

	public String getStopID() {
		return stopID;
	}

	public void setStopID(String stopID) {
		this.stopID = stopID;
	}

	public TrainJourney getTrainJourney() {
		return trainJourney;
	}

	public void setTrainJourney(TrainJourney trainJourney) {
		this.trainJourney = trainJourney;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public int getStopOrder() {
		return stopOrder;
	}

	public void setStopOrder(int stopOrder) {
		this.stopOrder = stopOrder;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}

	@Override
	public String toString() {
		return "Stop [stopID=" + stopID + ", trainJourney=" + trainJourney + ", station=" + station + ", stopOrder="
				+ stopOrder + ", distance=" + distance + ", arrivalTime=" + arrivalTime + ", departureTime="
				+ departureTime + "]";
	}
}
