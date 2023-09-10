package com.example.springjpa.repositories;

import com.example.springjpa.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"local"})
@DataJpaTest
@ComponentScan(basePackages = {"com.example.springjpa.repositories"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    void testBookStream() {
        AtomicInteger count = new AtomicInteger();

        bookRepository.findAllByTitleNotNull()
                .forEach(book -> count.incrementAndGet());

        assertTrue(count.get() > 1);
    }

    @Test
    void testBookFuture() throws ExecutionException, InterruptedException {
        Future<Book> bookFuture = bookRepository.queryByTitle("Clean Code");

        Book book = bookFuture.get();

        assertNotNull(book);
    }

    @Test
    void testBookQuery() {
        Book book = bookRepository.findBookByTitleWithQuery("Clean Code");

        assertNotNull(book);
    }

    @Test
    void testBookQueryNamedParam() {
        Book book = bookRepository.findBookByTitleWithQueryNamedParam("Clean Code");

        assertNotNull(book);
    }

    @Test
    void testBookNativeQuery() {
        Book book = bookRepository.findBookByTitleWithNativeQuery("Clean Code");

        assertNotNull(book);
    }

    @Test
    void testBookNamedQuery() {
        Book book = bookRepository.findTitleByNamedQuery("Clean Code");

        assertNotNull(book);
    }

    @Test
    void testEmptyResultException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            Book book = bookRepository.readByTitle("title");
        });
    }

    @Test
    void testNullParam() {
        assertNull(bookRepository.getByTitle(""));
    }

    @Test
    void testNoException() {
        assertNull(bookRepository.getByTitle("foo"));
    }
}