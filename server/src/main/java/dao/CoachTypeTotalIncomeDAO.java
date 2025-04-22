package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import entity.CoachTypeTotalIncome;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utils.HibernateUtil;

public class CoachTypeTotalIncomeDAO {

	public List<CoachTypeTotalIncome> getCoachTypeTotalIncome(int monthValue, int year) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<CoachTypeTotalIncome> resultList = null;

		try {
			Query query = em.createNativeQuery(
					"SELECT * FROM dbo.GetTotalTicketIncomeByCoachType(?, ?)",
					"CoachTypeTotalIncomeMapping"
			);
			query.setParameter(1, monthValue);
			query.setParameter(2, year);

			@SuppressWarnings("unchecked")
			List<CoachTypeTotalIncome> results = query.getResultList();

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