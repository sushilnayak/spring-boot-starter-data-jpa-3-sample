package com.nayak.springdatajpasample.repository.custom;

import com.nayak.springdatajpasample.entity.Employee;
import com.nayak.springdatajpasample.repository.projection.EmployeeDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> findByComplexCriteria(String departmentCode, BigDecimal minSalary, List<String> skills) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> employee = query.from(Employee.class);

        // Create joins
        Join<Object, Object> department = employee.join("department");
        Join<Object, Object> projects = employee.join("projects");

        // Create predicates
        Predicate departmentPredicate = cb.equal(department.get("code"), departmentCode);
        Predicate salaryPredicate = cb.greaterThan(employee.get("salary"), minSalary);

        // Combine predicates
        query.where(cb.and(departmentPredicate, salaryPredicate));
        query.distinct(true);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<EmployeeDTO> findEmployeesByDepartmentWithProjection(String departmentCode) {
        return entityManager.createQuery(
                        "SELECT new com.nayak.springdatajpasample.repository.projection.EmployeeDTO(" +
                                "CONCAT(e.firstName, ' ', e.lastName), " +
                                "d.name, " +
                                "e.email) " +
                                "FROM Employee e " +
                                "JOIN e.department d " +
                                "WHERE d.code = :code", EmployeeDTO.class)
                .setParameter("code", departmentCode)
                .getResultList();
    }

    @Override
    public void updateEmployeeSalariesByDepartment(String departmentCode, double percentage) {
        entityManager.createQuery(
                        "UPDATE Employee e " +
                                "SET e.salary = e.salary * :percentage " +
                                "WHERE e.department.code = :code")
                .setParameter("percentage", 1 + (percentage / 100))
                .setParameter("code", departmentCode)
                .executeUpdate();
    }

    @Override
    public List<Employee> findUsingStoredProcedure(String departmentCode) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("find_employees_by_department", Employee.class)
                .registerStoredProcedureParameter("p_department_code", String.class, ParameterMode.IN)
                .setParameter("p_department_code", departmentCode);

        return query.getResultList();
    }
}