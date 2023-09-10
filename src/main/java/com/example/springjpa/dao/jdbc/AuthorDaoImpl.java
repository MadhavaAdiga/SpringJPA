package com.example.springjpa.dao.jdbc;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.domain.Author;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Profile("jdbc")
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    public AuthorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Author getById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Author author = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM author where id=?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                author = createAuthor(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(resultSet, statement,connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return author;
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        return null;
    }

    @Override
    public List<Author> finalAll() {
        return null;
    }

    @Override
    public List<Author> findAll(int size, int offset) {
        return null;
    }

    @Override
    public List<Author> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Author> findByCriteria(Author criteria) {
        return null;
    }

    @Override
    public List<Author> findByLastName(String lastName, Pageable pageable) {
        return null;
    }

    @Override
    public Author getByName(String firstName, String lastName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Author author = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM author where first_name=? AND last_name=?");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                author = createAuthor(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(resultSet, statement,connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return author;
    }


    @Override
    public Author saveNewAuthor(Author author) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Author result = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO author (first_name,last_name) VALUES (?,?)");
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.execute();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM author where first_name=? AND last_name=?");
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                result = createAuthor(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(resultSet, statement,connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public Author updateAuthor(Author author) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("UPDATE author SET first_name=?, last_name=? WHERE id=?");
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setLong(3,author.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(null, statement,connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("DELETE FROM author WHERE id=?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(null, statement,connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Author createAuthor(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        return author;
    }

    private void closeAll(ResultSet resultSet, PreparedStatement statement,Connection connection) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }

        if (statement != null) {
            statement.close();
        }
        
        if(connection!=null){connection.close();}
    }
}
