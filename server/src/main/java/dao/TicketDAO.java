package dao;

import entity.*;
import jakarta.persistence.*;
import utils.HibernateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

	public Ticket getTicketByID(String ticketID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.find(Ticket.class, ticketID);
		} finally {
			em.close();
		}
	}

	public String addTicket(TrainJourney trainJourney, Seat seat, Passenger passenger, Order order) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Query query = em.createNativeQuery("""
				INSERT INTO Ticket (TrainJourneyID, SeatID, PassengerID, OrderID)
				OUTPUT INSERTED.TicketID
				VALUES (?, ?, ?, ?)
			""");

			query.setParameter(1, trainJourney.getTrainJourneyID());
			query.setParameter(2, seat.getSeatID());
			query.setParameter(3, passenger.getPassengerID());
			query.setParameter(4, order.getOrderID());

			String generatedTicketID = (String) query.getSingleResult();

			em.getTransaction().commit();
			return generatedTicketID;
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public boolean updateTicketStatus(String status, String ticketID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("UPDATE Ticket t SET t.status = :status WHERE t.ticketID = :ticketID");
			query.setParameter("status", status);
			query.setParameter("ticketID", ticketID);
			int updated = query.executeUpdate();
			em.getTransaction().commit();
			return updated > 0;
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public boolean reassignTicketToNewOrder(String newOrderID, String ticketID) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("UPDATE Ticket t SET t.order.orderID = :newOrderID WHERE t.ticketID = :ticketID");
			query.setParameter("newOrderID", newOrderID);
			query.setParameter("ticketID", ticketID);
			int updated = query.executeUpdate();
			em.getTransaction().commit();
			return updated > 0;
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<TicketInfo> fetchEligibleRefundTicketsForOrder(String orderID, boolean isRefund) {
		EntityManager em = HibernateUtil.getEntityManager();
		List<TicketInfo> tickets = new ArrayList<>();

		try {
			Query query = em.createNativeQuery("EXEC GetAllTicketsByOrderID ?", Tuple.class);
			query.setParameter(1, orderID);
			List<Tuple> results = query.getResultList();

			for (Tuple row : results) {
				String ticketID = row.get("TicketID", String.class);
				String passengerID = row.get("PassengerID", String.class);
				LocalDateTime departureDateTime = row.get("DepartureDateTime", java.sql.Timestamp.class).toLocalDateTime();
				int seatID = row.get("SeatID", Integer.class);
				int coachID = row.get("CoachID", Integer.class);
				String status = row.get("OrderStatus", String.class);
				double distance = row.get("distance", Double.class);

				Ticket ticket = getTicketByID(ticketID);
				if ((isRefund && !ticket.getStatus().equalsIgnoreCase("Đã Trả")) ||
						(!isRefund && ticket.getStatus().equalsIgnoreCase("Bình thường"))) {

					TicketInfo info = new TicketInfo(
							ticket,
							new PassengerDAO().getPassengerByID(passengerID),
							departureDateTime,
							new SeatDAO().getSeatByID(seatID),
							new CoachDAO().getCoachByID(coachID),
							status,
							status,
							distance
					);
					tickets.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return tickets;
	}
}
