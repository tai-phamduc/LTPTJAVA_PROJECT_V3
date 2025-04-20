package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;
import entity.Account;
import entity.Employee;
import utils.HibernateUtil;

public class AccountDAO {
	private EntityManager em;
	private EmployeeDAO employeeDAO;

	public AccountDAO() {
		this.em = HibernateUtil.getEntityManager();
		this.employeeDAO = new EmployeeDAO();
	}

	public boolean checkCredentials(String username, String password) {
		Account account = getAccountByUsername(username);
		if (account == null || !BCrypt.checkpw(password, account.getPassword())) {
			return false;
		}
		return true;
	}

	public Employee getEmployeeByAccount(String username, String password) {
		return getEmployeeByUsername(username, checkCredentials(username, password));
	}

	public Account getAccountByUsername(String username) {
		try {
			TypedQuery<Account> query = em.createQuery(
					"SELECT a FROM Account a WHERE a.username = :username", Account.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean createAccount(Account account) {
		try {
			em.getTransaction().begin();
			account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
			em.persist(account);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	public Employee getEmployeeByUsername(String username, boolean authentication) {
		if (!authentication || getAccountByUsername(username) == null) {
			return null;
		}

		try {
			TypedQuery<Employee> query = em.createQuery(
					"SELECT e FROM Employee e JOIN e.account a WHERE a.username = :username",
					Employee.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean updatePassword(String employeeID, String newPassword) {
		try {
			em.getTransaction().begin();
			Account account = em.createQuery(
							"SELECT a FROM Account a WHERE a.employee.employeeID = :employeeID", Account.class)
					.setParameter("employeeID", employeeID)
					.getSingleResult();

			account.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
			em.merge(account);
			em.getTransaction().commit();
			System.out.println("Password updated successfully.");
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Error updating password: " + e.getMessage());
			return false;
		}
	}

	public String getUserByEmployeeID(String employeeID) {
		try {
			Account account = em.createQuery(
							"SELECT a FROM Account a WHERE a.employee.employeeID = :employeeID", Account.class)
					.setParameter("employeeID", employeeID)
					.getSingleResult();
			return account.getUsername();
		} catch (NoResultException e) {
			System.out.println("Error fetching account information: " + e.getMessage());
			return null;
		}
	}

	public boolean checkAvalibility(String username) {
		return getAccountByUsername(username) == null;
	}

	public Account getAccountByEmployeeID(String employeeIDToFind) {
		try {
			Account account = em.createQuery(
							"SELECT a FROM Account a WHERE a.employee.employeeID = :employeeID", Account.class)
					.setParameter("employeeID", employeeIDToFind)
					.getSingleResult();

			return new Account(account.getUsername(), account.getPassword(),
					employeeDAO.getEmployeeByID(employeeIDToFind));
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean updateAccount(String employeeID, String username, String password) {
		try {
			em.getTransaction().begin();
			Account account = getAccountByEmployeeID(employeeID);
			if (account != null) {
				account.setUsername(username);
				account.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
				em.merge(account);
				em.getTransaction().commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}
}