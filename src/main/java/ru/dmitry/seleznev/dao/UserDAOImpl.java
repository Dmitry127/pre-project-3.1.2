package ru.dmitry.seleznev.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitry.seleznev.model.Role;
import ru.dmitry.seleznev.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUser(String email) {
        return entityManager.createQuery("select u from  User u join fetch u.roles where u.email = :email", User.class)
                .setParameter("email", email).getSingleResult();
    }

    @Override
    @Transactional
    public Role getRole(String role) {
        try {
            return entityManager.createQuery("select r from Role r where r.role = :role", Role.class)
                    .setParameter("role", "ROLE_" + role).getSingleResult();
        } catch (NoResultException e) {
            Role newRole = new Role();
            newRole.setRole("ROLE_" + role);
            entityManager.persist(newRole);
            return entityManager.createQuery("select r from Role r where r.role = :role", Role.class)
                    .setParameter("role", "ROLE_" + role).getSingleResult();
        }
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        entityManager.remove(getUser(email));
    }
}
