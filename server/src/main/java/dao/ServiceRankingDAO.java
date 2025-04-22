package dao;

import java.util.List;

import entity.ServiceRanking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utils.HibernateUtil;

public class ServiceRankingDAO {

	public List<ServiceRanking> getTop10ServiceRanking(int month, int year) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<ServiceRanking> resultList = null;

		try {
			Query query = em.createNativeQuery(
					"SELECT serviceName, salesQuantity, revenue FROM dbo.GetTopServicesByIncome(?, ?)",
					"ServiceRankingMapping"
			);
			query.setParameter(1, month);
			query.setParameter(2, year);

			resultList = query.getResultList();
		} finally {
			if (em.isOpen()) em.close();
		}

		return resultList;
	}
}
