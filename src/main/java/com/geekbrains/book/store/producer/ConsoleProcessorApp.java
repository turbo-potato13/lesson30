package com.geekbrains.book.store.producer;

import com.geekbrains.book.store.services.OrderService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleProcessorApp {
    private static OrderService orderService;
    public static final String QUEUE_FOR_ORDER_ID = "processingQueue";
    public static final String EXCHANGER_FOR_ORDER_READY = "processingResultsExchanger";

    @Autowired
    public ConsoleProcessorApp(OrderService orderService) {
        this.orderService = orderService;
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        List<Long> orders = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            System.out.println(" [*] Ожидание новых заказов");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Заказ с id '" + message + " поступил в обработку");
                orders.add(Long.parseLong(message));

                System.out.println("Список заказов: ");
                for (Long l : orders) {
                    System.out.println(l);
                }
            };
            try {
                channel.basicConsume(QUEUE_FOR_ORDER_ID, true, deliverCallback, consumerTag -> {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            String command = scanner.nextLine();

            if (command.startsWith("/готово")) {
                Long id = Long.parseLong((command.split(" ")[1]));
                if (orders.contains(id)) {
                    orders.remove(id);
                    try {
                        channel.basicPublish(EXCHANGER_FOR_ORDER_READY, "", null, id.toString().getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Такой команды нет");

            }
        }).start();
    }
}