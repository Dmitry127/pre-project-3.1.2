package ru.dmitry.seleznev.service;

import ru.dmitry.seleznev.model.Role;

import java.util.Set;

public interface RoleService {

    void saveRole(Role role);

    Role getRole(String role);

    Set<Role> updateRoles(Set<Role> role);
}
