package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.Alert;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("user/contacts")
public class ContactController {
    Logger logger=LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/add")
    public String addContactView(Model model){
        ContactForm form=new ContactForm();
        model.addAttribute("contactForm", form);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add-contact",method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication, HttpSession session){

        if(result.hasErrors()){
            return "user/add_contact";
        }

        
        // getting user
        String username=Helper.getLoggedInUserEmail(authentication);
        User user=userService.getUserByEmail(username);
        // process form
        Contact contact=new Contact();
        contact.setUser(user);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDiscription(contactForm.getDescription());
        contact.setFavourite(contactForm.getFavorite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        

        if(contactForm.getContactPic()!=null && !contactForm.getContactPic().isEmpty()){
            // uploading image
            String fileName=UUID.randomUUID().toString();
            String fileURL=imageService.uploadImage(contactForm.getContactPic(),fileName);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(fileName);
        }

        contactService.saveContact(contact);

        Alert message=Alert.builder()
            .content("Successfully added a new contact")
            .type(MessageType.green)
            .build();

        session.setAttribute("message", message);

        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContacts(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) int size,
        @RequestParam(value = "sortField", defaultValue = "name") String sortField,
        @RequestParam(value = "sortBy", defaultValue = "asc") String sortBy,
        Model model, Authentication authentication
        ){
        String username = Helper.getLoggedInUserEmail(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContacts = contactService.getByUser(user,page,size,sortField,sortBy);

        model.addAttribute("contacts", pageContacts);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    @RequestMapping(value = "/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) int size,
        @RequestParam(value = "sortBy", defaultValue = "asc") String sortBy,
        Model model, Authentication authentication){

            String username = Helper.getLoggedInUserEmail(authentication);
            User user = userService.getUserByEmail(username);

            Page<Contact> contacts=null;
            if(contactSearchForm.getField().equalsIgnoreCase("name")){
                contacts =contactService.searchByName(contactSearchForm.getKeyword(), page, size, contactSearchForm.getField(), sortBy,user);
            }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
                contacts=contactService.searchByEmail(contactSearchForm.getKeyword(), page, size, contactSearchForm.getField(), sortBy,user);
            }else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
                contacts=contactService.searchByPhonenumber(contactSearchForm.getKeyword(), page, size, contactSearchForm.getField(), sortBy,user);
            }else if(contactSearchForm.getField().equalsIgnoreCase("favourite")){
                contacts=contactService.searchByFavourite(page, size, sortBy,user);
            }

            model.addAttribute("contacts", contacts);
            model.addAttribute("contactSearchForm", contactSearchForm);
            return "user/search";
    }

    @RequestMapping(value = "/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId){
        contactService.deleteContact(contactId);
        logger.info("Contacte Deleted conto", contactId);
        System.out.println("Contcat ID: "+contactId);
        
        return "redirect:/user/contacts";
    }

    @RequestMapping(value = "/view/{contactId}")
    public String updateContactForm(@PathVariable String contactId, Model model){
        Contact contact=contactService.getContactById(contactId);

        ContactForm contactForm=new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDiscription());
        contactForm.setFavorite(contact.getFavorite());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }

    @RequestMapping(value = "/update/{contactId}",method = RequestMethod.POST)
    public String updateContact(
        @PathVariable("contactId") String contactId, Model model,@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }
        Contact contact=contactService.getContactById(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDiscription(contactForm.getDescription());
        contact.setFavourite(contactForm.getFavorite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setLinkedInLink(contactForm.getLinkedInLink());

        contact.setWebsiteLink(contactForm.getWebsiteLink());

        if(contactForm.getContactPic()!=null && !contactForm.getContactPic().isEmpty()){
            String filename=UUID.randomUUID().toString();
            String fileURL=imageService.uploadImage(contactForm.getContactPic(),filename);
            contact.setCloudinaryImagePublicId(filename);
            contact.setPicture(fileURL);
        }

        Contact updated= contactService.updateContact(contact);
        logger.info("Updated contact {}",updated);
        model.addAttribute("message", 
        Alert.builder().content("Contact Updated!!").type(MessageType.green).build());

        return "redirect:/user/contacts/view/"+contactId;
    }

}
