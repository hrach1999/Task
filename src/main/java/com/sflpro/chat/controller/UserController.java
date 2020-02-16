package com.sflpro.chat.controller;

import com.sflpro.chat.enums.Role;
import com.sflpro.chat.model.User;
import com.sflpro.chat.security.SpringUser;
import com.sflpro.chat.service.UserService;
import com.sflpro.chat.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

import static com.sflpro.chat.util.Constants.*;

@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String resolvePage() {
        User user = userService
                .findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        String redirectPage = LOGIN_PAGE;
        if (user != null) {
            Role role = user.getRole();
            redirectPage = REDIRECT_ADMIN_PAGE;
            if (Role.USER == role)
                redirectPage = REDIRECT_USER_PAGE;
        }
        return redirectPage;
    }

    @GetMapping("loginSuccess")
    public String loginSuccess(
            @AuthenticationPrincipal SpringUser springUser) {
        Role role = springUser.getUser().getRole();
        String page = REDIRECT_USER_PAGE;
        if (Role.ADMIN == role)
            page = REDIRECT_ADMIN_PAGE;
        return page;
    }

    @GetMapping("admin")
    public String adminPage(ModelMap modelMap) {
        List<User> allByUserRole =
                userService.findAllByUserRole();
        modelMap.addAttribute("users", allByUserRole);
        return "admin";
    }

    @PostMapping("removeUser")
    public String removeUser(@RequestParam("email") String email, ModelMap modelMap) {
        try {
            if (Util.isBlank(email))
                throw new Exception("Invalid Email");
            String currentLoggedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            if (currentLoggedUserEmail.equals(email))
                throw new Exception("Can't delete himself");
            User user = userService.findByEmail(email);
            if (user == null)
                throw new Exception("User not found");
            if (Role.ADMIN == user.getRole())
                throw new Exception("Can't delete admin");
            userService.removeByEmail(email);
            modelMap.addAttribute(MESSAGE, "User is deleted");
        } catch (Exception ex) {
            modelMap.addAttribute(MESSAGE, ex.getMessage());
            logger.error(ex.getMessage());
        }
        return REMOVE_USER_PAGE;
    }

    @PostMapping("manageUsers")
    public String addUser(@ModelAttribute User user, @RequestParam(value = "image", required = false) MultipartFile file, ModelMap modelMap) {
        try {
            user.setRole(Role.USER);
            if (!Util.isValidEmail(user.getEmail()))
                throw new Exception("Invalid Email address");
            User dbUser = userService.findByEmail(user.getEmail());
            if (dbUser != null) {
                if (!Util.isBlank(user.getFirstName()))
                    dbUser.setFirstName(user.getFirstName());
                if (!Util.isBlank(user.getLastName()))
                    dbUser.setFirstName(user.getLastName());
                if (!Util.isBlank(file.getOriginalFilename()))
                    dbUser.setImagePath(Util.saveImage(file));
                if (Util.isBlank(dbUser.getImagePath()))
                    dbUser.setImagePath(DEFAULT_AVATAR);
                userService.save(dbUser);
                modelMap.addAttribute(MESSAGE, "User is updated");
            } else {
                if (!Util.isValidUser(user))
                    throw new Exception("Invalid user data");
                if (!Util.isBlank(file.getOriginalFilename()))
                    user.setImagePath(Util.saveImage(file));
                else
                    user.setImagePath(DEFAULT_AVATAR);
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                userService.save(user);
                modelMap.addAttribute(MESSAGE, "User is created");
            }
        } catch (Exception ex) {
            modelMap.addAttribute(MESSAGE, ex.getMessage());
            modelMap.addAttribute("type", "error");
            logger.error(ex.getMessage());
        }
        return MANAGE_USER_PAGE;
    }

    @PostMapping("forgetPassword")
    public String forgetPassword(@RequestParam("email") String email,
                                 ModelMap modelMap) {
        try {
            boolean isUpdatedPassword = userService.isUpdatedForgottenPassword(email);
            if (isUpdatedPassword)
                modelMap.addAttribute(MESSAGE, "New password send to your email");
            else
                modelMap
                        .addAttribute(MESSAGE, "User with this email is not exist,or account is not active");
        } catch (MessagingException ex) {
            logger.error(ex.getMessage());
            modelMap.addAttribute(MESSAGE, "Updating forgotten password process is failed");
        }
        return FORGET_PASSWORD;
    }

    @GetMapping("user")
    public String userPage(ModelMap modelMap) {
        User user = userService
                .findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("users", userService.findAllUsersByRoleUserAndEmailNotEqual(user.getEmail()));
        return USER_PAGE;
    }
}
