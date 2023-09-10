package com.example.springjpa.dao.jdbc;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.dao.BookDao;
import com.example.springjpa.domain.Author;
import com.example.springjpa.domain.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Profile("jdbc")
@Component
public class BookDaoImpl implements BookDao {

    private final DataSource dataSource;
    private final AuthorDao authorDao;

    public BookDaoImpl(DataSource dataSource, AuthorDao authorDao) {
        this.dataSource = dataSource;
        this.authorDao = authorDao;
    }

    @Override
    public Book getById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Book book = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM book where id=?");
            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                book = createBook(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeAll(resultSet, statement, connection);
        }
        return book;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public List<Book> findAll(int size, int offset) {
        return null;
    }

    @Override
    public List<Book> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findAllSortByTitle(Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findByCriteria(Book criteria) {
        return null;
    }

    @Override
    public Book getByTitle(String title) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Book book = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM book where title=?");
            statement.setString(1, title);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                book = createBook(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeAll(resultSet, statement, connection);
        }
        return book;
    }

    @Override
    public Book saveNewBook(Book book) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Book book1 = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO book(title,isbn,publisher,author_id) VALUE(?,?,?,?)");
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getIsbn());
            statement.setString(3, book.getPublisher());
            if (book.getAuthor() != null) {
                statement.setLong(4, book.getAuthor().getId());
            } else {
                statement.setNull(4, -5);
            }
            statement.execute();

            book1 = this.getByTitle(book.getTitle());
            Author author = authorDao.getById(book1.getAuthor().getId());
            book1.setAuthor(author);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeAll(resultSet, statement, connection);
        }
        return book1;
    }

    @Override
    public Book updateBook(Book book) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Book book1 = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("UPDATE book SET title=?,isbn=?,publisher=?,author_id=? WHERE id=?");
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getIsbn());
            statement.setString(3, book.getPublisher());
            if (book.getAuthor() != null) {
                statement.setLong(4, book.getAuthor().getId());
            } else {
                statement.setNull(4, -5);
            }
            statement.setLong(5, book.getId());
            statement.execute();

            book1 = this.getById(book.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeAll(resultSet, statement, connection);
        }
        return book1;
    }

    @Override
    public void deleteBookById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("DELETE FROM book WHERE id=?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeAll(null, statement, connection);
        }
    }


    private Book createBook(ResultSet resultSet) throws SQLException {
        Book book;
        book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setAuthor(authorDao.getById(resultSet.getLong("author_id")));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setTitle(resultSet.getString("title"));
        return book;
    }

    private void closeAll(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
