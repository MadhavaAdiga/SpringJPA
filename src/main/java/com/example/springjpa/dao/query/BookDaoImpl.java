package com.example.springjpa.dao.query;

import com.example.springjpa.dao.BookDao;
import com.example.springjpa.domain.Author;
import com.example.springjpa.domain.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Profile("query")
@Component
public class BookDaoImpl implements BookDao {
    private final EntityManagerFactory emf;

    public BookDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Native query
     * @param id book id
     * @return book
     */
    @Override
    public Book getById(Long id) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT * FROM book b WHERE b.id = ?", Book.class);
            query.setParameter(1, id);

            return (Book) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Hibernate Typed Query
     *
     * @param isbn
     * @return
     */
    @Override
    public Book findByIsbn(String isbn) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b where b.isbn = :isbn", Book.class);
            query.setParameter("isbn", isbn);

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Hibernate Named Query
     * named query found in entity class
     *
     * @return list of books
     */
    @Override
    public List<Book> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createNamedQuery("find_all_books", Book.class);

            return query.getResultList();
        } finally {
            em.close();
        }
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

    /**
     * Hibernate criteria Query
     *
     * @param criteria author
     * @return list of author
     */
    @Override
    public List<Book> findByCriteria(Book criteria) {
        EntityManager em = getEntityManager();

        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Book> query = builder.createQuery(Book.class);

            Root<Book> root = query.from(Book.class);

            List<Predicate> predicates = new ArrayList<>();

            if (criteria != null) {
                if (criteria.getId() != null) {
                    predicates.add(builder.equal(root.get("id"), criteria.getId()));
                }
                if (criteria.getTitle() != null) {
                    predicates.add(builder.equal(root.get("title"), criteria.getTitle()));
                }
                if (criteria.getIsbn() != null) {
                    predicates.add(builder.equal(root.get("isbn"), criteria.getIsbn()));
                }

                if (predicates.size() > 0) {
                    query.where(builder.and(predicates.toArray(new Predicate[]{})));
                }
            }
            return em.createQuery(query).getResultList();

        } finally {
            em.close();
        }
    }

    /**
     * Hibernate Named Query with parameter
     * named query found in entity class
     *
     * @param title of book
     * @return book
     */
    @Override
    public Book getByTitle(String title) {
        EntityManager em = getEntityManager();
        TypedQuery<Book> query = em.createNamedQuery("find_by_title", Book.class);
        query.setParameter("title", title);

        Book book = query.getSingleResult();
        em.close();
        return book;
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(book);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return book;
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(book);
        em.flush();
        em.clear();

        Book savedBook = em.find(Book.class, book.getId());
        em.getTransaction().commit();
        em.close();

        return savedBook;
    }

    @Override
    public void deleteBookById(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        em.getTransaction().commit();
        em.close();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
