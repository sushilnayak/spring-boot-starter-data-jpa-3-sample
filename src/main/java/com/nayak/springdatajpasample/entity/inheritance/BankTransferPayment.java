package com.nayak.springdatajpasample.entity.inheritance;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BankTransferPayment extends Payment {

    private String bankName;
    private String accountNumber;
    private String routingNumber;
    private String transferReference;
}