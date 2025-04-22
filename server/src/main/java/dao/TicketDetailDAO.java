package dao;

import entity.Stop;
import entity.Ticket;
import entity.TicketDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.HibernateUtil;

public class TicketDetailDAO {

	public void themChiTietVe(Stop stop, Ticket ticket) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = null;

		try {
			tx = em.getTransaction();
			tx.begin();

			TicketDetail detail = new TicketDetail(stop, ticket);
			em.persist(detail);

			tx.commit();
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
}
