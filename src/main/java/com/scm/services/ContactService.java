package com.scm.services;

import java.util.List;
import org.springframework.data.domain.Page;
import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
    Contact saveContact(Contact contact);
    Contact updateContact(Contact contact);
    List<Contact> getAllContacts();
    Contact getContactById(String id);
    void deleteContact(String id);
    List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(User user ,int page ,int size, String sortField, String sortBy);
    Page<Contact> searchByName(String name,int page ,int size, String sortField, String sortBy,User user);
    Page<Contact> searchByEmail(String email, int page ,int size, String sortField, String sortBy, User user);
    Page<Contact> searchByPhonenumber(String phoneNumber, int page ,int size, String sortField, String sortBy, User user);
    Page<Contact> searchByFavourite(int page, int size, String sortBy, User user);
}
