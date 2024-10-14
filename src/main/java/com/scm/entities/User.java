package com.scm.entities;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@Table(name = "users")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{

    @Id
    private String userId;
    @Column(name="user_name",nullable = false)
    private String name;
    private String phoneNumber;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;  
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String profilePic;
    // verification
    private boolean enabled=false;
    private boolean emailVerified=false; 
    private boolean phoneVerified=false;
    private String emailToken;

    // SELF,Google,Facebook,Github
    @Enumerated(value = EnumType.STRING)
    private Provider provider=Provider.SELF;
    private String providerUserId;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        orphanRemoval = true
    )
    private List<Contact> contacts=new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // List of roles(USER, ADMIN)
        Collection<SimpleGrantedAuthority> roles=roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }
}
