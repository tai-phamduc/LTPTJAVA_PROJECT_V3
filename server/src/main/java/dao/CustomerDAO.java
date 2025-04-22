package dao;

import entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import utils.HibernateUtil;

public class CustomerDAO {

	public Customer getCustomerByID(String customerID) {
		EntityManager em = HibernateUtil.getEntityManager();
		Customer customer = null;

		try {
			customer = em.find(Customer.class, customerID);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return customer;
	}

	public String addCustomer(Customer customer) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			// Use native SQL insert to let DB generate CustomerID and return it
			Query query = em.createNativeQuery("""
            INSERT INTO Customer (fullName, phoneNumber, email, identificationNumber)
            OUTPUT INSERTED.CustomerID
            VALUES (?, ?, ?, ?)
        """);

			query.setParameter(1, customer.getFullName());
			query.setParameter(2, customer.getPhoneNumber());
			query.setParameter(3, customer.getEmail());
			query.setParameter(4, customer.getIdentificationNumber());

			// Get the generated CustomerID from OUTPUT
			String generatedCustomerID = (String) query.getSingleResult();

			em.getTransaction().commit();
			return generatedCustomerID;

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

	public Customer getCustomerByEmail(String email) {
		EntityManager em = HibernateUtil.getEntityManager();
		Customer customer = null;

		try {
			customer = em.createQuery("""
					SELECT c FROM Customer c WHERE c.email = :email
				""", Customer.class)
					.setParameter("email", email)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			// Return null if no result
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return customer;
	}
}
