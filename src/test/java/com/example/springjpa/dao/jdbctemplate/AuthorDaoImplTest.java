package com.example.springjpa.dao.jdbctemplate;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"local", "jdbc_template"})
@DataJpaTest
@Import(AuthorDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplTest {

    @Autowired
    AuthorDao authorDao;


    @Test
    void testFindAll() {
        List<Author> authors = authorDao.finalAll();

        assertNotNull(authors);
        assertTrue(authors.size() > 0);
    }

    @Test
    void testFindAllBySizeAndOffset() {
        List<Author> books = authorDao.findAll(3, 5);

        assertNotNull(books);
        assertEquals(3, books.size());
    }

    @Test
    void testFindAllBySizeAndOffset2() {
        List<Author> books = authorDao.findAll(3, 100);

        assertNotNull(books);
        assertEquals(0, books.size());
    }

    @Test
    void testFindByLastNameSortByFirstName() {
        List<Author> books = authorDao.findByLastName(
                "Smith", PageRequest.of(0, 5, Sort.by(Sort.Order.desc("first_name"))));

        assertNotNull(books);
        assertEquals(5, books.size());
    }

    @Test
    void testFindByLastNameSortByFirstName2() {
        List<Author> books = authorDao.findByLastName(
                "Smith", PageRequest.of(0, 50, Sort.by(Sort.Order.desc("first_name"))));

        assertNotNull(books);
        assertTrue(books.size() >= 10);
    }

    @Test
    void testFindAllByPageable() {
        List<Author> books = authorDao.findAll(PageRequest.of(0, 3));

        assertNotNull(books);
        assertEquals(3, books.size());
    }

    @Test
    void testFindAllByPageable2() {
        List<Author> books = authorDao.findAll(PageRequest.of(100, 3));

        assertNotNull(books);
        assertEquals(0, books.size());
    }

    @Test
    void getById() {
        Author author = authorDao.getById(1L);
        assertThat(author).isNotNull();
    }

    @Test
    void getByName() {
        Author author = authorDao.getByName("Eric", "Evans");
        assertThat(author).isNotNull();
    }

    @Test
    void saveNewAuthor() {
        Author author1 = new Author();
        author1.setFirstName("1John");
        author1.setLastName("1Doe");
        Author author = authorDao.saveNewAuthor(author1);

        assertThat(author).isNotNull();
    }

    @Test
    void updateAuthor() {
        Author author1 = new Author();
        author1.setFirstName("1John");
        author1.setLastName("2Doe");
        Author saved = authorDao.saveNewAuthor(author1);

        saved.setFirstName("J");
        Author author = authorDao.updateAuthor(saved);

        assertThat(author).isNotNull();
        assertEquals(author.getId(), saved.getId());
        assertEquals(author.getFirstName(), saved.getFirstName());
        assertEquals(author.getLastName(), saved.getLastName());
    }

    @Test
    void deleteAuthorById() {
        Author author1 = new Author();
        author1.setFirstName("@John");
        author1.setLastName("1Doe");
        Author saved = authorDao.saveNewAuthor(author1);

        authorDao.deleteAuthorById(saved.getId());

        assertThrows(TransientDataAccessResourceException.class, () -> authorDao.getById(saved.getId()));
    }
}