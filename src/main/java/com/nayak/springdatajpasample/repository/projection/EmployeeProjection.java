package com.nayak.springdatajpasample.repository.projection;
import org.springframework.beans.factory.annotation.Value;

public interface EmployeeProjection {
    String getFirstName();
    String getLastName();

    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();

    DepartmentInfo getDepartment();

    interface DepartmentInfo {
        String getName();
        String getCode();
    }
}