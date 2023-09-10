package com.example.springjpa.dao.query;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.dao.BookDao;
import com.example.springjpa.domain.Author;
import com.example.springjpa.domain.Book;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.NoResultException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles({"local", "query"})
@DataJpaTest
@Import({BookDaoImpl.class, AuthorDaoImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoImplTest {

    @Autowired
    BookDao bookDao;
    @Autowired
    AuthorDao authorDao;

    @Test
    void getById() {
        Book newBook = new Book("Hello world", "12JDSA23", "Doma Publication", authorDao.getById(1L));
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
    void getByCriteria() {
        Book book = new Book();
        book.setTitle("Domain-Driven Design");

        List<Book> books = bookDao.findByCriteria(book);
        assertNotNull(books);
        assertEquals(1, books.size());
    }


    @Test
    void saveNewBook() {
        Book newBook = new Book("Hello world", "12JDSA23", "Doma Publication",
                authorDao.getById(1L));
        Book book = bookDao.saveNewBook(newBook);

        assertNotNull(book.getId());
        assertEquals(newBook.getTitle(), book.getTitle());
        assertEquals(newBook.getAuthor().getId(), book.getAuthor().getId());
        assertEquals(newBook.getIsbn(), book.getIsbn());
    }

    @Test
    void testFindByIsbn() {
        Book book = new Book();
        book.setIsbn("123" + RandomString.make());
        book.setTitle("ISBN test");

        book = bookDao.saveNewBook(book);

        Book fetched = bookDao.findByIsbn(book.getIsbn());

        assertEquals(book, fetched);
    }

    @Test
    void testFindAll() {
        List<Book> books = bookDao.findAll();

        assertNotNull(books);
        assertTrue(books.size() > 0);
    }

//    @Test
//    void updateBook() {
//        Book uBook = bookDao.getById(3L);
//
//        uBook.setTitle("Hehehe");
//        bookDao.updateBook(uBook);
//        Book book = bookDao.getById(3L);
//
//        assertEquals(book.getId(), book.getId());
//        assertEquals(book.getTitle(), book.getTitle());
//        assertEquals(book.getAuthor().getId(), book.getAuthor().getId());
//        assertEquals(book.getIsbn(), book.getIsbn());
//    }

    @Test
    void deleteAuthorById() {
        Book newBook = new Book("Hello world", "12JDSA23", "Doma Publication",
                authorDao.getById(1L));
        Book savedBook = bookDao.saveNewBook(newBook);

        bookDao.deleteBookById(savedBook.getId());
        assertThrows(NoResultException.class,() -> {
            Book book = bookDao.getById(savedBook.getId());
        });
//        assertNull(book);
    }
}