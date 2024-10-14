package com.scm.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.scm.entities.Contact;
import com.scm.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact,String>{
    // find contacts by user
    Page<Contact> findByUser(User user, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.id= :userId")
    List<Contact> findByUserId(@Param("userId") String userId);
    Optional<Contact> findByContactId(String id);

    Page<Contact> findByUserAndNameContaining(User user, String name, Pageable pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumber, Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user, String email, Pageable pageable);
    Page<Contact> findByUserAndFavouriteTrue(User user, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Contact c WHERE c.contactId = :contactId")
    void deleteByContactId(@Param("contactId") String contactId);

}
