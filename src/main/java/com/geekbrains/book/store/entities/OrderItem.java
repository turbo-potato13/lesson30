package com.geekbrains.book.store.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders_items")
@Data
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(Book book) {
        this.book = book;
        this.quantity = 1;
        this.price = new BigDecimal(0).add(book.getPrice());
    }

    public void increment() {
        this.quantity++;
        this.price = new BigDecimal(quantity * book.getPrice().doubleValue());
    }

    public void decrement() {
        this.quantity--;
        this.price = new BigDecimal(quantity * book.getPrice().doubleValue());
    }

    public boolean isEmpty() {
        return quantity == 0;
    }
}
