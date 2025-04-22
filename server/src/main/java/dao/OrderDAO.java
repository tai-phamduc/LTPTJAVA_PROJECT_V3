package dao;

import entity.Customer;
import entity.Employee;
import entity.Order;
import entity.TrainJourney;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class OrderDAO {

	public int getTicketCountByOrderID(String orderID) {
		EntityManager em = HibernateUtil.getEntityManager();
		int ticketCount = 0;
		try {
			Query query = em.createNativeQuery("SELECT COUNT(*) AS TicketCount FROM Ticket WHERE OrderID = ?");
			query.setParameter(1, orderID);
			ticketCount = ((Number) query.getSingleResult()).intValue();
		} finally {
			em.close();
		}
		return ticketCount;
	}

	public Order getOrderByID(String orderID) {
		EntityManager em = HibernateUtil.getEntityManager();
		Order order = null;
		try {
			// Querying for the Order entity by orderID using TypedQuery for type safety
			TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.orderID = :orderID", Order.class);
			query.setParameter("orderID", orderID);
			order = query.getSingleResult();

			// Hibernate will automatically load the related Customer, TrainJourney, and Employee entities
			// because they are mapped as @ManyToOne relationships in the Order entity.

		} catch (NoResultException e) {
			// If no result is found, log or handle this scenario gracefully
			System.out.println("Order with ID " + orderID + " not found.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return order;
	}

	public String addOrder(LocalDate orderDate, String note, String orderStatus, Customer customer,
						   TrainJourney trainJourney, Employee employee) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			// Prepare the SQL query to insert a new Order record and return the generated OrderID
			Query query = em.createNativeQuery("""
    INSERT INTO [Order] 
        (OrderDate, Note, TimeRemaining, OrderStatus, CustomerID, TrainJourneyID, EmployeeID, TaxID)
    OUTPUT INSERTED.OrderID
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
""");


			// Set parameters for the query
			query.setParameter(1, LocalDateTime.of(orderDate, LocalTime.of(12, 0))); // Set order date with time
			query.setParameter(2, note);
			query.setParameter(3, LocalTime.of(12, 0)); // Set default time remaining
			query.setParameter(4, orderStatus);
			query.setParameter(5, customer.getCustomerID()); // Assuming customer has a getCustomerID() method
			query.setParameter(6, trainJourney.getTrainJourneyID()); // Assuming trainJourney has a getTrainJourneyID() method
			query.setParameter(7, employee.getEmployeeID()); // Assuming employee has a getEmployeeID() method
			query.setParameter(8, "Tax001"); // Example TaxID, replace as needed

			// Get the generated OrderID from OUTPUT
			String generatedOrderID = (String) query.getSingleResult();

			em.getTransaction().commit();
			return generatedOrderID; // Return the generated OrderID

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public int getNumberOfOrderWithService(int monthValue, int year) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			Query query = em.createNativeQuery("SELECT dbo.GetOrdersWithService(?, ?)");
			query.setParameter(1, monthValue);
			query.setParameter(2, year);
			return ((Number) query.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}

	public int getNumberOfOrderWithoutService(int monthValue, int year) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			Query query = em.createNativeQuery("SELECT dbo.GetOrdersWithoutService(?, ?)");
			query.setParameter(1, monthValue);
			query.setParameter(2, year);
			return ((Number) query.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}
}
