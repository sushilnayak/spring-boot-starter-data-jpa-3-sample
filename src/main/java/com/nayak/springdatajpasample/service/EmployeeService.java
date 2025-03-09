package com.nayak.springdatajpasample.service;

import com.nayak.springdatajpasample.entity.Employee;
import com.nayak.springdatajpasample.repository.EmployeeRepository;
import com.nayak.springdatajpasample.specification.EmployeeSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Basic CRUD operations
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    // Advanced query methods
    public List<Employee> findByDepartmentAndStatus(String departmentCode, Employee.EmploymentStatus status) {
        return employeeRepository.findByDepartment_CodeAndStatus(departmentCode, status);
    }

    public Page<Employee> findBySalaryRange(BigDecimal min, BigDecimal max, Pageable pageable) {
        return employeeRepository.findBySalaryBetween(min, max, pageable);
    }

    public Employee findEmployeeWithHighestSalary() {
        return employeeRepository.findEmployeeWithHighestSalary();
    }

    // Specification-based queries
    public List<Employee> findEmployeesBySpecification(
            String firstName,
            String lastName,
            BigDecimal minSalary,
            String departmentCode,
            Employee.EmploymentStatus status,
            LocalDate birthDateStart,
            LocalDate birthDateEnd,
            Long projectId) {

        Specification<Employee> spec = Specification.where(null);

        if (firstName != null) {
            spec = spec.and(EmployeeSpecifications.hasFirstName(firstName));
        }
        if (lastName != null) {
            spec = spec.and(EmployeeSpecifications.hasLastName(lastName));
        }
        if (minSalary != null) {
            spec = spec.and(EmployeeSpecifications.salaryGreaterThan(minSalary));
        }
        if (departmentCode != null) {
            spec = spec.and(EmployeeSpecifications.inDepartment(departmentCode));
        }
        if (status != null) {
            spec = spec.and(EmployeeSpecifications.hasStatus(status));
        }
        if (birthDateStart != null && birthDateEnd != null) {
            spec = spec.and(EmployeeSpecifications.dateOfBirthBetween(birthDateStart, birthDateEnd));
        }
        if (projectId != null) {
            spec = spec.and(EmployeeSpecifications.workingOnProject(projectId));
        }

        return employeeRepository.findAll(spec);
    }


    // Complex business operations
    @Transactional
    public void transferEmployee(Long employeeId, Long newDepartmentId) {
        Employee employee = employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Additional business logic for transfer
        // ...
    }

    public List<Employee> findEmployeesOnMultipleProjects(long minProjects) {
        return employeeRepository.findEmployeesWorkingOnMultipleProjects(minProjects);
    }

    public List<Employee> findEmployeesWithFullDetails(Employee.EmploymentStatus status) {
        return employeeRepository.findAllByStatus(status);
    }
}