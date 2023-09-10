package com.example.springjpa.bootstrap;

import com.example.springjpa.domain.Book;
import com.example.springjpa.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;

//@Component
//@Profile({"local","default"})
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        bookRepository.deleteAll();
        Book bookDDD = new Book("Domain Driven Development", "123", "RandomHouse", null);

        System.out.println("Id: " + bookDDD.getId());

        Book savedDDD = bookRepository.save(bookDDD);

        System.out.println("Id: " + savedDDD.getId());

        Book bookSIA = new Book("Spring In Action", "234234", "Oriely", null);
        Book savedSIA = bookRepository.save(bookSIA);

        bookRepository.findAll().forEach(book -> {
            System.out.println("Book Id: " + book.getId());
            System.out.println("Book Title: " + book.getTitle());
        });
    }
}
