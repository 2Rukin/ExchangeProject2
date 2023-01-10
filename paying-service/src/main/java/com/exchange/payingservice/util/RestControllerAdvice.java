package com.exchange.payingservice.util;

import com.exchange.payingservice.dto.CardDTO;
import com.exchange.payingservice.dto.StatusCards;
import com.exchange.payingservice.entity.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestControllerAdvice {

    public static ResponseEntity<Object> generateResponsePost(CardDTO card) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", new StatusCards("Карта сохранена",
                "Управляйте картами в платежной информации"));
        map.put("create_at", LocalDateTime.now());
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", message);
        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generateResponse(CardDTO cardDTO) {
        if (cardDTO == null) {
            throw new UsernameNotFoundException("Card not found");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", cardDTO);
        map.put("create_at", LocalDateTime.now());
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> generateResponsePut(Card card) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", new StatusCards("Карта изменена",
                "Управляйте картами в платежной информации"));
        map.put("create_at", LocalDateTime.now());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> generateResponseDelete(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", new StatusCards(String.format("Карта c id %d удалена", id),
                "Управляйте картами в платежной информации"));
        map.put("create_at", LocalDateTime.now());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }




}
