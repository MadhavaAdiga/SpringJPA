package com.example.springjpa.dao;


import com.example.springjpa.domain.Author;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorDao {
    Author getById(Long id);

    List<Author> findByLastName(String lastName);

    List<Author> findByLastName(String lastName, Pageable pageable);

    List<Author> finalAll();

    List<Author> findAll(int size, int offset);

    List<Author> findAll(Pageable pageable);

    List<Author> findByCriteria(Author criteria);

    Author getByName(String firstName, String lastName);

    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);
}
