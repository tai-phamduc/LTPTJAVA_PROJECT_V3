package dao;

import entity.CustomerRanking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utils.HibernateUtil;

import java.util.List;

public class CustomerRankingDAO {

	public List<CustomerRanking> getTop10CustomerRanking(int month, int year) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<CustomerRanking> resultList;

		try {
			// Native query with the result mapping name
			Query query = em.createNativeQuery(
					"SELECT fullName AS customerName, phoneNumber, totalSpending FROM dbo.GetTop10CustomersBySpending(?, ?)",
					"CustomerRankingMapping" // Use the result mapping name defined in the CustomerRanking class
			);
			query.setParameter(1, month);
			query.setParameter(2, year);

			// This will return a List of CustomerRanking objects, not Object[]
			resultList = query.getResultList();
		} finally {
			if (em.isOpen()) em.close();
		}

		return resultList;
	}
}
