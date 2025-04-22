package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import entity.Employee;
import utils.HibernateUtil;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Query;

public class EmployeeDAO {
	private EntityManager em;

	public EmployeeDAO() {
		this.em = HibernateUtil.getEntityManager();
	}

	public boolean updateAvatar(String imagePath, String employeeID) {
		try {
			em.getTransaction().begin();
			Employee employee = em.find(Employee.class, employeeID);
			if (employee != null) {
				employee.setImageSource(imagePath);
				em.merge(employee);
				em.getTransaction().commit();
				System.out.println("Avatar updated successfully.");
				return true;
			}
			System.out.println("Failed to update avatar.");
			return false;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Error updating avatar: " + e.getMessage());
			return false;
		}
	}

	public List<Employee> getAllEmployees() {
		try {
			TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Employee> findEmployeByName(String nameToFind) {
		try {
			TypedQuery<Employee> query = em.createQuery(
					"SELECT e FROM Employee e WHERE e.fullName LIKE :name", Employee.class);
			query.setParameter("name", "%" + nameToFind + "%");
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean removeEmployeeByID(String employeeIDToDelete) {
		try {
			em.getTransaction().begin();
			Employee employee = em.find(Employee.class, employeeIDToDelete);
			if (employee != null) {
				em.remove(employee);
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

	public String addNewEmployee(Employee newEmployee) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			// Insert employee using native SQL, letting the DB generate the ID
			Query query = em.createNativeQuery("""
            INSERT INTO Employee 
                (FullName, Gender, DateOfBirth, Email, PhoneNumber, Role, StartingDate, ImageSource)
            OUTPUT INSERTED.EmployeeID
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """);

			query.setParameter(1, newEmployee.getFullName());
			query.setParameter(2, newEmployee.isGender());
			query.setParameter(3, newEmployee.getDateOfBirth());
			query.setParameter(4, newEmployee.getEmail());
			query.setParameter(5, newEmployee.getPhoneNumber());
			query.setParameter(6, newEmployee.getRole());
			query.setParameter(7, newEmployee.getStartingDate());
			query.setParameter(8, newEmployee.getImageSource());

			// Get the generated EmployeeID from OUTPUT
			String generatedEmployeeID = (String) query.getSingleResult();

			em.getTransaction().commit();
			return generatedEmployeeID;

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

	public Employee getEmployeeByID(String employeeIDToFind) {
		try {
			return em.find(Employee.class, employeeIDToFind);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean updateEmployee(Employee newEmployee) {
		try {
			em.getTransaction().begin();
			em.merge(newEmployee);
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
}