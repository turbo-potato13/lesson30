package com.geekbrains.book.store.consumer;

import com.geekbrains.book.store.entities.Order;
import com.geekbrains.book.store.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderMessageReceiver {
    private final OrderService orderService;

    @Autowired
    public OrderMessageReceiver(OrderService orderService) {
        this.orderService = orderService;
    }

    @Transactional
    public void receiveMessage(byte[] message) {
        log.info("Received from topic <" + new String(message) + ">");
        orderService.findById(Long.parseLong(new String(message)))
                .ifPresent(o -> o.setStatus(Order.Status.READY));
    }
}