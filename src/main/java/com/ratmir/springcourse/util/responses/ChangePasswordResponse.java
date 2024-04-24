package com.ratmir.springcourse.util.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ChangePasswordResponse {
    private String message;
    private Date responseDate;
}
