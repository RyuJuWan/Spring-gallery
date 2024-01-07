package org.africalib.galley.backend.controller;

import org.africalib.galley.backend.dto.OrderDto;
import org.africalib.galley.backend.entity.Order;
import org.africalib.galley.backend.repository.OrderRepository;
import org.africalib.galley.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class OrderController {

    @Autowired
    JwtService jwtService;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/api/orders")
    public ResponseEntity getOrders(@CookieValue(value = "token", required = false) String token) {
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<Order> orders = orderRepository.findAll();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/api/orders")
    public ResponseEntity pushOrder(@RequestBody OrderDto dto,
                                    @CookieValue(value = "token", required = false) String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Order newOrder = new Order();
        newOrder.setMemberId(jwtService.getId(token));
        newOrder.setName(dto.getName());
        newOrder.setAddress(dto.getAddress());
        newOrder.setPayment(dto.getPayment());
        newOrder.setCardNumber(dto.getCardNumber());
        newOrder.setItems(dto.getItems());

        orderRepository.save(newOrder);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
