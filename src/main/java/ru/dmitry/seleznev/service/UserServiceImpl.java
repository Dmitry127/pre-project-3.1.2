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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements  UserService, UserDetailsService {

    private final UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(User user) {
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
    public Role getRole(String role) {
        return userDAO.getRole(role);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public void updateUser(User user, String adminRole) {
        User persistentUser = getUser(user.getId());
        persistentUser.setEmail(user.getEmail());
        persistentUser.setFirstName(user.getFirstName());
        persistentUser.setLastName(user.getLastName());
        persistentUser.setAge(user.getAge());
        persistentUser.setRoles(getRoleSet(adminRole));
        if (!user.getPassword().equals(persistentUser.getPassword())) {
            persistentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }

    @Override
    public void deleteUser(long id) {
        userDAO.deleteUser(id);
    }

    @Override
    public void deleteUser(String email) {
        userDAO.deleteUser(email);
    }

    @Override
    public Set<Role> getRoleSet(String role) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(getRole("USER"));
        if (role.equals("ADMIN")) {
            roleSet.add(getRole("ADMIN"));
        }
        return roleSet;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDAO.getUser(email);
    }

}
