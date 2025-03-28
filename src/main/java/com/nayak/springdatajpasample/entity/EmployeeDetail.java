package com.nayak.springdatajpasample.entity;

import com.nayak.springdatajpasample.entity.embedded.Benefit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String phoneNumber;

    private String emergencyContact;

    private String bloodGroup;

    @Embedded
    private Benefit benefits;

    @OneToOne(fetch = FetchType.LAZY)
    private Employee employee;
}