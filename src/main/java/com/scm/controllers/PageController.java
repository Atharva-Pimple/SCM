package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Alert;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;





@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping(value = "/home")
    public String home(Model model){
        System.out.println("Home page handler");
        model.addAttribute("name","SCM By Atharva Pimple");
        model.addAttribute("githubRepo", "https://github.com/Atharva-Pimple");
        return "home";
    }
    
    @RequestMapping("/about")
    public String aboutPage(){
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contactPage() {
        System.out.println("contact page loading");
        return new String("contact");
    }

    @GetMapping("/login")
    public String loginPage() {
        System.out.println("login page loading");
        return "/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        UserForm userform=new UserForm();
        if(!model.containsAttribute("userForm")){
            model.addAttribute("userForm", userform);
        }

        System.out.println("register page loading");

        return "register";
    }

    @RequestMapping(value = "/do-register",method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        // System.out.println(userForm);
        // System.out.println(userForm.getName());

        if(rBindingResult.hasErrors()){
            return "register";
        }

        // User user=User.builder()
        //     .name(userForm.getName())
        //     .email(userForm.getEmail())
        //     .password(userForm.getPassword())
        //     .phoneNumber(userForm.getPhoneNumber())
        //     .about(userForm.getAbout())
        //     .profilePic("https://media.istockphoto.com/id/610003972/vector/vector-businessman-black-silhouette-isolated.jpg?s=1024x1024&w=is&k=20&c=6D1-x-pb87YSvln510lq9bHhSOW5ns5lJ3G1dIy-Oyk=")
        //     .build();

        User user=new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        // user.setProfilePic("https://media.istockphoto.com/id/610003972/vector/vector-businessman-black-silhouette-isolated.jpg?s=1024x1024&w=is&k=20&c=6D1-x-pb87YSvln510lq9bHhSOW5ns5lJ3G1dIy-Oyk=");

        userService.saveUser(user); 

        Alert message=Alert.builder()
        .content("Registration Successful")
        .type(MessageType.blue).build();

        

        session.setAttribute("message", message);
        return "redirect:/register";
    }
    
}
