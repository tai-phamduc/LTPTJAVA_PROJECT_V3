package dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import utils.HibernateUtil;
import entity.Line;
import entity.Seat;
import entity.Station;
import entity.Stop;
import entity.Train;
import entity.TrainJourney;
import entity.TrainJourneyDetails;
import entity.TrainJourneyOptionItem;

public class TrainJourneyDAO {

	public List<TrainJourneyDetails> getAllTrainJourneyDetails() {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainJourneyDetails> trainJourneyDetailsList = new ArrayList<>();

		try {
			Query query = em.createNativeQuery(
					"SELECT trainJourneyID, trainJourneyName, trainNumber, departureStation, arrivalStation, " +
							"departureDate, departureTime, arrivalTime, totalDistance, bookedTickets, totalSeats " +
							"FROM dbo.fn_GetAllTrainJourneyDetails()", Object[].class);

			List<Object[]> results = query.getResultList();

			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

			for (Object[] row : results) {
				String trainJourneyID = (String) row[0];
				String trainJourneyName = (String) row[1];
				String trainNumber = (String) row[2];
				String departureStation = (String) row[3];
				String arrivalStation = (String) row[4];

				LocalDate departureDate = row[5] != null
						? ((java.sql.Date) row[5]).toLocalDate()
						: LocalDate.now();

				LocalTime departureTime = row[6] != null
						? ((java.sql.Time) row[6]).toLocalTime()
						: LocalTime.now();

				LocalTime arrivalTime = row[7] != null
						? ((java.sql.Time) row[7]).toLocalTime()
						: LocalTime.now();

				// Check if totalDistance is null before calling intValue
				int totalDistance = row[8] != null
						? ((Number) row[8]).intValue()
						: 0;  // Use a default value if it's null

				// Check if bookedTickets is null before calling intValue
				int bookedTickets = row[9] != null
						? ((Number) row[9]).intValue()
						: 0;  // Use a default value if it's null

				// Check if totalSeats is null before calling intValue
				int totalSeats = row[10] != null
						? ((Number) row[10]).intValue()
						: 0;  // Use a default value if it's null

				trainJourneyDetailsList.add(new TrainJourneyDetails(
						trainJourneyID,
						trainNumber,
						trainJourneyName,
						departureStation + " - " + arrivalStation,
						departureDate.format(dateFormatter),
						departureTime.format(timeFormatter) + " - " + arrivalTime.format(timeFormatter),
						totalDistance,
						bookedTickets + "/" + totalSeats
				));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}

		return trainJourneyDetailsList;
	}

	public boolean addStops(List<Stop> stopList) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			for (Stop stop : stopList) {
				Query query = em.createNativeQuery(
						"INSERT INTO Stop (trainJourneyID, stationID, stopOrder, distance, departureDate, arrivalTime, departureTime) " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)");

				query.setParameter(1, stop.getTrainJourney().getTrainJourneyID());
				query.setParameter(2, stop.getStation().getStationID());
				query.setParameter(3, stop.getStopOrder());
				query.setParameter(4, stop.getDistance());
				query.setParameter(5, stop.getDepartureDate());
				query.setParameter(6, stop.getArrivalTime());
				query.setParameter(7, stop.getDepartureTime());

				query.executeUpdate();
			}

			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public int deleteTrainJourneyByID(String trainJourneyID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			// Delete related stops first
			Query deleteStopsQuery = em.createNativeQuery("DELETE FROM Stop WHERE trainJourneyID = ?");
			deleteStopsQuery.setParameter(1, trainJourneyID);
			int stopsDeleted = deleteStopsQuery.executeUpdate();

			// Then delete the train journey
			Query deleteJourneyQuery = em.createNativeQuery("DELETE FROM TrainJourney WHERE trainJourneyID = ?");
			deleteJourneyQuery.setParameter(1, trainJourneyID);
			int journeyDeleted = deleteJourneyQuery.executeUpdate();

			em.getTransaction().commit();
			return stopsDeleted + journeyDeleted;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return 0;
		} finally {
			em.close();
		}
	}

