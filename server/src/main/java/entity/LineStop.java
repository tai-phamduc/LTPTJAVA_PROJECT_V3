package entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "LineStop")
@IdClass(LineStopId.class)  // Use @IdClass to specify the composite key
public class LineStop implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "LineID", referencedColumnName = "LineID", nullable = false)
	private Line line;

	@Id
	@ManyToOne
	@JoinColumn(name = "StationID", referencedColumnName = "StationID", nullable = false)
	private Station station;

	@Column(name = "StopOrder", nullable = false)
	private int stopOrder;

	@Column(name = "Distance", nullable = false)
	private int distance;

	// No-argument constructor (required by JPA)
	public LineStop() {
		super();
	}

	// Constructor with parameters
	public LineStop(Line line, Station station, int stopOrder, int distance) {
		super();
		this.line = line;
		this.station = station;
		this.stopOrder = stopOrder;
		this.distance = distance;
	}

	// Getters and setters
	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
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

	@Override
	public String toString() {
		return "LineStop [line=" + line + ", station=" + station + ", stopOrder=" + stopOrder + ", distance=" + distance
				+ "]";
	}
}

class LineStopId implements Serializable {
	private String line;
	private String station;

	// Default constructor
	public LineStopId() {}

	public LineStopId(String line, String station) {
		this.line = line;
		this.station = station;
	}

	// Getters and setters
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	// hashCode() and equals() are necessary for composite keys
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		LineStopId that = (LineStopId) obj;
		return line.equals(that.line) && station.equals(that.station);
	}

	@Override
	public int hashCode() {
		return line.hashCode() + station.hashCode();
	}
}
