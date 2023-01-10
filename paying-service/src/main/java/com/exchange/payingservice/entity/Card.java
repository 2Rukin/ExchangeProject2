package com.exchange.payingservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "principal")
    private String principal;

    @Column(name = "csv")
    private String CSV;

    @Column(name = "user_id")
    private Long user_id;

    public Card(String number, String principal, String CSV, Long user_id) {
        this.number = number;
        this.principal = principal;
        this.CSV = CSV;
        this.user_id = user_id;
    }

}