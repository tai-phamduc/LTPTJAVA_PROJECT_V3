package entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "TicketDetail")
@IdClass(TicketDetailPK.class)  // Specifies the composite key class
public class TicketDetail implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "StopID", referencedColumnName = "StopID", nullable = false)
	private Stop stopID;

	@Id
	@ManyToOne
	@JoinColumn(name = "TicketID", referencedColumnName = "TicketID", nullable = false)
	private Ticket ticketID;

	// Default constructor (required by JPA)
	public TicketDetail() {
		super();
	}

	// Constructor with parameters
	public TicketDetail(Stop stopID, Ticket ticketID) {
		this.stopID = stopID;
		this.ticketID = ticketID;
	}

	// Getters and setters
	public Stop getStopID() {
		return stopID;
	}

	public void setStopID(Stop stopID) {
		this.stopID = stopID;
	}

	public Ticket getTicketID() {
		return ticketID;
	}

	public void setTicketID(Ticket ticketID) {
		this.ticketID = ticketID;
	}

	@Override
	public String toString() {
		return "TicketDetail [stopID=" + stopID + ", ticketID=" + ticketID + "]";
	}
}

class TicketDetailPK implements Serializable {

	private String stopID;
	private String ticketID;

	// Default constructor
	public TicketDetailPK() {}

	public TicketDetailPK(String stopID, String ticketID) {
		this.stopID = stopID;
		this.ticketID = ticketID;
	}

	// Getters and setters
	public String getStopID() {
		return stopID;
	}

	public void setStopID(String stopID) {
		this.stopID = stopID;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	// hashCode and equals are necessary for composite keys
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		TicketDetailPK that = (TicketDetailPK) obj;
		return stopID.equals(that.stopID) && ticketID.equals(that.ticketID);
	}

	@Override
	public int hashCode() {
		return stopID.hashCode() + ticketID.hashCode();
	}
}
