package com.scm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User saveUser(User user) {
        String userId=UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoleList(List.of(AppConstants.ROLE_USER));

        String emailToken=UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser=userRepository.save(user);

        String emailLink=Helper.getEmailVerificationLink(emailToken);
        emailService.sendEmail(
            user.getEmail(),
            "Verify Email: Smart Contact Manager",
            emailLink);

        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2=userRepository
            .findById(user.getUserId())
            .orElseThrow(()-> new ResourceNotFoundException());

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setProvider(user.getProvider());

        return Optional.ofNullable(userRepository.save(user2));

    }

    @Override
    public void deleteUser(String id) {
        User user=userRepository
            .findById(id)
            .orElseThrow(()-> new ResourceNotFoundException());

        userRepository.delete(user);
    }

    @Override
    public boolean isUserExist(String id) {
        User user=userRepository
            .findById(id)
            .orElse(null);

        return user!=null? true:false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user=userRepository
            .findByEmail(email)
            .orElse(null);

        return user!=null? true:false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
        
    }

    @Override
    public Optional<User> getUserByToken(String token) {
        return userRepository.findByEmailToken(token);
    }

    @Override
    public User saveUserEnable(User user) {
        return userRepository.save(user);
    }

    

}
