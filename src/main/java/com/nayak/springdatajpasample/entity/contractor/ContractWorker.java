package com.nayak.springdatajpasample.entity.contractor;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CONTRACTOR")
@PrimaryKeyJoinColumn(name = "contractor_id")
@Getter
@Setter
@NoArgsConstructor
public class ContractWorker extends Person {

    private String contractNumber;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private BigDecimal hourlyRate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Agency agency;
}