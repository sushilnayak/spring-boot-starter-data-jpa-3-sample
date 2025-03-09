package com.nayak.springdatajpasample.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
public class Benefit {
    private String healthInsurancePlan;
    private BigDecimal insuranceCoverage;
    private Integer paidTimeOff;
    private Boolean hasStockOptions;
    private String pensionPlan;
}