package ru.dmitry.seleznev.dao;

import org.springframework.stereotype.Repository;
import ru.dmitry.seleznev.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDAOImpl implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Role getRole(String role) {
            return entityManager.createQuery("select r from Role r where r.role = :role", Role.class)
                    .setParameter("role", role).getSingleResult();
    }

    @Override
    public void deleteRole(String role) {
        entityManager.remove(getRole(role));
    }
}