	public TrainJourney getTrainJourneyByID(String trainJourneyID) {
		EntityManager em = HibernateUtil.getEntityManager();
		TrainJourney trainJourney = null;

		try {
			TypedQuery<TrainJourney> query = em.createQuery(
					"SELECT tj FROM TrainJourney tj " +
							"JOIN FETCH tj.train t " +
							"JOIN FETCH tj.line l " +
							"WHERE tj.trainJourneyID = :id",
					TrainJourney.class
			);
			query.setParameter("id", trainJourneyID);
			trainJourney = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace(); // optionally log this
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return trainJourney;
	}

	public List<Stop> getAllStops(TrainJourney trainJourney) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Stop> stopList = new ArrayList<>();
		try {
			Query query = em.createNativeQuery(
					"SELECT s.stopID, s.stationID, st.stationName, s.stopOrder, s.distance, " +
							"s.departureDate, s.arrivalTime, s.departureTime " +
							"FROM Stop s JOIN Station st ON s.stationID = st.stationID " +
							"WHERE s.trainJourneyID = ?", Object[].class);

			query.setParameter(1, trainJourney.getTrainJourneyID());
			List<Object[]> results = query.getResultList();

			for (Object[] row : results) {
				String stopID = (String) row[0];
				String stationID = (String) row[1];
				String stationName = (String) row[2];
				int stopOrder = (int) row[3];
				int distance = (int) row[4];
				LocalDate departureDate = ((java.sql.Date) row[5]).toLocalDate();
				LocalTime arrivalTime = ((java.sql.Time) row[6]).toLocalTime();
				LocalTime departureTime = ((java.sql.Time) row[7]).toLocalTime();

				stopList.add(new Stop(
						stopID, trainJourney, new Station(stationID, stationName),
						stopOrder, distance, departureDate, arrivalTime, departureTime
				));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return stopList;
	}

	public int updateTrainJourney(TrainJourney trainJourney) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Query query = em.createNativeQuery(
					"UPDATE TrainJourney SET trainJourneyName = ?, basePrice = ? WHERE trainJourneyID = ?");

			query.setParameter(1, trainJourney.getTrainJourneyName());
			query.setParameter(2, trainJourney.getBasePrice());
			query.setParameter(3, trainJourney.getTrainJourneyID());

			int result = query.executeUpdate();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return -1;
		} finally {
			em.close();
		}
	}

	public List<TrainJourneyDetails> getAllTrainJourneyDetailsByTrainNumber(String trainNumberToFind) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainJourneyDetails> trainJourneyDetailsList = new ArrayList<>();

		try {
			Query query = em.createNativeQuery(
					"SELECT trainJourneyID, trainJourneyName, trainNumber, departureStation, arrivalStation, " +
							"departureDate, departureTime, arrivalTime, totalDistance, bookedTickets, totalSeats " +
							"FROM dbo.fn_GetAllTrainJourneyDetails() WHERE trainNumber LIKE ?", Object[].class);

			query.setParameter(1, "%" + trainNumberToFind + "%");
			List<Object[]> results = query.getResultList();

			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

			for (Object[] row : results) {
				String trainJourneyID = (String) row[0];
				String trainJourneyName = (String) row[1];
				String trainNumber = (String) row[2];
				String departureStation = (String) row[3];
				String arrivalStation = (String) row[4];

				LocalDate departureDate = row[5] != null
						? ((java.sql.Date) row[5]).toLocalDate()
						: LocalDate.now();

				LocalTime departureTime = row[6] != null
						? ((java.sql.Time) row[6]).toLocalTime()
						: LocalTime.now();

				LocalTime arrivalTime = row[7] != null
						? ((java.sql.Time) row[7]).toLocalTime()
						: LocalTime.now();

				int totalDistance = row[8] != null
						? ((Number) row[8]).intValue()
						: 0;

				int bookedTickets = row[9] != null
						? ((Number) row[9]).intValue()
						: 0;

				int totalSeats = row[10] != null
						? ((Number) row[10]).intValue()
						: 0;

				trainJourneyDetailsList.add(new TrainJourneyDetails(
						trainJourneyID,
						trainNumber,
						trainJourneyName,
						departureStation + " - " + arrivalStation,
						departureDate.format(dateFormatter),
						departureTime.format(timeFormatter) + " - " + arrivalTime.format(timeFormatter),
						totalDistance,
						bookedTickets + "/" + totalSeats
				));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}

		return trainJourneyDetailsList;
	}

	public String addTrainJourney(TrainJourney trainJourney) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Query query = em.createNativeQuery(
					"INSERT INTO TrainJourney (trainJourneyName, trainID, lineID, basePrice) " +
							"OUTPUT inserted.trainJourneyID VALUES (?, ?, ?, ?)");

