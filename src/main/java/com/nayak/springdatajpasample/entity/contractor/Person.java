package com.nayak.springdatajpasample.entity.contractor;


import com.nayak.springdatajpasample.entity.Address;
import com.nayak.springdatajpasample.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "person_type")
@Getter
@Setter
@NoArgsConstructor
public abstract class Person extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @Embedded
    private Address address;
}