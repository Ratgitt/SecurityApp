package com.ratmir.springcourse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
    private String username;

    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Age should not be lower that 1900")
    private int yearOfBirth;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 2, max = 100, message = "Password should be between 2 and 100 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @NotEmpty(message = "Email field should not be empty")
    @Email(message = "Incorrect email format")
    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    public Person(String username, String password) {
        this.username = username;
         this.password = password;
    }
}
