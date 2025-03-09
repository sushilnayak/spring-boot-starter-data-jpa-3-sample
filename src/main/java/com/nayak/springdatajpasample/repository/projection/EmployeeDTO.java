package com.nayak.springdatajpasample.repository.projection;

import lombok.Value;

@Value
public class EmployeeDTO {
    String fullName;
    String departmentName;
    String email;
}