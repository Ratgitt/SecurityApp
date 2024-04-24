package com.ratmir.springcourse.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    @PreAuthorize("hasRole('ADMIN')")
    public void doAdminStuff() {
        System.out.println("Only admin here");
    }
}
