package dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.HibernateUtil;
import entity.Train;
import entity.TrainDetails;

public class TrainDAO {

	public List<TrainDetails> getAllTrainDetails() {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainDetails> trainDetailsList = new ArrayList<>();

		try {
			Query query = em.createNativeQuery(
					"SELECT TrainID, TrainNumber, NumberOfCoaches, Capacity, " +
							"NumberOfCoachTypes, CoachTypes, Status FROM dbo.GetAllTrainDetails()");

			List<Object[]> results = query.getResultList();

			for (Object[] row : results) {
				String trainID = (String) row[0];
				String trainNumber = (String) row[1];
				int numberOfCoaches = (int) row[2];
				int capacity = (int) row[3];
				int numberOfCoachTypes = (int) row[4];
				String coachTypes = (String) row[5];
				String status = (String) row[6];

				trainDetailsList.add(new TrainDetails(
						trainID, trainNumber, numberOfCoaches, capacity,
						numberOfCoachTypes, coachTypes, status
				));
			}
		} finally {
			em.close();
		}
		return trainDetailsList;
	}

	public String addNewTrain(Train train) {
		EntityManager em = HibernateUtil.getEntityManager();
		jakarta.persistence.EntityTransaction tx = null;
		String generatedTrainID = null;

		try {
			tx = em.getTransaction();
			tx.begin();

			em.persist(train);

			tx.commit();
			generatedTrainID = train.getTrainID();
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
		return generatedTrainID;
	}

	public int deleteTrainByID(String trainID) {
		EntityManager em = HibernateUtil.getEntityManager();
		jakarta.persistence.EntityTransaction tx = null;
		int rowsAffected = 0;

		try {
			tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery("DELETE FROM Train t WHERE t.trainID = :trainID");
			query.setParameter("trainID", trainID);
			rowsAffected = query.executeUpdate();

			tx.commit();
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
			return -1;
		} finally {
			em.close();
		}
		return rowsAffected;
	}

	public Train getTrainByID(String trainID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.find(Train.class, trainID);
		} finally {
			em.close();
		}
	}

	public int getNumberOfCoaches(Train train) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			Query query = em.createQuery(
					"SELECT COUNT(c.coachID) FROM Coach c WHERE c.train.trainID = :trainID");
			query.setParameter("trainID", train.getTrainID());

			Long count = (Long) query.getSingleResult();
			return count != null ? count.intValue() : 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			em.close();
		}
	}

	public boolean updateTrain(Train train) {
		EntityManager em = HibernateUtil.getEntityManager();
		jakarta.persistence.EntityTransaction tx = null;

		try {
			tx = em.getTransaction();
			tx.begin();

			em.merge(train);

			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public List<TrainDetails> getTrainDetailsByTrainNumber(String trainNumberToFind) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainDetails> trainDetailsList = new ArrayList<>();

		try {
			Query query = em.createNativeQuery(
					"SELECT TrainID, TrainNumber, NumberOfCoaches, Capacity, " +
							"NumberOfCoachTypes, CoachTypes, Status FROM dbo.GetAllTrainDetails() " +
							"WHERE TrainNumber LIKE ?1");
			query.setParameter(1, "%" + trainNumberToFind + "%");

			List<Object[]> results = query.getResultList();

			for (Object[] row : results) {
				String trainID = (String) row[0];
				String trainNumber = (String) row[1];
				int numberOfCoaches = (int) row[2];
				int capacity = (int) row[3];
				int numberOfCoachTypes = (int) row[4];
				String coachTypes = (String) row[5];
				String status = (String) row[6];

				trainDetailsList.add(new TrainDetails(
						trainID, trainNumber, numberOfCoaches, capacity,
						numberOfCoachTypes, coachTypes, status
				));
			}
		} finally {
			em.close();
		}
		return trainDetailsList;
	}
	public List<Train> getAllTrain() {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			TypedQuery<Train> query = em.createQuery(
					"SELECT t FROM Train t", Train.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
}