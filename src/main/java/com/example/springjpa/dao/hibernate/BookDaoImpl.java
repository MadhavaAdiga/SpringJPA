package com.example.springjpa.dao.hibernate;

import com.example.springjpa.dao.BookDao;
import com.example.springjpa.domain.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

@Profile("hibernate")
@Component
public class BookDaoImpl implements BookDao {
    private final EntityManagerFactory emf;

    public BookDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Book getById(Long id) {
        EntityManager em = getEntityManager();
        Book book = em.find(Book.class, id);
        em.close();
        return book;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Book> findAll(int size, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            query.setFirstResult(offset);
            query.setMaxResults(size);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Book> findAll(Pageable pageable) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            query.setFirstResult(Math.toIntExact(pageable.getOffset()));
            query.setMaxResults(pageable.getPageSize());

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Book> findAllSortByTitle(Pageable pageable) {
        EntityManager em = getEntityManager();
        try {
            String hql = String.format("SELECT b FROM Book b order by b.title %s",
                    pageable.getSort().getOrderFor("title").getDirection().name());

            TypedQuery<Book> query = em.createQuery(hql, Book.class);
            query.setFirstResult(Math.toIntExact(pageable.getOffset()));
            query.setMaxResults(pageable.getPageSize());

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Book> findByCriteria(Book criteria) {
        return null;
    }

    @Override
    public Book getByTitle(String title) {
        EntityManager em = getEntityManager();
        TypedQuery<Book> query = em
                .createQuery("SELECT b FROM Book b where b.title = :title", Book.class);
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
