package com.ratmir.springcourse.controllers;

import com.ratmir.springcourse.dto.ChangePasswordRequestDTO;
import com.ratmir.springcourse.services.AuthenticationService;
import com.ratmir.springcourse.services.PeopleService;
import com.ratmir.springcourse.util.exceptions.ActivationErrorException;
import com.ratmir.springcourse.util.responses.ActivationResponse;
import com.ratmir.springcourse.util.responses.ChangePasswordResponse;
import com.ratmir.springcourse.util.validators.ChangePasswordRequestValidator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer Authentication")
public class PeopleController {

    private final PeopleService peopleService;
    private final ChangePasswordRequestValidator passwordValidator;
    private final AuthenticationService authenticationService;

    @GetMapping("/activate/{code}")
    public ResponseEntity<ActivationResponse> activate(@PathVariable("code") String code) {

        // Throws ActivationErrorException
        ActivationResponse response = authenticationService.activatePerson(code);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordRequestDTO request,
            BindingResult bindingResult,
            Principal connectedPerson
    ) {
        passwordValidator.validate(request, bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(error -> errorMessage.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; "));
            throw new IllegalArgumentException(errorMessage.toString());
        }

        ChangePasswordResponse response = peopleService.changePassword(request, connectedPerson);

        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(ActivationErrorException.class)
    private ResponseEntity<ActivationResponse> handleActivationErrorException(ActivationErrorException ex) {
        ActivationResponse response = new ActivationResponse(
                ex.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ChangePasswordResponse> handlePasswordsDontMatchException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
                ChangePasswordResponse.builder()
                        .message(ex.getMessage())
                        .responseDate(new Date())
                        .build()
        );
    }
}
