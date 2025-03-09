package com.nayak.springdatajpasample.repository;

import com.nayak.springdatajpasample.entity.Employee;
import com.nayak.springdatajpasample.repository.custom.CustomEmployeeRepository;
import com.nayak.springdatajpasample.repository.projection.EmployeeNameOnly;
import com.nayak.springdatajpasample.repository.projection.EmployeeProjection;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@Lock(LockModeType.NONE)
public interface EmployeeRepository extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee>, CustomEmployeeRepository,
        RevisionRepository<Employee, Long, Integer> {

    // Basic queries
    Optional<Employee> findByEmail(String email);

    List<Employee> findByLastNameOrderByFirstNameAsc(String lastName);

    // Multiple conditions
    List<Employee> findByDepartment_CodeAndStatus(String departmentCode, Employee.EmploymentStatus status);

    // Using patterns
    List<Employee> findByEmailLike(String pattern);

    List<Employee> findByFirstNameStartingWith(String prefix);

    // Comparison operators
    List<Employee> findBySalaryGreaterThan(BigDecimal salary);

    List<Employee> findByDateOfBirthBetween(LocalDateTime start, LocalDateTime end);

    List<Employee> findAllByStatus(Employee.EmploymentStatus status);

    // Pagination and sorting
    Page<Employee> findBySalaryBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    // Projections
    <T> List<T> findByDepartmentCode(String departmentCode, Class<T> type);

    List<EmployeeNameOnly> findByStatus(Employee.EmploymentStatus status);

    List<EmployeeProjection> findByDepartmentCodeAndSalaryGreaterThan(String departmentCode, BigDecimal minSalary);

    // Custom JPQL queries
    @Query("SELECT e FROM Employee e WHERE e.salary = (SELECT MAX(emp.salary) FROM Employee emp)")
    Employee findEmployeeWithHighestSalary();

    @Query("SELECT e FROM Employee e " +
            "JOIN e.projects p " +
            "WHERE p.status = 'IN_PROGRESS' " +
            "GROUP BY e " +
            "HAVING COUNT(p) >= :minProjects")
    List<Employee> findEmployeesWorkingOnMultipleProjects(@Param("minProjects") long minProjects);

    // Native SQL query with projection
    @Query(value = "SELECT e.id, e.first_name, e.last_name, COUNT(p.id) as project_count " +
            "FROM employee e " +
            "LEFT JOIN employee_project ep ON e.id = ep.employee_id " +
            "LEFT JOIN project p ON ep.project_id = p.id " +
            "GROUP BY e.id",
            nativeQuery = true)
    List<Object[]> findEmployeeProjectCounts();

    // Locking examples
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    Optional<Employee> findEmployeeById(@Param("id") Long id);

//    @Lock(LockModeType.OPTIMISTIC)
//    Optional<Employee> findEmployeeById(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Employee> findEmployeeByDepartment_Code(String departmentCode);

    // Complex JOIN query with fetch joins
    @Query("SELECT DISTINCT e FROM Employee e " +
            "LEFT JOIN FETCH e.department " +
            "LEFT JOIN FETCH e.projects " +
            "LEFT JOIN FETCH e.employeeDetail " +
            "LEFT JOIN FETCH e.skills " +
            "LEFT JOIN FETCH e.documents " +
            "WHERE e.status = :status")
    List<Employee> findEmployeesWithAllDetails(@Param("status") Employee.EmploymentStatus status);

    // Stored procedure
    @Procedure(name = "Employee.giveRaise")
    void giveEmployeeRaise(@Param("emp_id") Long employeeId, @Param("raise_amount") BigDecimal raiseAmount);

    // Modifying queries
    @Modifying
    @Query("UPDATE Employee e SET e.salary = e.salary * :multiplier WHERE e.department.code = :departmentCode")
    int updateSalariesByDepartment(@Param("departmentCode") String departmentCode,
                                   @Param("multiplier") BigDecimal multiplier);
}