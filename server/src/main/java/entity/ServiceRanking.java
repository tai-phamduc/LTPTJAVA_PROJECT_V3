package entity;

import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.ColumnResult;

import java.io.Serializable;

@SqlResultSetMapping(
		name = "ServiceRankingMapping",
		classes = @ConstructorResult(
				targetClass = ServiceRanking.class,
				columns = {
						@ColumnResult(name = "serviceName", type = String.class),
						@ColumnResult(name = "salesQuantity", type = Integer.class),
						@ColumnResult(name = "revenue", type = Long.class)
				}
		)
)
public class ServiceRanking implements Serializable {

	private String serviceName;
	private int salesQuantity;
	private long revenue;

	public ServiceRanking(String serviceName, int salesQuantity, long revenue) {
		this.serviceName = serviceName;
		this.salesQuantity = salesQuantity;
		this.revenue = revenue;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getSalesQuantity() {
		return salesQuantity;
	}

	public void setSalesQuantity(int salesQuantity) {
		this.salesQuantity = salesQuantity;
	}

	public long getRevenue() {
		return revenue;
	}

	public void setRevenue(long revenue) {
		this.revenue = revenue;
	}

	@Override
	public String toString() {
		return "ServiceRanking [serviceName=" + serviceName + ", salesQuantity=" + salesQuantity + ", revenue=" + revenue + "]";
	}
}
