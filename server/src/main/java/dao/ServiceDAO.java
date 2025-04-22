package dao;

import entity.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

	public List<Service> getServiceByType(String type) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			TypedQuery<Service> query = em.createQuery(
					"SELECT s FROM Service s WHERE s.type = :type", Service.class);
			query.setParameter("type", type);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public List<Service> findFoodByName(String serviceName) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			TypedQuery<Service> query = em.createQuery(
					"SELECT s FROM Service s WHERE s.serviceName LIKE :name AND s.type = 'Đồ ăn'", Service.class);
			query.setParameter("name", "%" + serviceName + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public List<Service> findDrinkByName(String serviceName) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			TypedQuery<Service> query = em.createQuery(
					"SELECT s FROM Service s WHERE s.serviceName LIKE :name AND s.type = 'Dồ uống'", Service.class);
			query.setParameter("name", "%" + serviceName + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public String addNewService(Service newService) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Query query = em.createNativeQuery("""
			INSERT INTO Service (ServiceName, Price, Type, ImageSource)
			OUTPUT INSERTED.ServiceID
			VALUES (?, ?, ?, ?)
		""");

			query.setParameter(1, newService.getServiceName());
			query.setParameter(2, newService.getPrice());
			query.setParameter(3, newService.getType());
			query.setParameter(4, newService.getImageSource());

			// Get generated ID from OUTPUT
			String generatedId = (String) query.getSingleResult();

			em.getTransaction().commit();
			return generatedId;

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


	public boolean removeServiceByID(String serviceID) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Service service = em.find(Service.class, serviceID);
			if (service != null) {
				em.remove(service);
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

	public boolean updateNewProduct(Service newService) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.merge(newService);
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

	public List<String> getAllServiceTypes() {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.createQuery(
					"SELECT DISTINCT s.type FROM Service s", String.class).getResultList();
		} finally {
			em.close();
		}
	}
}
