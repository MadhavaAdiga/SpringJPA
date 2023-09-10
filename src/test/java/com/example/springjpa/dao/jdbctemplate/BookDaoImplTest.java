package com.example.springjpa.dao.jdbctemplate;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.dao.BookDao;
import com.example.springjpa.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles({"local","jdbc_template"})
@DataJpaTest
@Import({BookDaoImpl.class,AuthorDaoImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoImplTest {

    @Autowired
    BookDao bookDao;
    @Autowired
    AuthorDao authorDao;

    @Test
    void testFindAll() {
        List<Book> books = bookDao.findAll();

        assertNotNull(books);
        assertTrue(books.size() > 0);
    }


    @Test
    void testFindAllBySizeAndOffset() {
        List<Book> books = bookDao.findAll(3,0);

        assertNotNull(books);
        assertEquals(3, books.size());
    }

    @Test
    void testFindAllBySizeAndOffset2() {
        List<Book> books = bookDao.findAll(3,100);

        assertNotNull(books);
        assertEquals(0, books.size());
    }

    @Test
    void testFindAllByPageable() {
        List<Book> books = bookDao.findAll(PageRequest.of(0,3));

        assertNotNull(books);
        assertEquals(3, books.size());
    }

    @Test
    void testFindAllByPageable2() {
        List<Book> books = bookDao.findAll(PageRequest.of(100,3));

        assertNotNull(books);
        assertEquals(0, books.size());
    }

    @Test
    void testFindAllSortByTitle() {
        List<Book> books = bookDao.findAllSortByTitle(
                PageRequest.of(0,10, Sort.by(Sort.Order.desc("title"))));

        assertNotNull(books);
        assertTrue(books.size() > 0);
    }


    @Test
    void getById() {
        Book newBook = new Book("Hello world1", "12JDSA23", "Doma Publication",
                authorDao.getById(1L));
        Book savedBook = bookDao.saveNewBook(newBook);

        Book book = bookDao.getById(savedBook.getId());
        assertNotNull(book);
        assertEquals(savedBook.getId(), book.getId());
    }

    @Test
    void getByTitle() {
        Book book = bookDao.getByTitle("Domain-Driven Design");
        assertNotNull(book);
        assertEquals("Domain-Driven Design", book.getTitle());
    }

    @Test
    void saveNewBook() {
        Book newBook = new Book("Hello world2", "12JDSA23", "Doma Publication",
                authorDao.getById(1L));
        Book book = bookDao.saveNewBook(newBook);

        assertNotNull(book.getId());
        assertEquals(newBook.getTitle(), book.getTitle());
        assertEquals(newBook.getAuthor().getId(), book.getAuthor().getId());
        assertEquals(newBook.getIsbn(), book.getIsbn());
    }

    @Test
    void updateBook() {
        Book uBook = bookDao.getById(3L);

        uBook.setTitle("Hehehe");
        Book book = bookDao.updateBook(uBook);
        assertEquals(uBook.getId(), book.getId());
        assertEquals(uBook.getTitle(), book.getTitle());
        assertEquals(uBook.getAuthor().getId(), book.getAuthor().getId());
        assertEquals(uBook.getIsbn(), book.getIsbn());
    }

    @Test
    void deleteAuthorById() {
        Book newBook = new Book("Hello world1", "12JDSA23", "Doma Publication",
                authorDao.getById(1L));
        Book savedBook = bookDao.saveNewBook(newBook);

        bookDao.deleteBookById(savedBook.getId());
        assertThrows(EmptyResultDataAccessException.class,()-> bookDao.getById(savedBook.getId()));
    }
}