package entity;

import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.ColumnResult;

import java.io.Serializable;

@SqlResultSetMapping(
		name = "CustomerRankingMapping",
		classes = @ConstructorResult(
				targetClass = CustomerRanking.class,
				columns = {
						@ColumnResult(name = "customerName", type = String.class),
						@ColumnResult(name = "phoneNumber", type = String.class),
						@ColumnResult(name = "totalSpending", type = Long.class)
				}
		)
)
public class CustomerRanking implements Serializable {

	private String customerName;
	private String phoneNumber;
	private long totalSpending;

	public CustomerRanking(String customerName, String phoneNumber, long totalSpending) {
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.totalSpending = totalSpending;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getTotalSpending() {
		return totalSpending;
	}

	public void setTotalSpending(long totalSpending) {
		this.totalSpending = totalSpending;
	}

	@Override
	public String toString() {
		return "CustomerRanking [customerName=" + customerName + ", phoneNumber=" + phoneNumber + ", totalSpending=" + totalSpending + "]";
	}
}
