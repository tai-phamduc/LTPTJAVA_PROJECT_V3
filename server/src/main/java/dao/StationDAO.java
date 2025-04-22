package dao;

import entity.Station;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import utils.HibernateUtil;

import java.util.List;

public class StationDAO {

	public List<Station> getAllStation() {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Station> stationList = null;
		try {
			TypedQuery<Station> query = em.createQuery("FROM Station", Station.class);
			stationList = query.getResultList();
		} finally {
			em.close();
		}
		return stationList;
	}

	public String addNewStation(String stationName) {
		EntityManager em = HibernateUtil.getEntityManager();
		String generatedStationID = null;

		try {
			em.getTransaction().begin();

			// Insert using native SQL (letting DB generate StationID)
			Query query = em.createNativeQuery("""
            INSERT INTO Station (StationName) VALUES (?)
        """);
			query.setParameter(1, stationName);
			query.executeUpdate();

			// Optional: Fetch back the inserted station using the station name
			// Make sure stationName is unique or this will return the first match
			Station insertedStation = em.createQuery(
							"SELECT s FROM Station s WHERE s.stationName = :stationName ORDER BY s.stationID DESC",
							Station.class)
					.setParameter("stationName", stationName)
					.setMaxResults(1)
					.getSingleResult();

			em.getTransaction().commit();
			generatedStationID = insertedStation.getStationID();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}

		return generatedStationID;
	}


	public int deleteStationByID(String stationID) {
		EntityManager em = HibernateUtil.getEntityManager();
		int rowsAffected = 0;
		try {
			em.getTransaction().begin();
			Station station = em.find(Station.class, stationID);
			if (station != null) {
				em.remove(station);
				rowsAffected = 1;
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
			rowsAffected = -1;
		} finally {
			em.close();
		}
		return rowsAffected;
	}

	public List<Station> findStationByName(String stationName) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Station> stations = null;
		try {
			TypedQuery<Station> query = em.createQuery(
					"FROM Station WHERE stationName LIKE :name", Station.class);
			query.setParameter("name", "%" + stationName + "%");
			stations = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return stations;
	}

	public Station getStationByID(String stationID) {
		EntityManager em = HibernateUtil.getEntityManager();
		Station station = null;
		try {
			station = em.find(Station.class, stationID);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return station;
	}

	public boolean updateStation(Station station) {
		EntityManager em = HibernateUtil.getEntityManager();
		boolean success = false;
		try {
			em.getTransaction().begin();
			em.merge(station);
			em.getTransaction().commit();
			success = true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		return success;
	}

	public List<Station> getStationsForTicket(String ticketID) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Station> stations = null;
		try {
			StoredProcedureQuery query = em.createNamedStoredProcedureQuery("Station.getStationsForTicket");
			query.setParameter("ticketID", ticketID);
			stations = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return stations;
	}

}
