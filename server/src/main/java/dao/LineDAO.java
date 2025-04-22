package dao;

import entity.Line;
import entity.LineDetails;
import entity.Station;
import entity.StationLine;
import entity.Stop;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LineDAO {

	public List<Line> getAllLine() {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Line> lineList = new ArrayList<>();
		try {
			TypedQuery<Line> query = em.createQuery("SELECT l FROM Line l", Line.class);
			lineList = query.getResultList();
		} finally {
			em.close();
		}
		return lineList;
	}

	public List<LineDetails> getAllLineDetailsByName(String lineNameToFind) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<LineDetails> result = new ArrayList<>();
		try {
			Query query = em.createNativeQuery("SELECT * FROM GetLineDetails() WHERE LineName LIKE ?", LineDetails.class);
			query.setParameter(1, "%" + lineNameToFind + "%");
			result = query.getResultList();
		} finally {
			em.close();
		}
		return result;
	}

	public List<Stop> getLineStops(String lineID) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Stop> stopList = new ArrayList<>();
		try {
			Query query = em.createNativeQuery("""
                SELECT stopOrder, s.stationID, s.stationName, distance
                FROM line l
                JOIN LineStop ls ON l.lineID = ls.lineID
                JOIN station s ON ls.stationID = s.stationID
                WHERE l.lineID = ?
            """);
			query.setParameter(1, lineID);
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				int stopOrder = (int) row[0];
				String stationID = (String) row[1];
				String stationName = (String) row[2];
				int distance = (int) row[3];
				stopList.add(new Stop(new Station(stationID, stationName), stopOrder, distance,
						LocalDate.now(), LocalTime.now(), LocalTime.now()));
			}
		} finally {
			em.close();
		}
		return stopList;
	}

	public List<LineDetails> getAllLineDetails() {
		EntityManager em = HibernateUtil.getEntityManager();
		List<LineDetails> lineDetailsList = new ArrayList<>();

		try {
			Query query = em.createNativeQuery("SELECT * FROM GetLineDetails()");
			List<Object[]> results = query.getResultList();

			for (Object[] row : results) {
				String lineID = (String) row[0];
				String lineName = (String) row[1];
				String departureStation = (String) row[2];
				String arrivalStation = (String) row[3];
				int totalDistance = ((Number) row[4]).intValue();

				lineDetailsList.add(new LineDetails(lineID, lineName, departureStation, arrivalStation, totalDistance));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return lineDetailsList;
	}


	public boolean removeLineByID(String lineID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("DELETE FROM Line l WHERE l.lineID = :lineID");
			query.setParameter("lineID", lineID);
			int rowsAffected = query.executeUpdate();
			em.getTransaction().commit();
			return rowsAffected > 0;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public String addLine(String lineName) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createNativeQuery("INSERT INTO Line (LineName) OUTPUT inserted.LineID VALUES (?)");
			query.setParameter(1, lineName);
			String generatedLineID = (String) query.getSingleResult();
			em.getTransaction().commit();
			return generatedLineID;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public boolean addLineStop(String lineID, StationLine stationLine) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createNativeQuery("""
                INSERT INTO LineStop (LineID, StationID, StopOrder, Distance) VALUES (?, ?, ?, ?)
            """);
			query.setParameter(1, lineID);
			query.setParameter(2, stationLine.getStation().getStationID());
			query.setParameter(3, stationLine.getIndex());
			query.setParameter(4, stationLine.getDistance());
			int rowsAffected = query.executeUpdate();
			em.getTransaction().commit();
			return rowsAffected > 0;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public Line getLineByID(String lineID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.find(Line.class, lineID);
		} finally {
			em.close();
		}
	}

	public List<StationLine> getLineStopByLineID(String lineID) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<StationLine> stationLineList = new ArrayList<>();
		try {
			Query query = em.createNativeQuery("SELECT * FROM LineStop WHERE LineID = ?");
			query.setParameter(1, lineID);
			List<Object[]> result = query.getResultList();
			StationDAO stationDAO = new StationDAO(); // Assuming it's been Hibernate-migrated
			for (Object[] row : result) {
				int stopOrder = (int) row[2]; // stopOrder
				String stationID = (String) row[1]; // stationID
				int distance = (int) row[3]; // distance
				stationLineList.add(new StationLine(stopOrder, stationDAO.getStationByID(stationID), distance));
			}
		} finally {
			em.close();
		}
		return stationLineList;
	}

	public boolean updateLine(Line line) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(line);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public boolean removeAllByLineID(String lineID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("DELETE FROM LineStop ls WHERE ls.line.lineID = :lineID");
			query.setParameter("lineID", lineID);
			int rows = query.executeUpdate();
			em.getTransaction().commit();
			return rows > 0;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}
}
