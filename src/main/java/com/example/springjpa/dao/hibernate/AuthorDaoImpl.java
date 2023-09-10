package com.example.springjpa.dao.hibernate;

import com.example.springjpa.dao.AuthorDao;
import com.example.springjpa.domain.Author;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

@Profile("hibernate")
@Component
public class AuthorDaoImpl implements AuthorDao {
    private final EntityManagerFactory entityManagerFactory;

    public AuthorDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Author> finalAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a", Author.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> findAll(int size, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a", Author.class);
            query.setFirstResult(offset);
            query.setMaxResults(size);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> findAll(Pageable pageable) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a", Author.class);
            query.setFirstResult(Math.toIntExact(pageable.getOffset()));
            query.setMaxResults(pageable.getPageSize());

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> findByCriteria(Author criteria) {
        return null;
    }

    @Override
    public Author getById(Long id) {
        EntityManager entityManager = getEntityManager();
        Author author = entityManager.find(Author.class, id);
        entityManager.close();
        return author;
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a WHERE a.lastName :lastName", Author.class);
            query.setParameter("lastName", lastName);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> findByLastName(String lastName, Pageable pageable) {
        EntityManager em = getEntityManager();

        StringBuilder hql = new StringBuilder();

        hql.append("SELECT a FROM Author a WHERE a.lastName = :last_name");
        hql.append(" ");

        Sort.Order lastNameSortOrder = pageable.getSort().getOrderFor("first_name");
        if(lastNameSortOrder != null){
            hql.append(String.format("order by a.firstName %s", lastNameSortOrder.getDirection().name()));
        }

        try {
            TypedQuery<Author> query = em.createQuery(hql.toString(), Author.class);
            query.setParameter("last_name", lastName);
            query.setFirstResult(Math.toIntExact(pageable.getOffset()));
            query.setMaxResults(pageable.getPageSize());

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Author getByName(String firstName, String lastName) {
        EntityManager entityManager = getEntityManager();
        TypedQuery<Author> query = entityManager.createQuery(
                "SELECT a FROM Author a WHERE a.firstName = :first_name AND a.lastName = :last_name",
                Author.class
        );
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
