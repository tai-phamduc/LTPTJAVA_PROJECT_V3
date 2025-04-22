package dao;

import entity.Train;
import entity.TrainDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import jakarta.persistence.EntityTransaction;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class TrainDAO {

	public List<TrainDetails> getAllTrainDetails() {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainDetails> list = new ArrayList<>();
		try {
			// Use the named SQL result set mapping for TrainDetails
			Query query = em.createNativeQuery(
					"SELECT TrainID, TrainNumber, NumberOfCoaches, Capacity, NumberOfCoachTypes, CoachTypes, Status FROM dbo.GetAllTrainDetails()",
					"TrainDetailsMapping"
			);

			// Get the result list, which will be automatically mapped to TrainDetails
			list = query.getResultList();
		} finally {
			em.close();
		}
		return list;
	}

	public String addNewTrain(Train train) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Query query = em.createNativeQuery("""
			INSERT INTO Train (TrainNumber, Status)
			OUTPUT INSERTED.TrainID
			VALUES (?, ?)
		""");

			query.setParameter(1, train.getTrainNumber());
			query.setParameter(2, train.getStatus());

			String generatedTrainID = (String) query.getSingleResult();

			em.getTransaction().commit();
			return generatedTrainID;
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


	public int deleteTrainByID(String trainID) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Train train = em.find(Train.class, trainID);
			if (train != null) {
				em.remove(train);
				tx.commit();
				return 1;
			}
			return 0;
		} catch (Exception e) {
			if (tx.isActive()) tx.rollback();
			e.printStackTrace();
			return -1;
		} finally {
			em.close();
		}
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
			Query query = em.createNativeQuery(
					"SELECT COUNT(c.CoachID) AS NumberOfCoaches FROM Train t LEFT JOIN Coach c ON t.TrainID = c.TrainID WHERE t.TrainID = ? GROUP BY t.TrainID, t.TrainNumber"
			);
			query.setParameter(1, train.getTrainID());
			Object result = query.getSingleResult();
			return ((Number) result).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			em.close();
		}
	}

	public boolean updateTrain(Train train) {
		EntityManager em = HibernateUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.merge(train);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx.isActive()) tx.rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public List<TrainDetails> getTrainDetailsByTrainNumber(String trainNumberToFind) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainDetails> list = new ArrayList<>();
		try {
			Query query = em.createNativeQuery(
					"SELECT TrainID, TrainNumber, NumberOfCoaches, Capacity, NumberOfCoachTypes, CoachTypes, Status " +
							"FROM dbo.GetAllTrainDetails() WHERE TrainNumber LIKE ?",
					"TrainDetailsMapping" // Use the mapping defined above
			);
			query.setParameter(1, "%" + trainNumberToFind + "%");  // Set parameter by index (1)

			// Get the result list, which will be automatically mapped to TrainDetails
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return list;
	}


	public List<Train> getAllTrain() {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			TypedQuery<Train> query = em.createQuery("SELECT t FROM Train t", Train.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
}
