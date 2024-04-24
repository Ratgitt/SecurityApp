package com.ratmir.springcourse.services;

import com.ratmir.springcourse.dto.ChangePasswordRequestDTO;
import com.ratmir.springcourse.models.Person;
import com.ratmir.springcourse.repositories.PeopleRepository;
import com.ratmir.springcourse.security.PersonDetails;
import com.ratmir.springcourse.util.responses.ChangePasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Person> loadPersonByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }


    @Transactional
    public ChangePasswordResponse changePassword(ChangePasswordRequestDTO request, Principal connectedPerson) {

        PersonDetails personDetails = (PersonDetails) ((UsernamePasswordAuthenticationToken)connectedPerson).getPrincipal();

        if(!passwordEncoder.matches(request.getCurrentPassword(), personDetails.getPassword())) {
            throw new IllegalArgumentException("Wrong current password");
        }

        if(request.getCurrentPassword().equals(request.getNewPassword())) {
            return ChangePasswordResponse.builder()
                    .message("Current and new passwords are the same")
                    .responseDate(new Date())
                    .build();
        }

        Optional<Person> foundPerson = peopleRepository.findByUsername(personDetails.getUsername());
        foundPerson.ifPresent(person ->
                person.setPassword(
                        passwordEncoder.encode(request.getNewPassword())
                )
        );

        return ChangePasswordResponse.builder()
                .message("Password changed successfully")
                .responseDate(new Date())
                .build();
    }
}
