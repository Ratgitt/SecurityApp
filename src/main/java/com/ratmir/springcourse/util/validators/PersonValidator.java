package com.ratmir.springcourse.util.validators;

import com.ratmir.springcourse.models.Person;
import com.ratmir.springcourse.services.PeopleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Person person = (Person) target;

        Optional<Person> foundByUsername = peopleService.loadPersonByUsername(person.getUsername());
        if(foundByUsername.isPresent()) {
            errors.rejectValue("username", "", "User with this username is already exists");
        }

        Optional<Person> foundByEmail = peopleService.findByEmail(person.getEmail());
        if(foundByEmail.isPresent()) {
            errors.rejectValue("email", "", "User with this email is already exists");
        }
    }
}
