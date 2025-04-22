package entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@IdClass(ServiceDetailId.class)
@Table(name = "ServiceDetail")
public class ServiceDetail implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "ServiceID", nullable = false)
	private Service service;

	@Id
	@ManyToOne
	@JoinColumn(name = "OrderID", nullable = false)
	private Order order;

	@Column(name = "Quantity", nullable = false)
	private int quantity;

	public ServiceDetail() {
	}

	public ServiceDetail(Service service, Order order, int quantity) {
		super();
		this.service = service;
		this.order = order;
		this.quantity = quantity;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}

class ServiceDetailId implements Serializable {
	private String service;
	private String order;

	public ServiceDetailId() {
	}

	public ServiceDetailId(String service, String order) {
		this.service = service;
		this.order = order;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ServiceDetailId)) return false;
		ServiceDetailId that = (ServiceDetailId) o;
		return Objects.equals(service, that.service) &&
				Objects.equals(order, that.order);
	}

	@Override
	public int hashCode() {
		return Objects.hash(service, order);
	}
}
