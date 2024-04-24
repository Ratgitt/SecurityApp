package com.ratmir.springcourse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
    private String username;

    @Min(value = 1900, message = "Age should not be lower that 1900")
    private int yearOfBirth;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 2, max = 100, message = "Password should be between 2 and 100 characters")
    private String password;

    @NotEmpty(message = "Email field should not be empty")
    @Email(message = "Incorrect email format")
    private String email;
}