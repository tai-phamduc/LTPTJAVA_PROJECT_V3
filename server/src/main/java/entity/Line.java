package entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "Line")
public class Line implements Serializable {

	@Id
	@Column(name = "LineID", length = 12)
	private String lineID;

	@Column(name = "LineName", nullable = false, length = 255)
	private String lineName;

	public Line() {
	}

	public Line(String lineID, String lineName) {
		super();
		this.lineID = lineID;
		this.lineName = lineName;
	}

	public Line(String lineID) {
		this.lineID = lineID;
	}

	public String getLineID() {
		return lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	@Override
	public String toString() {
		return this.lineName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lineID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		return Objects.equals(lineID, other.lineID);
	}
}
