package dao;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import entity.Coach;
import entity.Seat;
import utils.HibernateUtil;

public class SeatDAO {

	public int addSeat(Seat seat) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(seat);
			tx.commit();
			return seat.getSeatID(); // Since it's auto-generated, ID is set after persist
		} catch (Exception e) {
			e.printStackTrace();
			if (tx.isActive()) tx.rollback();
		} finally {
			em.close();
		}
		return -1;
	}

	public Seat getSeatByID(int seatID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.find(Seat.class, seatID);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return null;
	}

	public List<Seat> getSeats(Coach selectedCoach) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			TypedQuery<Seat> query = em.createQuery(
					"SELECT s FROM Seat s WHERE s.coach.coachID = :coachID", Seat.class);
			query.setParameter("coachID", selectedCoach.getCoachID());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return null;
	}
}
