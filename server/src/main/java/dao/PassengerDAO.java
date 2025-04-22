package dao;

import entity.Customer;
import entity.Passenger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityTransaction;
import utils.HibernateUtil;

import java.util.List;

public class PassengerDAO {

	public List<Passenger> getAllPassengers() {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			String query = "SELECT p FROM Passenger p";
			TypedQuery<Passenger> tq = em.createQuery(query, Passenger.class);
			return tq.getResultList();
		} finally {
			em.close();
		}
	}

	public List<Passenger> findPassengersByName(String nameToFind) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			String query = "SELECT p FROM Passenger p WHERE p.fullName LIKE :name";
			TypedQuery<Passenger> tq = em.createQuery(query, Passenger.class);
			tq.setParameter("name", "%" + nameToFind + "%");
			return tq.getResultList();
		} finally {
			em.close();
		}
	}

	public List<Passenger> findPassengersByIdentifier(String identifierToFind) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			String query = "SELECT p FROM Passenger p WHERE p.identifier LIKE :identifier";
			TypedQuery<Passenger> tq = em.createQuery(query, Passenger.class);
			tq.setParameter("identifier", "%" + identifierToFind + "%");
			return tq.getResultList();
		} finally {
			em.close();
		}
	}

	public boolean removePassengerByID(String passengerIDToDelete) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Passenger passenger = em.find(Passenger.class, passengerIDToDelete);
			if (passenger != null) {
				em.remove(passenger);
				tx.commit();
				return true;
			}
			tx.rollback();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx.isActive()) tx.rollback();
			return false;
		} finally {
			em.close();
		}
	}

	public Passenger getPassengerByID(String passengerIDToFind) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.find(Passenger.class, passengerIDToFind);
		} finally {
			em.close();
		}
	}

	public boolean updatePassenger(Passenger updatedPassenger) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.merge(updatedPassenger);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx.isActive()) tx.rollback();
			return false;
		} finally {
			em.close();
		}
	}

	public String addPassenger(Passenger passenger) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			// Insert using native SQL and let DB generate the PassengerID
			Query query = em.createNativeQuery("""
            INSERT INTO Passenger (FullName, Identifier, PassengerType, DateOfBirth, IdentifierType)
            OUTPUT INSERTED.PassengerID
            VALUES (?, ?, ?, ?, ?)
        """);

			// Set parameters for the query
			query.setParameter(1, passenger.getFullName());
			query.setParameter(2, passenger.getIdentifier());
			query.setParameter(3, passenger.getPassengerType());
			query.setParameter(4, passenger.getDateOfBirth());
			query.setParameter(5, passenger.getIdentifierType());

			// Get the generated PassengerID from OUTPUT
			String generatedPassengerID = (String) query.getSingleResult();

			em.getTransaction().commit();
			return generatedPassengerID; // Return the generated PassengerID

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

	public String updateCustomerByPhoneNumber(String phoneNumber, String fullName, String email) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			TypedQuery<Customer> query = em.createQuery(
					"SELECT c FROM Customer c WHERE c.phoneNumber = :phone", Customer.class);
			query.setParameter("phone", phoneNumber);
			List<Customer> customers = query.getResultList();

			if (!customers.isEmpty()) {
				Customer customer = customers.get(0);
				customer.setFullName(fullName);
				customer.setEmail(email);
				em.merge(customer);
				tx.commit();
				return customer.getCustomerID();
			} else {
				tx.rollback();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx.isActive()) tx.rollback();
			return null;
		} finally {
			em.close();
		}
	}

}
