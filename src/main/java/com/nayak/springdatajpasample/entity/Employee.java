package com.nayak.springdatajpasample.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Audited
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "Employee.giveRaise",
                procedureName = "give_employee_raise",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "emp_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "raise_amount", type = BigDecimal.class),
                }
        )
})
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private LocalDateTime dateOfBirth;
    @Column(precision = 19, scale = 2)
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @NotAudited
    private Set<Project> projects = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "employee_skill",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @NotAudited
    private Set<Skill> skills = new HashSet<>();

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    private EmployeeDetail employeeDetail;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    private Set<EmployeeDocument> documents = new HashSet<>();


    public void addProject(Project project) {
        projects.add(project);
        project.getEmployees().add(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.getEmployees().remove(this);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.getEmployees().add(this);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
        skill.getEmployees().remove(this);
    }

    public void addDocument(EmployeeDocument document) {
        documents.add(document);
        document.setEmployee(this);
    }

    public void removeDocument(EmployeeDocument document) {
        documents.remove(document);
        document.setEmployee(null);
    }

    public enum EmploymentStatus {
        FULL_TIME, PART_TIME, CONTRACT, INTERN
    }
}
