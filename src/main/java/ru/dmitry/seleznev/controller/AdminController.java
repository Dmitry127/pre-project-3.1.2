package ru.dmitry.seleznev.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dmitry.seleznev.model.Role;
import ru.dmitry.seleznev.model.User;
import ru.dmitry.seleznev.service.UserService;

import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String adminPage(Model model, Authentication auth) {
        model.addAttribute("users", userService.getAllUsers());

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userService.getRole("ADMIN"));
        roleSet.add(userService.getRole("USER"));
        model.addAttribute("roleSet", roleSet);


        User user = userService.getUser(auth.getName());
        model.addAttribute("loggedUser", user);


        return "admin";
    }

    @PostMapping()
    public String saveUser(@ModelAttribute("user") User user, @RequestParam(defaultValue = "") String adminRole) {
        user.setRoles(userService.getRoleSet(adminRole));
        userService.saveUser(user);
        return "redirect:/admin";
    }


    @PatchMapping()
    public String updateUser(@ModelAttribute("user") User user, @RequestParam(defaultValue = "") String adminRole) {
        userService.updateUser(user, adminRole);
        return "redirect:/admin";
    }

    @DeleteMapping()
    public String deleteUser(@ModelAttribute User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }

    @ModelAttribute
    public void addAttribute(Model model) {
        model.addAttribute("user", new User());
    }
}
