package com.geekbrains.book.store.beans;

import com.geekbrains.book.store.entities.Book;
import com.geekbrains.book.store.entities.OrderItem;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Cart {
    private List<OrderItem> items;
    private BigDecimal price;

    @PostConstruct
    public void init() {
        items = new ArrayList<>();
    }

    public void clear() {
        items.clear();
        recalculate();
    }

    public void add(Book book) {
        for (OrderItem i : items) {
            if (i.getBook().getId().equals(book.getId())) {
                i.increment();
                recalculate();
                return;
            }
        }
        items.add(new OrderItem(book));
        recalculate();
    }

    public void decrement(Book book) {
        for (OrderItem i : items) {
            if (i.getBook().getId().equals(book.getId())) {
                i.decrement();
                if (i.isEmpty()) {
                    items.remove(i);
                }
                recalculate();
                return;
            }
        }
    }

    public void removeByProductId(Long productId) {
        for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext(); ) {
            boolean condition = Optional.of(iterator.next())
                    .map(OrderItem::getBook)
                    .map(Book::getId)
                    .map(id -> id.equals(productId))
                    .orElse(false);
            if (condition) {
                iterator.remove();
                recalculate();
                return;
            }
        }
    }

    public void recalculate() {
        price = BigDecimal.valueOf(0.0);
        for (OrderItem i : items) {
            price = price.add(i.getPrice());
        }
    }
}

