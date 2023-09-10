package com.example.springjpa.repositories;

import com.example.springjpa.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.title=?1")
    Book findBookByTitleWithQuery(String title);

    @Query("SELECT b FROM Book b WHERE b.title=:title")
    Book findBookByTitleWithQueryNamedParam(@Param("title") String title);

    @Query(value = "SELECT * FROM book WHERE title=:title", nativeQuery = true)
    Book findBookByTitleWithNativeQuery(@Param("title") String title);

    // check book entity for declaration
    Book findTitleByNamedQuery(@Param("title")String title);

    Optional<Book> findBookByIsbn(String isbn);

    Optional<Book> findBookByTitle(String title);

    Book readByTitle(String title);

    /**
     * Requires NonNullApi annotation added in package-info.java
     */
    @Nullable
    Book getByTitle(String title);

    Stream<Book> findAllByTitleNotNull();

    @Async
    Future<Book> queryByTitle(String title);
}
