package com.nayak.springdatajpasample.repository;

import com.nayak.springdatajpasample.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Basic queries
    Optional<Department> findByCode(String code);
    List<Department> findByNameContainingIgnoreCase(String name);

    // Custom JPQL query
    @Query("SELECT d FROM Department d WHERE SIZE(d.employees) > :minEmployees")
    List<Department> findDepartmentsWithMoreEmployeesThan(int minEmployees);

    // Native SQL query
    @Query(value = "SELECT d.* FROM department d " +
            "JOIN employee e ON d.id = e.department_id " +
            "GROUP BY d.id " +
            "HAVING AVG(e.salary) > :minAvgSalary",
            nativeQuery = true)
    List<Department> findDepartmentsWithAvgSalaryGreaterThan(double minAvgSalary);
}