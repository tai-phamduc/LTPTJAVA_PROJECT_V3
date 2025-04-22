package dao;

import java.util.ArrayList;
import java.util.List;

import entity.TrainJourneyRanking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utils.HibernateUtil;

public class TrainJourneyRankingDAO {

	public List<TrainJourneyRanking> getTop10TrainJourneyRanking(int month, int year) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainJourneyRanking> resultList = null;

		try {
			Query query = em.createNativeQuery(
					"SELECT * FROM dbo.GetTop10TrainJourneysByRevenue(?, ?)",
					"TrainJourneyRankingMapping"
			);
			query.setParameter(1, month);
			query.setParameter(2, year);

			@SuppressWarnings("unchecked")
			List<TrainJourneyRanking> results = query.getResultList();
			resultList = results;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}

		return resultList;
	}

}
