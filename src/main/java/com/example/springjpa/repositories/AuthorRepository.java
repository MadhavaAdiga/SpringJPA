package com.example.springjpa.repositories;


import com.example.springjpa.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findAuthorByFirstNameAndLastName(String firstName, String lastName);

    List<Author> findAuthorByLastNameLike(String lastName);

    Page<Author> findAuthorByLastName(String lastName, Pageable pageable);

    /**
     * Requires NonNullApi annotation added in package-info.java
     */
    @Nullable
    Author getAuthorByFirstNameAndLastName(String firstName, String lastName);
}
