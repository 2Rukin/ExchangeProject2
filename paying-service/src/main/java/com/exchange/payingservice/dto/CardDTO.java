package com.exchange.payingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardDTO {

    private Long id;


    private String number;


    private String principal;



    private Long CSV;

    private Long user_id;


    public CardDTO(Long id, String number, String principal, Long CSV, Long user_id) {
        this.id = id;
        this.number = number;
        this.principal = principal;
        this.CSV = CSV;
        this.user_id = user_id;
    }
}
