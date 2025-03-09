package com.nayak.springdatajpasample.entity.inheritance;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCardPayment extends Payment {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
}