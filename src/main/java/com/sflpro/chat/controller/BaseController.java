package com.sflpro.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.sflpro.chat.util.Constants.*;

@Controller
public class BaseController {

    @GetMapping("login")
    private String loginPage() {
        return LOGIN_PAGE;
    }

    @GetMapping("removeUser")
    private String removeUser() {
        return REMOVE_USER_PAGE;
    }

    @GetMapping("manageUsers")
    public String manageUsers() {
        return MANAGE_USER_PAGE;
    }

    @GetMapping("forgetPassword")
    public String forgetPassword() {
        return FORGET_PASSWORD;
    }
}
