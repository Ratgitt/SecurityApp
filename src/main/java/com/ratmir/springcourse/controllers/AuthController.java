package com.ratmir.springcourse.controllers;

import com.ratmir.springcourse.dto.AuthenticationDTO;
import com.ratmir.springcourse.dto.PersonDTO;
import com.ratmir.springcourse.models.Person;
import com.ratmir.springcourse.services.AuthenticationService;
import com.ratmir.springcourse.util.exceptions.PersonNotRegisteredException;
import com.ratmir.springcourse.util.responses.AuthenticationErrorResponse;
import com.ratmir.springcourse.util.responses.AuthenticationResponse;
import com.ratmir.springcourse.util.validators.PersonValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PersonValidator personValidator;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> performLogin(
            HttpServletResponse response,
            @RequestBody @Valid AuthenticationDTO authDTO
    ) throws NoSuchAlgorithmException {

        Person person = new Person(authDTO.getUsername(), authDTO.getPassword());

        AuthenticationResponse authResponse = authenticationService.authenticate(response, person); // this method throws BadCredentialsException

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            HttpServletResponse response,
            @RequestBody @Valid PersonDTO personDTO,
            BindingResult bindingResult
    ) throws NoSuchAlgorithmException {

        Person person = modelMapper.map(personDTO, Person.class);
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(error -> errorMessage.append(error.getField())
                                                .append(": ")
                                                .append(error.getDefaultMessage())
                                                .append("; "));
            throw new PersonNotRegisteredException(errorMessage.toString());
        }

        AuthenticationResponse registerResponse = authenticationService.register(response, person);

        return ResponseEntity.ok(registerResponse);
    }

    @ExceptionHandler({PersonNotRegisteredException.class, BadCredentialsException.class, NoSuchAlgorithmException.class})
    private ResponseEntity<AuthenticationErrorResponse> handleAuthenticationException(RuntimeException ex) {
        AuthenticationErrorResponse response = new AuthenticationErrorResponse(
                ex.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    private ResponseEntity<AuthenticationErrorResponse> handleAuthenticationException(DisabledException ex) {
        AuthenticationErrorResponse response = new AuthenticationErrorResponse(
                "Account is disabled. Activation link was sent to your email.",
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}