package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.AdminUserUpdateDto;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    public final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getByUserId(id);
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute("user") AdminUserUpdateDto userUpdateDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/user-form";
        }

        userService.updateUser(id, userUpdateDto);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/permanent-delete")
    public String hardDeleteUser(@PathVariable Long id) {
        userService.hardDeleteUser(id);
        return "redirect:/admin/users";
    }
}
