package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {
    private Logger logger=LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ModelAttribute
    private void addLoggedInUserInfo(Model model,Authentication authentication){
        if(authentication==null){
            return;
        }
        String username=Helper.getLoggedInUserEmail(authentication);
        logger.info("Logged user :"+username);

        User user=userService.getUserByEmail(username);
        if(user==null){
            model.addAttribute("loggedInUser", null);
        }else{
            model.addAttribute("loggedInUser", user);
        }
        
    }
}
