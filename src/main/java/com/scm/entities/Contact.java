package com.scm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    private String contactId;
    
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    @Column(length = 1000)
    private String discription;
    private boolean favourite;

    private String websiteLink;
    private String linkedInLink;

    private String cloudinaryImagePublicId;
    
    // @OneToMany(
    //     mappedBy = "contact",
    //     cascade = CascadeType.ALL,
    //     fetch = FetchType.EAGER,
    //     orphanRemoval = true
    // )
    // private List<SocialLink> links=new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private User user;

    public boolean getFavorite() {
        return this.favourite;
    }
}
