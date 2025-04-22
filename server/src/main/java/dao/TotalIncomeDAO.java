package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;

import entity.TotalIncome;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utils.HibernateUtil;

public class TotalIncomeDAO {

	public TotalIncome getTotalIncome(int month, int year) {
		EntityManager em = HibernateUtil.getEntityManager();
		TotalIncome totalIncome = null;

		try {
			Query query = em.createNativeQuery("SELECT * FROM dbo.GetTotalIncomeByMonthYear(?, ?)");

			query.setParameter(1, month);
			query.setParameter(2, year);

			Object[] result = (Object[]) query.getSingleResult();

			BigDecimal sumOfTicket = ((BigDecimal) result[0]).setScale(2, RoundingMode.HALF_UP);
			BigDecimal sumOfService = ((BigDecimal) result[1]).setScale(2, RoundingMode.HALF_UP);
			BigDecimal sumOfTicketAndService = ((BigDecimal) result[2]).setScale(2, RoundingMode.HALF_UP);

			totalIncome = new TotalIncome(sumOfTicket, sumOfService, sumOfTicketAndService);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return totalIncome;
	}

}
