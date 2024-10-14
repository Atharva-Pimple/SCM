package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helper.Alert;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session){
        
        User user=userService.getUserByToken(token).orElse(null);
        if(user!=null){
            if(user.getEmailToken().equals(token)){
                user.setEnabled(true);
                user.setEmailVerified(true);
                userService.saveUserEnable(user);
                session.setAttribute("message",Alert.builder()
                    .content("Email Verified.User is Enabled ")
                    .type(MessageType.green).build() 
                );
                return "success_page";
            }
        }

        session.setAttribute("message",Alert.builder()
            .content("Something faild.Email not Verified")
            .type(MessageType.red).build() 
        );

        return "error_page";
    }
}
