package com.example.springjpa.dao;

import com.example.springjpa.domain.Book;
import com.example.springjpa.enums.Sort;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {

    List<Book> findAll();

    List<Book> findAll(int size, int offset);

    List<Book> findAll(Pageable pageable);

    Book getById(Long id);

    Book findByIsbn(String isbn);

    List<Book> findAllSortByTitle(Pageable pageable);

    List<Book> findByCriteria(Book criteria);

    Book getByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}
