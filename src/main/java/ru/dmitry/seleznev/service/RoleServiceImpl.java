package ru.dmitry.seleznev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitry.seleznev.dao.RoleDAO;
import ru.dmitry.seleznev.model.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public void saveRole(Role role) {
        roleDAO.saveRole(role);
    }

    @Override
    public Role getRole(String role) {
        return roleDAO.getRole(role);
    }

    @Override
    public Set<Role> updateRoles(Set<Role> roleSet) {
        Set<Role> resultSet = new HashSet<>();
        for (Role r:
             roleSet) {
            try {
                resultSet.add(getRole(r.getRole()));
            } catch (EmptyResultDataAccessException e) {
                saveRole(r);
                resultSet.add(getRole(r.getRole()));
            }
        }
        return resultSet;
    }
}
