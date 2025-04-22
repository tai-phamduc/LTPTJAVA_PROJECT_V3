package entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "TrainJourney")
public class TrainJourney implements Serializable {

	@Id
	@Column(name = "TrainJourneyID", length = 12, nullable = false)
	private String trainJourneyID;

	@Column(name = "TrainJourneyName", nullable = false, length = 255)
	private String trainJourneyName;

	@ManyToOne
	@JoinColumn(name = "TrainID", nullable = false)
	private Train train;

	@Column(name = "BasePrice", nullable = false)
	private double basePrice;

	@ManyToOne
	@JoinColumn(name = "LineID", nullable = false)
	private Line line;

	// No-argument constructor
	public TrainJourney() {
		super();
	}

	public TrainJourney(String trainJourneyID) {
		super();
		this.trainJourneyID = trainJourneyID;
	}

	public TrainJourney(String trainJourneyID, String traInJourneyName, Train train, double basePrice, Line line) {
		super();
		this.trainJourneyID = trainJourneyID;
		this.trainJourneyName = traInJourneyName;
		this.train = train;
		this.basePrice = basePrice;
		this.line = line;
	}

	public TrainJourney(String tenChuyenTau, Train tau, Line duongDi, double giaGoc) {
		this.trainJourneyName = tenChuyenTau;
		this.train = tau;
		this.line = duongDi;
		this.basePrice = giaGoc;
	}

	public String getTrainJourneyID() {
		return trainJourneyID;
	}

	public void setTrainJourneyID(String trainJourneyID) {
		this.trainJourneyID = trainJourneyID;
	}

	public String getTrainJourneyName() {
		return trainJourneyName;
	}

	public void setTrainJourneyName(String trainJourneyName) {
		this.trainJourneyName = trainJourneyName;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "TrainJourney [trainJourneyID=" + trainJourneyID + ", trainJourneyName=" + trainJourneyName + ", train="
				+ train + ", basePrice=" + basePrice + ", line=" + line + "]";
	}
}
