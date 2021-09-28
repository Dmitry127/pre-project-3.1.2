package ru.dmitry.seleznev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.dmitry.seleznev.model.User;
import ru.dmitry.seleznev.service.UserService;

@Component
public class PopulateDb {

    private UserService userService;

    @Autowired
    public PopulateDb(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        User user = new User("user", "user", "user@mail.ru", "user", 10, null);
        userService.saveUser(user, "USER");

        User admin = new User("admin", "admin", "admin@mail.ru", "admin", 99, null);
        userService.saveUser(admin, "ADMIN");

    }
}
