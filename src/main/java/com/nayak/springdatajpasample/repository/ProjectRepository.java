package com.nayak.springdatajpasample.repository;

import com.nayak.springdatajpasample.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Basic queries
    List<Project> findByStatus(Project.ProjectStatus status);
    List<Project> findByEndDateAfter(LocalDate date);

    // Complex queries
    @Query("SELECT p FROM Project p " +
            "WHERE p.status = 'IN_PROGRESS' " +
            "AND SIZE(p.employees) >= :minTeamSize")
    List<Project> findActiveProjectsWithLargeTeams(@Param("minTeamSize") int minTeamSize);

    // Subquery example
    @Query("SELECT p FROM Project p " +
            "WHERE SIZE(p.employees) = (" +
            "    SELECT MAX(SIZE(p2.employees)) " +
            "    FROM Project p2)")
    List<Project> findProjectsWithLargestTeam();

    // Native query with joins
    @Query(value = "SELECT DISTINCT p.* FROM project p " +
            "JOIN employee_project ep ON p.id = ep.project_id " +
            "JOIN employee e ON ep.employee_id = e.id " +
            "JOIN department d ON e.department_id = d.id " +
            "WHERE d.code = :departmentCode " +
            "AND p.status = 'IN_PROGRESS'",
            nativeQuery = true)
    List<Project> findActiveProjectsByDepartment(@Param("departmentCode") String departmentCode);
}