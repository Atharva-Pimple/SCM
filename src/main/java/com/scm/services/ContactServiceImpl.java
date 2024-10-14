package com.scm.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepository;

import lombok.var;

@Service
public class ContactServiceImpl implements ContactService{
    
    @Autowired
    private ContactRepository repository;

    private Logger logger=LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public Contact saveContact(Contact contact) {
        String contactId=UUID.randomUUID().toString();
        contact.setContactId(contactId);
        return repository.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact) {

        repository.save(contact);
        
        
        return contact;
    }

    @Override
    public List<Contact> getAllContacts() {
        return repository.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact Not found with given id"));
    }

    // @Transactional
    // @Override
    // public void deleteContact(String id) {
    //     Contact contact=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact Not found with given id"));
        
    //     logger.info("Contact deleted {}",contact);
    //     System.out.println(contact.getEmail());
    //     repository.delete(contact);
    // }

    @Transactional
    @Override
    public void deleteContact(String id) {
        repository.deleteByContactId(id);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user,int page ,int size, String sortField, String sortBy) {

        Sort sort=sortBy.equals("desc")? Sort.by(sortField).descending(): Sort.by(sortField).ascending();

        var pageable=PageRequest.of(page, size, sort);

        return repository.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortField, String sortBy, User user) {
        Sort sort=sortBy.equals("desc")? Sort.by(sortField).descending(): Sort.by(sortField).ascending();

        var pageable=PageRequest.of(page, size, sort);

        return repository.findByUserAndNameContaining(user,name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortField, String sortBy, User user) {
        Sort sort=sortBy.equals("desc")? Sort.by(sortField).descending(): Sort.by(sortField).ascending();

        var pageable=PageRequest.of(page, size, sort);

        return repository.findByUserAndEmailContaining(user,email, pageable);
    }

    @Override
    public Page<Contact> searchByPhonenumber(String phoneNumber, int page, int size, String sortField, String sortBy, User user) {
        Sort sort=sortBy.equals("desc")? Sort.by(sortField).descending(): Sort.by(sortField).ascending();

        var pageable=PageRequest.of(page, size, sort);

        return repository.findByUserAndPhoneNumberContaining(user,phoneNumber, pageable);
    }

    @Override
    public Page<Contact> searchByFavourite(int page, int size, String sortBy, User user) {
        Sort sort= Sort.by("name").descending();

        var pageable=PageRequest.of(page, size, sort);

        return repository.findByUserAndFavouriteTrue(user, pageable);
    }

}
