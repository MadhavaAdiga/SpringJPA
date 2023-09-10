package com.example.springjpa.dao.jdbc;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"local","jdbc"})
@DataJpaTest
@Import(AuthorDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthor() {
        Author author = authorDao.getById(1L);
        assertThat(author).isNotNull();
    }

    @Test
    void testGetName() {
        Author author = authorDao.getByName("Eric","Evans");
        assertThat(author).isNotNull();
    }

    @Test
    void savedAuthor(){
        Author author1 = new Author();
        author1.setFirstName("John");
        author1.setLastName("Doe");
        Author author = authorDao.saveNewAuthor(author1);

        assertThat(author).isNotNull();
    }

    @Test
    void updateAuthor(){
        Author author1 = new Author();
        author1.setFirstName("John");
        author1.setLastName("Doe");
        Author saved = authorDao.saveNewAuthor(author1);

        saved.setFirstName("J");
        Author author = authorDao.updateAuthor(saved);

        assertThat(author).isNotNull();
        assertEquals(author.getId(),saved.getId());
        assertEquals(author.getFirstName(),saved.getFirstName());
        assertEquals(author.getLastName(),saved.getLastName());
    }

    @Test
    void deleteAuthorById(){
        Author author1 = new Author();
        author1.setFirstName("John");
        author1.setLastName("Doe");
        Author saved = authorDao.saveNewAuthor(author1);

        authorDao.deleteAuthorById(saved.getId());

        Author author = authorDao.getById(saved.getId());
        assertThat(author).isNull();
    }
}