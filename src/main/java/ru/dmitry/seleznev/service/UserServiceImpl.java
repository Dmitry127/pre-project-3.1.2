package ru.dmitry.seleznev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitry.seleznev.dao.UserDAO;
import ru.dmitry.seleznev.model.Role;
import ru.dmitry.seleznev.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements  UserService, UserDetailsService {

    private final UserDAO userDAO;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user, String roles) {
        Set<Role> roleSet = Stream.of(roles.split(" ")).map(s -> new Role("ROLE_" + s))
                .collect(Collectors.toSet());
        user.setRoles(roleService.updateRoles(roleSet));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.saveUser(user);
    }

    @Override
    public User getUser(long id) {
        return userDAO.getUser(id);
    }

    @Override
    public User getUser(String email) {
        return userDAO.getUser(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void updateUser(User user, String roles) {
        User persistentUser = getUser(user.getId());
        if (!user.getPassword().equals(persistentUser.getPassword())) {
            persistentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        Set<Role> roleSet = Stream.of(roles.split(" ")).map(s -> new Role("ROLE_" + s))
                .collect(Collectors.toSet());
        persistentUser.setRoles(roleService.updateRoles(roleSet));
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userDAO.deleteUser(id);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        userDAO.deleteUser(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDAO.getUser(email);
    }

}
