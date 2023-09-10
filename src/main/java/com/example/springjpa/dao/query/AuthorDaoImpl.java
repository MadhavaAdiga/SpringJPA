package com.example.springjpa.dao.query;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.domain.Author;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Profile("query")
@Component
public class AuthorDaoImpl implements AuthorDao {
    private final EntityManagerFactory entityManagerFactory;

    public AuthorDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Native Query
     *
     * @param id author id
     * @return author
     */
    @Override
    public Author getById(Long id) {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createNativeQuery("SELECT * FROM author a WHERE a.id = ?", Author.class);
            query.setParameter(1, id);

            return (Author) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Method using Hibernate Query
     *
     * @param lastName of author
     * @return list of author
     */
    @Override
    public List<Author> findByLastName(String lastName) {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createQuery("SELECT a from Author a where a.lastName like:last_name");
            query.setParameter("last_name", lastName + "%");

            List<Author> authors = query.getResultList();
            return authors;
        } finally {
            em.close();
        }
    }

    /**
     * Hibernate Named Query
     * named query found in entity class
     *
     * @return list of authors
     */
    @Override
    public List<Author> finalAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Author> query = em.createNamedQuery("author_find_all", Author.class);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> findAll(int size, int offset) {
        return null;
    }

    @Override
    public List<Author> findAll(Pageable pageable) {
        return null;
    }

    /**
     * Hibernate criteria Query
     *
     * @param criteria author
     * @return list of author
     */
    @Override
    public List<Author> findByCriteria(Author criteria) {
        EntityManager em = getEntityManager();

        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Author> query = builder.createQuery(Author.class);

            Root<Author> root = query.from(Author.class);

            List<Predicate> predicates = new ArrayList<>();
            if (criteria != null) {
                if (criteria.getId() != null) {
                    predicates.add(builder.equal(root.get("id"), criteria.getId()));
                }
                if (criteria.getFirstName() != null) {
                    predicates.add(builder.equal(root.get("firstName"), criteria.getFirstName()));
                }
                if (criteria.getLastName() != null) {
                    predicates.add(builder.equal(root.get("lastName"), criteria.getLastName()));
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

    @Override
    public List<Author> findByLastName(String lastName, Pageable pageable) {
        return null;
    }

    /**
     * Hibernate Named Query with parameter
     * named query found in entity class
     *
     * @param firstName of author
     * @param lastName  of author
     * @return author
     */
    @Override
    public Author getByName(String firstName, String lastName) {
        EntityManager entityManager = getEntityManager();
        TypedQuery<Author> query = entityManager.createNamedQuery("find_by_name", Author.class);
        query.setParameter("first_name", firstName);
        query.setParameter("last_name", lastName);

        Author author = query.getSingleResult();
        entityManager.close();
        return author;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(author);
        em.flush();
        em.getTransaction().commit();

        em.close();
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(author);
            em.flush();
            em.clear();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return author;
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();
        Author author = em.find(Author.class, id);
        em.getTransaction().begin();
        em.remove(author);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
