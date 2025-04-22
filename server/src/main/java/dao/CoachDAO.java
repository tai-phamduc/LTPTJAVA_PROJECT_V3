package dao;

import entity.Coach;
import entity.Train;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class CoachDAO {

	public int addCoach(Coach coach) {
		EntityManager em = HibernateUtil.getEntityManager();
		int generatedID = -1;

		try {
			em.getTransaction().begin();
			em.persist(coach);
			em.getTransaction().commit();
			generatedID = coach.getCoachID(); // Assuming CoachID is auto-generated and set after persist
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}

		return generatedID;
	}

	public List<Coach> getCoaches(Train train) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Coach> coachList = new ArrayList<>();

		try {
			Query query = em.createQuery("SELECT c FROM Coach c WHERE c.train.trainID = :trainID", Coach.class);
			query.setParameter("trainID", train.getTrainID());
			coachList = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return coachList;
	}

	public boolean removeCoaches(Train train) {
		EntityManager em = HibernateUtil.getEntityManager();
		boolean success = false;

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("DELETE FROM Coach c WHERE c.train.trainID = :trainID");
			query.setParameter("trainID", train.getTrainID());
			int status = query.executeUpdate();
			em.getTransaction().commit();
			success = status > 0;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}

		return success;
	}

	public Coach getCoachByID(int coachID) {
		EntityManager em = HibernateUtil.getEntityManager();
		Coach coach = null;

		try {
			coach = em.find(Coach.class, coachID);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return coach;
	}
}
