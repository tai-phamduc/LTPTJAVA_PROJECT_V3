package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@SqlResultSetMapping(
		name = "CoachTypeTotalIncomeMapping",
		classes = @ConstructorResult(
				targetClass = CoachTypeTotalIncome.class,
				columns = {
						@ColumnResult(name = "CoachType", type = String.class),
						@ColumnResult(name = "TotalTicketIncome", type = BigDecimal.class)
				}
		)
)
public class CoachTypeTotalIncome implements Serializable {

	private String coachType;
	private BigDecimal coachTypeTotalIncome;

	public CoachTypeTotalIncome(String coachType, BigDecimal coachTypeTotalIncome) {
		this.coachType = coachType;
		this.coachTypeTotalIncome = coachTypeTotalIncome;
	}

	public String getCoachType() {
		return coachType;
	}

	public void setCoachType(String coachType) {
		this.coachType = coachType;
	}

	public BigDecimal getCoachTypeTotalIncome() {
		return coachTypeTotalIncome;
	}

	public void setCoachTypeTotalIncome(BigDecimal coachTypeTotalIncome) {
		this.coachTypeTotalIncome = coachTypeTotalIncome;
	}

	@Override
	public String toString() {
		return "CoachTypeTotalIncome [coachType=" + coachType + ", coachTypeTotalIncome=" + coachTypeTotalIncome + "]";
	}
}
