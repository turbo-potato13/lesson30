package com.geekbrains.book.store;

import com.geekbrains.book.store.beans.Cart;
import com.geekbrains.book.store.entities.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class CartTest {
    @Autowired
    private Cart cart;

    private final Book book1 = new Book(1L, "Harry Potter", "Desc", BigDecimal.valueOf(1488L), 2001, Book.Genre.FANTASY);
    private final Book book2 = new Book(2L, "LOTR", "Desc", BigDecimal.valueOf(1000L), 2004, Book.Genre.FICTION);


    @BeforeEach
    public void init() {
    }

    @Test
    void tryToAdd() {
        Assertions.assertAll(() -> {
            cart.add(book1);
            Assertions.assertEquals(book1, cart.getItems().get(0).getBook());
            Assertions.assertEquals(BigDecimal.valueOf(1488.0), cart.getPrice());
            Assertions.assertEquals(1, cart.getItems().size());
        }, () -> {
            cart.add(book2);
            Assertions.assertEquals(book2, cart.getItems().get(1).getBook());
            Assertions.assertEquals(BigDecimal.valueOf(2488.0), cart.getPrice());
            Assertions.assertEquals(2, cart.getItems().size());
        });
    }

    @Test
    void tryToDeleteBook() {
        cart.add(book1);
        cart.add(book2);
        Assertions.assertAll(() -> {
            cart.removeByProductId(1L);
            Assertions.assertEquals(1, cart.getItems().size());
            Assertions.assertNotEquals(book1, cart.getItems().get(0).getBook());
            Assertions.assertEquals(BigDecimal.valueOf(1000.0), cart.getPrice());
        }, () -> {
            cart.removeByProductId(2L);
            Assertions.assertEquals(0, cart.getItems().size());
            Assertions.assertEquals(BigDecimal.valueOf(0.0), cart.getPrice());
        });
    }

    @Test
    void tryToClearCart() {
        cart.add(book1);
        cart.add(book2);
        cart.clear();
        Assertions.assertEquals(0, cart.getItems().size());
        Assertions.assertEquals(BigDecimal.valueOf(0.0), cart.getPrice());
    }
}