			query.setParameter(1, trainJourney.getTrainJourneyName());
			query.setParameter(2, trainJourney.getTrain().getTrainID());
			query.setParameter(3, trainJourney.getLine().getLineID());
			query.setParameter(4, trainJourney.getBasePrice());

			String generatedID = (String) query.getSingleResult();
			em.getTransaction().commit();
			return generatedID;
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

	public int getDistanceBetweenTwoStopsOfATrainJourney(String trainJourneyID, Station departureStation, Station arrivalStation) {
		EntityManager em = HibernateUtil.getEntityManager();
		int distance = -1;

		try {
			Query query = em.createNativeQuery("SELECT dbo.GetDistanceBetweenStops(?, ?, ?) AS distance");
			query.setParameter(1, trainJourneyID);
			query.setParameter(2, departureStation.getStationID());
			query.setParameter(3, arrivalStation.getStationID());

			Object result = query.getSingleResult();
			if (result != null) {
				distance = ((Number) result).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace(); // or use a logger
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return distance;
	}

	public List<TrainJourneyOptionItem> searchTrainJourney(String gaDi, String gaDen, LocalDate ngayDi) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TrainJourneyOptionItem> resultList = new ArrayList<>();

		try {
			Query query = em.createNativeQuery(
					"SELECT * FROM dbo.GetTrainJourneysByStationNames(?, ?, ?)"
			);
			query.setParameter(1, gaDi);
			query.setParameter(2, gaDen);
			query.setParameter(3, ngayDi);

			List<Object[]> results = query.getResultList();
			for (Object[] row : results) {
				String trainJourneyID = (String) row[0];
				String trainID = (String) row[2];
				String trainNumber = (String) row[3];

				Train train = new Train();
				train.setTrainID(trainID);
				train.setTrainNumber(trainNumber);

				String departureStationID = (String) row[4];
				String departureStationName = (String) row[5];
				LocalDate departureDate = ((java.sql.Date) row[6]).toLocalDate();
				LocalTime departureTime = ((java.sql.Time) row[7]).toLocalTime();

				String arrivalStationID = (String) row[8];
				String arrivalStationName = (String) row[9];
				LocalDate arrivalDate = ((java.sql.Date) row[10]).toLocalDate();
				LocalTime arrivalTime = ((java.sql.Time) row[11]).toLocalTime();

				int journeyDuration = ((Number) row[12]).intValue();
				int numberOfAvailableSeatsLeft = ((Number) row[13]).intValue();

				Station departureStation = new Station(departureStationID, departureStationName);
				Station arrivalStation = new Station(arrivalStationID, arrivalStationName);

				TrainJourneyOptionItem item = new TrainJourneyOptionItem(
						trainJourneyID,
						train,
						numberOfAvailableSeatsLeft,
						departureDate,
						departureTime,
						arrivalDate,
						arrivalTime,
						journeyDuration,
						departureStation,
						arrivalStation
				);

				resultList.add(item);
			}
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return resultList;
	}

	public List<Seat> getUnavailableSeats(String trainJourneyID, Station departureStation, Station arrivalStation) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Seat> seats = new ArrayList<>();
		try {
			Query query = em.createNativeQuery(
					"SELECT SeatID, SeatNumber FROM dbo.fn_GetUnavailableSeats(?, ?, ?)", Object[].class);

			query.setParameter(1, trainJourneyID);
			query.setParameter(2, departureStation.getStationID());
			query.setParameter(3, arrivalStation.getStationID());

			List<Object[]> results = query.getResultList();
			for (Object[] row : results) {
				int seatID = (int) row[0];
				int seatNumber = (int) row[1];
				seats.add(new Seat(seatID, seatNumber));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return seats;
	}

	public List<Stop> getStops(TrainJourney trainJourney, Station departureStation, Station arrivalStation) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Stop> stops = new ArrayList<>();
		try {
			Query query = em.createNativeQuery(
					"SELECT stopid FROM dbo.GetStopsForJourney(?, ?, ?)", Object[].class);

			query.setParameter(1, trainJourney.getTrainJourneyID());
			query.setParameter(2, departureStation.getStationID());
			query.setParameter(3, arrivalStation.getStationID());

			List<Object[]> results = query.getResultList();
			for (Object[] row : results) {
				String stopID = (String) row[0];
				stops.add(new Stop(stopID));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return stops;
	}
}