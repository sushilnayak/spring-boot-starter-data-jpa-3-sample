package com.nayak.springdatajpasample.repository.custom;

import com.nayak.springdatajpasample.entity.Employee;
import com.nayak.springdatajpasample.repository.projection.EmployeeDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CustomEmployeeRepository {
    List<Employee> findByComplexCriteria(String departmentCode, BigDecimal minSalary, List<String> skills);

    List<EmployeeDTO> findEmployeesByDepartmentWithProjection(String departmentCode);

    void updateEmployeeSalariesByDepartment(String departmentCode, double percentage);

    List<Employee> findUsingStoredProcedure(String departmentCode);
}
