package com.example.springjpa.dao.jdbctemplate;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.dao.BookDao;
import com.example.springjpa.domain.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile({"jdbc_template"})
@Component
public class BookDaoImpl implements BookDao {
    private final AuthorDao authorDao;
    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(AuthorDao authorDao, JdbcTemplate jdbcTemplate) {
        this.authorDao = authorDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book author where id=?"
                , getRowMapper()
                , id);
    }

    @Override
    public Book findByIsbn(String isbn) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT  * FROM book", getRowMapper());
    }

    @Override
    public List<Book> findAll(int size, int offset) {
        return jdbcTemplate.query("SELECT  * FROM book LIMIT ? OFFSET ?",
                getRowMapper(),
                size,
                offset);
    }

    @Override
    public List<Book> findAll(Pageable pageable) {
        return jdbcTemplate.query("SELECT  * FROM book LIMIT ? OFFSET ?",
                getRowMapper(),
                pageable.getPageSize(),
                pageable.getOffset());
    }

    @Override
    public List<Book> findAllSortByTitle(Pageable pageable) {
        String sql = String.format("SELECT * FROM book order by title %s limit ? offset ?",
                pageable.getSort().getOrderFor("title").getDirection().name());

        return jdbcTemplate.query(sql, getRowMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Book> findByCriteria(Book criteria) {
        return null;
    }

    @Override
    public Book getByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where title=?"
                , getRowMapper()
                , title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book(title,isbn,publisher,author_id) VALUE(?,?,?,?)"
                , book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthor().getId());

        return getByTitle(book.getTitle());
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book SET title=?,isbn=?,publisher=?,author_id=? WHERE id=?"
                , book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthor().getId(), book.getId());

        return getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    private RowMapper<Book> getRowMapper() {
        return (rs, rowNum) -> {
            Book book = new Book();
            book.setTitle(rs.getString("title"));
            book.setPublisher(rs.getString("publisher"));
            book.setAuthor(authorDao.getById(rs.getLong("author_id")));
            book.setIsbn(rs.getString("isbn"));
            book.setId(rs.getLong("id"));
            return book;
        };
    }
}
