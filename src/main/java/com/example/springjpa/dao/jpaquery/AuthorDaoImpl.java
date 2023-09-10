package com.example.springjpa.dao.jpaquery;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.domain.Author;
import com.example.springjpa.repositories.AuthorRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Profile("jpaquery")
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final AuthorRepository authorRepository;

    public AuthorDaoImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getById(Long id) {
        return authorRepository.getReferenceById(id);
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        return authorRepository.findAuthorByLastNameLike(lastName);
    }

    @Override
    public List<Author> finalAll() {
        return authorRepository.findAll();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Author> findByLastName(String lastName, Pageable pageable) {
        return null;
    }

    @Override
    public Author getByName(String firstName, String lastName) {
        return authorRepository.findAuthorByFirstNameAndLastName(firstName,lastName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return authorRepository.saveAndFlush(author);
    }

    @Override
    @Transactional
    public Author updateAuthor(Author author) {
        Author authorDb = getById(author.getId());
        authorDb.setFirstName(author.getFirstName());
        authorDb.setLastName(author.getLastName());
//        author.setBooks(author.getBooks());
        return authorRepository.save(authorDb);
    }

    @Override
    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }
}
