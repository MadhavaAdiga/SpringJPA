package com.example.springjpa.dao.jdbctemplate;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.domain.Author;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("jdbc_template")
@Component
public class AuthorDaoImpl implements AuthorDao {
    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        String sql = "select author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author\n" +
                "left outer join book on author.id = book.author_id where author.id = ?";

        return jdbcTemplate.query(sql, new AuthorExtractor(), id);
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        return null;
    }

    @Override
    public List<Author> finalAll() {
        return jdbcTemplate.query("SELECT * FROM author", getRowMapper());
    }

    @Override
    public List<Author> findAll(int size, int offset) {
        Pageable pageable = PageRequest.ofSize(size);

        pageable = offset > 0 ?
                pageable.withPage(offset / size) :
                pageable.withPage(0);

        return findAll(pageable);
    }

    @Override
    public List<Author> findAll(Pageable pageable) {
        return jdbcTemplate.query("SELECT * FROM author LIMIT ? OFFSET ?",
                getRowMapper(),
                pageable.getPageSize(),
                pageable.getOffset());
    }

    @Override
    public List<Author> findByCriteria(Author criteria) {
        return null;
    }

    @Override
    public List<Author> findByLastName(String lastName, Pageable pageable) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM author WHERE last_name=?");
        sql.append(" ");
        if (pageable.getSort().getOrderFor("first_name") != null) {
            sql.append(String.format("ORDER BY first_name %s",
                    pageable.getSort().getOrderFor("first_name").getDirection().name()));
        }
        sql.append(" ");
        sql.append("LIMIT ? OFFSET ?");

        return jdbcTemplate.query(sql.toString(), getRowMapper(), lastName, pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public Author getByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("SELECT * FROM author where first_name=? AND last_name=?"
                , getRowMapper()
                , firstName, lastName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name,last_name) VALUES (?,?)"
                , author.getFirstName(), author.getLastName());

        return getByName(author.getFirstName(), author.getLastName());
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author SET first_name=?, last_name=? WHERE id=?"
                , author.getFirstName(), author.getLastName(), author.getId());

        return getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id=?", id);
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }
}
