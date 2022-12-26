package com.exchange.payingservice.controller;

import com.exchange.payingservice.Service.PaymentService;
import com.exchange.payingservice.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/findOne")
    public ResponseEntity<Payment> getPayment(Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayment());
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createPayment(@RequestBody Payment payment) {
        paymentService.createPayment(payment);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updatePayment(@RequestBody Payment payment) {
        paymentService.updatePayment(payment);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deletePayment(Long id) {
        paymentService.deletePaymentById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
