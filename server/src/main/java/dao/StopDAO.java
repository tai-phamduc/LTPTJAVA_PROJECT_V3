package dao;

import entity.Stop;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.HibernateUtil;

public class StopDAO {

	public int updateStop(Stop stop) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction transaction = null;

		try {
			transaction = em.getTransaction();
			transaction.begin();

			Stop existingStop = em.find(Stop.class, stop.getStopID());
			if (existingStop != null) {
				existingStop.setDepartureDate(stop.getDepartureDate());
				existingStop.setArrivalTime(stop.getArrivalTime());
				existingStop.setDepartureTime(stop.getDepartureTime());
				em.merge(existingStop);
				transaction.commit();
				return 1; // Success
			} else {
				return 0; // Stop not found
			}

		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
			return -1; // Error
		} finally {
			em.close();
		}
	}
}
