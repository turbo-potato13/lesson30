package com.geekbrains.book.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekbrains.book.store.controllers.rest.BookRestController;
import com.geekbrains.book.store.entities.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookRestControllerTest {
    private BookRestController bookRestController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Book book = new Book(2L, "LOTR", "Desc", BigDecimal.valueOf(1000L), 2004, Book.Genre.FICTION);

    BookRestControllerTest() {
    }

    @Test
    void tryToStart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    } //Не совсем понял нужно ли добавлять проверку json объектов здесь, ведь данные могут поменяться.
    //Но тогда следущий тест тоже нужно будет исправить

    @Test
    void tryGetBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{ \"id\": 1," +
                        "\"title\": \"Harry Potter: Philosopher's Stone\"," +
                        "\"description\": \"Description 1\", " +
                        "\"price\": 300.00, " +
                        "\"publishYear\": 2000, " +
                        "\"genre\": \"FANTASY\" }"));
    }

    @Test
    void tryCreateNewBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());
    }

    @Test
    void tryToModifyBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    void tryToDeleteAllBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books"))
                .andExpect(status().isOk());
    }

    @Test
    void tryToDeleteBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/1"))
                .andExpect(status().isOk());
    }
}
