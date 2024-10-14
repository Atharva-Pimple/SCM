package com.scm.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping("/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Model model,Authentication authentication) {
        return "user/profile";
    }
    
}
