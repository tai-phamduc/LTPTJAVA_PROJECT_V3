	package entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;

    import java.io.Serializable;
    import java.util.Objects;
	
	@Entity
	@Table(name = "Service")
	public class Service implements Serializable {
	
		@Id
		@Column(name = "ServiceID", length = 12)
		private String serviceID;
	
		@Column(name = "ServiceName", nullable = false, length = 100)
		private String serviceName;
	
		@Column(name = "Price", nullable = false)
		private double price;
	
		@Column(name = "Type", nullable = false, length = 10)
		private String type;
	
		@Column(name = "ImageSource", length = 100)
		private String imageSource;
	
		public Service() {
		}
	
		public Service(String serviceID) {
			this.serviceID = serviceID;
		}
	
		public Service(String serviceID, String serviceName, double price, String type, String imageSource) {
			super();
			this.serviceID = serviceID;
			this.serviceName = serviceName;
			this.price = price;
			this.type = type;
			this.imageSource = imageSource;
		}
	
		public Service(String serviceName, double price, String type, String imageSource) {
			super();
			this.serviceName = serviceName;
			this.price = price;
			this.type = type;
			this.imageSource = imageSource;
		}
	
		public String getServiceID() {
			return serviceID;
		}
	
		public void setServiceID(String serviceID) {
			if (this.serviceID == null) {
				this.serviceID = serviceID;
			}
		}
	
		public String getServiceName() {
			return serviceName;
		}
	
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
	
		public double getPrice() {
			return price;
		}
	
		public void setPrice(double price) {
			this.price = price;
		}
	
		public String getType() {
			return type;
		}
	
		public void setType(String type) {
			this.type = type;
		}
	
		public String getImageSource() {
			return imageSource;
		}
	
		public void setImageSource(String imageSource) {
			this.imageSource = imageSource;
		}
	
		@Override
		public String toString() {
			return "Service [serviceID=" + serviceID + ", serviceName=" + serviceName + ", price=" + price + ", type="
					+ type + ", setImageSource=" + imageSource + "]";
		}
	
		@Override
		public int hashCode() {
			return Objects.hash(serviceID);
		}
	
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Service other = (Service) obj;
			return Objects.equals(serviceID, other.serviceID);
		}
	}
