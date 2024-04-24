package com.ratmir.springcourse.util.validators;

import com.ratmir.springcourse.dto.ChangePasswordRequestDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChangePasswordRequestValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ChangePasswordRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ChangePasswordRequestDTO request = (ChangePasswordRequestDTO) target;

        if(request.getNewPassword() != null && request.getConfirmationPassword() != null &&
                !request.getNewPassword().equals(request.getConfirmationPassword())) {
            errors.rejectValue("newPassword","" , "New and confirmation passwords don't match");
        }
    }
}
