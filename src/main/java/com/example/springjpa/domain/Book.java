package com.example.springjpa.domain;

import javax.persistence.*;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name = "find_all_books", query = "From Book"),
        @NamedQuery(name = "find_by_title", query = "FROM Book b where b.title = :title"),
        // for JPA repository named query, implemented in interface
        @NamedQuery(name = "Book.findTitleByNamedQuery", query = "FROM Book b where b.title = :title")
})
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;
    private String publisher;
//    @Transient
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author authorId;

    public Book() {

    }

    public Book(String title, String isbn, String publisher, Author authorId) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Author getAuthor() {
        return authorId;
    }

    public void setAuthor(Author authorId) {
        this.authorId = authorId;
    }
}
