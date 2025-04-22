package dao;

import entity.Order;
import entity.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utils.HibernateUtil;

public class ServiceDetailDAO {

	public boolean themChiTietDichVu(Service service, Order order, int quantity) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Query query = em.createNativeQuery("""
			INSERT INTO ServiceDetail (ServiceID, OrderID, Quantity)
			VALUES (?, ?, ?)
		""");

			query.setParameter(1, service.getServiceID());
			query.setParameter(2, order.getOrderID());
			query.setParameter(3, quantity);

			query.executeUpdate();

			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}


}
