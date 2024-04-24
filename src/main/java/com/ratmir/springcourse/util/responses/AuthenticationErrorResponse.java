package com.ratmir.springcourse.util.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AuthenticationErrorResponse {
    private String message;
    private Date timestamp;
}