package com.ratmir.springcourse.repositories;

import com.ratmir.springcourse.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OpaqueTokenDsl;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByActivationCode(String activationCode);
    Optional<Person> findByEmail(String email);
}
