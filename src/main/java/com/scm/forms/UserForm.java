package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {

    @NotBlank(message = "Username is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email address")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min=6,message = "minimum 6 characters required")
    private String password;
    @Size(min = 8,max = 12,message = "Invalid Phone number")
    private String phoneNumber;
    private String about;
}
