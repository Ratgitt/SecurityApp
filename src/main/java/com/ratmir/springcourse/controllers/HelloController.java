package com.ratmir.springcourse.controllers;

import com.ratmir.springcourse.security.PersonDetails;
import com.ratmir.springcourse.services.AdminService;
import com.ratmir.springcourse.util.exceptions.NotActivatedException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class HelloController {

    private final AdminService adminService;

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint!");
    }

    @GetMapping("/showUserInfo")
    public ResponseEntity<String> showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if(!personDetails.isEnabled()) {
            throw new NotActivatedException("Activate your account to see info");
        }

        System.out.println(personDetails.getPerson());
        return ResponseEntity.ok("{\n"
                + "    username: " + personDetails.getUsername() + ",\n"
                + "    yearOfBirth: " + personDetails.getPerson().getYearOfBirth() + ",\n"
                + "    email: " + personDetails.getPerson().getEmail() + "\n"
                + "}");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminPage() {

        adminService.doAdminStuff();
        return ResponseEntity.ok("Hello, admin!");
    }

    @ExceptionHandler(NotActivatedException.class)
    private ResponseEntity<String> handleNotActivatedException(NotActivatedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}