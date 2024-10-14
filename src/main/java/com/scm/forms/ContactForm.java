package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;
    @NotBlank(message = "PhoneNumber is required")
    @Pattern(regexp = "^[0-9]{10}$",message = "Inavlid Phone Number")
    private String phoneNumber;
    private String address;
    private String description;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;
    @ValidFile
    private MultipartFile contactPic;
    public boolean getFavorite() {
        return favorite;
    }
    private String picture;
    
}
