package com.ratmir.springcourse.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequestDTO {

    @NotEmpty(message = "Field should not be empty")
    @Size(min = 2, max = 100, message = "Password should be between 2 and 100 characters")
    private String currentPassword;

    @NotEmpty(message = "Field should not be empty")
    @Size(min = 2, max = 100, message = "Password should be between 2 and 100 characters")
    private String newPassword;

    @NotEmpty(message = "Field should not be empty")
    @Size(min = 2, max = 100, message = "Password should be between 2 and 100 characters")
    private String confirmationPassword;
}