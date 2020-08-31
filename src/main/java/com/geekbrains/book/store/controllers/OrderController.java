package com.geekbrains.book.store.controllers;

import com.geekbrains.book.store.beans.Cart;
import com.geekbrains.book.store.consumer.OrderMessageReceiver;
import com.geekbrains.book.store.entities.Order;
import com.geekbrains.book.store.entities.User;
import com.geekbrains.book.store.exceptions.ResourceNotFoundException;
import com.geekbrains.book.store.services.OrderService;
import com.geekbrains.book.store.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    public static final String EXCHANGE_FOR_PROCESSING_TASK = "processingExchanger";
    public static final String QUEUE_WITH_PROCESSING_TASK_RESULTS = "processingResultsQueue";

    private final RabbitTemplate rabbitTemplate;
    private final UserService userService;
    private final OrderService orderService;
    private final Cart cart;

    @GetMapping("/create")
    public String createOrder(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).get();
        Order order = new Order(user, cart);
        orderService.saveOrder(order);
        model.addAttribute("user", user);
        rabbitTemplate.convertAndSend(OrderController.EXCHANGE_FOR_PROCESSING_TASK, null,  order.getId().toString() );
        return "order_info";
    }

    @Bean
    public SimpleMessageListenerContainer containerForTopic(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_WITH_PROCESSING_TASK_RESULTS);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(OrderMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


    @PostMapping("/confirm")
    @ResponseBody
    public String confirmOrder(Principal principal) {
        return Optional.of(principal)
                .map(Principal::getName)
                .flatMap(userService::findByUsername)
                .map(user -> {
                    Order order = new Order(user, cart);
                    order.setStatus(Order.Status.READY);
                    order = orderService.saveOrder(order);
                    return order.getId() + " " + order.getPrice();
                })
                .orElseThrow(() -> new ResourceNotFoundException("User was not found!"));
    }
}
