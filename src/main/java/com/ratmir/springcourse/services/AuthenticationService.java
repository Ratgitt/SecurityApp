package com.ratmir.springcourse.services;

import com.ratmir.springcourse.models.Person;
import com.ratmir.springcourse.models.Role;
import com.ratmir.springcourse.repositories.PeopleRepository;
import com.ratmir.springcourse.security.JwtService;
import com.ratmir.springcourse.util.exceptions.ActivationErrorException;
import com.ratmir.springcourse.util.responses.ActivationResponse;
import com.ratmir.springcourse.util.responses.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SmtpMailSender mailSender;

    @Transactional
    public AuthenticationResponse register(
            HttpServletResponse response,
            Person person
    ) throws BadCredentialsException, NoSuchAlgorithmException {
        // Шифрование пароля
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole(Role.ROLE_USER);
        person.setEnabled(false);
        person.setActivationCode(UUID.randomUUID().toString());
        peopleRepository.save(person);

        String message = String.format(
                "Hello, %s! \n" +
                        "Please, visit next link to activate you account: https://monkfish-app-n9pmx.ondigitalocean.app/users/activate/%s",
                person.getUsername(),
                person.getActivationCode()
        );
        mailSender.send(person.getEmail(), "Activation code", message);

        String jwtToken = jwtService.generateToken(response, person.getUsername());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .responseDate(new Date())
                .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(
            HttpServletResponse response,
            Person person
    ) throws BadCredentialsException, NoSuchAlgorithmException {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword());

        authenticationManager.authenticate(authToken);

        String jwtToken = jwtService.generateToken(response, person.getUsername());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .responseDate(new Date())
                .build();
    }

    @Transactional
    public ActivationResponse activatePerson(String code) throws ActivationErrorException {
        Optional<Person> foundPerson = peopleRepository.findByActivationCode(code);

        if(foundPerson.isEmpty()) {
            throw new ActivationErrorException("Invalid activation code");
        }

        Person person = foundPerson.get();
        person.setActivationCode(null);
        person.setEnabled(true);
        peopleRepository.save(person);

        return ActivationResponse.builder()
                .message("Your account is successfully activated")
                .responseDate(new Date())
                .build();
    }
}