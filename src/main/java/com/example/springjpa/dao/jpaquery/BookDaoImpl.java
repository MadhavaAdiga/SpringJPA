package com.example.springjpa.dao.jpaquery;

import com.example.springjpa.dao.BookDao;
import com.example.springjpa.domain.Book;
import com.example.springjpa.repositories.BookRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Profile("jpaquery")
@Component
public class BookDaoImpl implements BookDao {

    private final BookRepository bookRepository;

    public BookDaoImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findAll(int size, int offset) {
        Pageable pageable = PageRequest.ofSize(size);

        pageable = offset > 0 ?
                pageable.withPage(offset / size) :
                pageable.withPage(0);

        return findAll(pageable);
    }

    @Override
    public List<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Book> findAllSortByTitle(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);

        return bookPage.getContent();
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.getReferenceById(id);
    }

    @Override
    public Book findByIsbn(String isbn) {
        return bookRepository.findBookByIsbn(isbn)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Book> findByCriteria(Book criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Book getByTitle(String title) {
        return bookRepository.findBookByTitle(title)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Book saveNewBook(Book book) {
        return bookRepository.saveAndFlush(book);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        Book bookDb = getById(book.getId());
        bookDb.setTitle(book.getTitle());
        bookDb.setIsbn(book.getIsbn());
        bookDb.setPublisher(book.getPublisher());
//        bookDb.setAuthor(book.getAuthor());
        return bookRepository.save(bookDb);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
