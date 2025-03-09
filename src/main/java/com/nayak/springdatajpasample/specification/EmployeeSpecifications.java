package com.nayak.springdatajpasample.specification;

import com.nayak.springdatajpasample.entity.Employee;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeSpecifications {

    public static Specification<Employee> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("firstName"), firstName);
        };
    }

    public static Specification<Employee> hasLastName(String lastName) {
        return (root, query, cb) -> {
            if (lastName == null) {
                return null;
            }
            return cb.equal(root.get("lastName"), lastName);
        };
    }

    public static Specification<Employee> salaryGreaterThan(BigDecimal salary) {
        return (root, query, cb) -> {
            if (salary == null) {
                return null;
            }
            return cb.greaterThan(root.get("salary"), salary);
        };
    }

    public static Specification<Employee> inDepartment(String departmentCode) {
        return (root, query, cb) -> {
            if (departmentCode == null) {
                return null;
            }
            return cb.equal(root.join("department").get("code"), departmentCode);
        };
    }

    public static Specification<Employee> hasStatus(Employee.EmploymentStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Employee> dateOfBirthBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) {
                return null;
            }
            return cb.between(root.get("dateOfBirth"), startDate, endDate);
        };
    }

    public static Specification<Employee> workingOnProject(Long projectId) {
        return (root, query, cb) -> {
            if (projectId == null) {
                return null;
            }
            return cb.equal(root.join("projects").get("id"), projectId);
        };
    }

    public static Specification<Employee> hasEmailLike(String emailPattern) {
        return (root, query, cb) -> {
            if (emailPattern == null || emailPattern.isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("email")), "%" + emailPattern.toLowerCase() + "%");
        };
    }

    public static Specification<Employee> fullNameLike(String namePart) {
        return (root, query, cb) -> {
            if (namePart == null || namePart.isEmpty()) {
                return null;
            }
            // Concatenate firstName and lastName with a space in between
            Expression<String> fullName = cb.concat(root.get("firstName"), " ");
            fullName = cb.concat(fullName, root.get("lastName"));
            return cb.like(cb.lower(fullName), "%" + namePart.toLowerCase() + "%");
        };
    }

    // Or Expression
    public static Specification<Employee> hasEitherFirstNameOrLastName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            return cb.or(
                    cb.equal(root.get("firstName"), name),
                    cb.equal(root.get("lastName"), name)
            );
        };
    }

    // SELECT DISTINCT e.*
    //   FROM Employee e
    //  WHERE (
    //    SELECT COUNT(p.id)
    //    FROM Employee e2
    //    JOIN e2.projects p
    //    WHERE e2.id = e.id
    //  ) > projectCount
    public static Specification<Employee> hasProjectCountGreaterThan(long projectCount) {
        return (root, query, cb) -> {
            if (projectCount <= 0) {
                return null;
            }
            // Ensure distinct results when using joins
            query.distinct(true);

            // Create a subquery to count the projects
            Subquery<Long> subquery = query.subquery(Long.class);
            var subRoot = subquery.from(Employee.class);
            Join<Object, Object> projectsJoin = subRoot.join("projects");
            subquery.select(cb.count(projectsJoin.get("id")));
            subquery.where(cb.equal(subRoot.get("id"), root.get("id")));

            return cb.greaterThan(subquery, projectCount);
        };
    }

    public static Specification<Employee> ageGreaterThan(int age) {
        return (root, query, cb) -> {
            if (age <= 0) {
                return null;
            }
            // Calculate cutoff birth date: employees born before this date are older than the given age.
            LocalDate cutoffDate = LocalDate.now().minusYears(age);
            return cb.lessThan(root.get("dateOfBirth"), cutoffDate);
        };
    }
}
