package com.aptner.v3.user.service;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.custom.CustomException;
import com.aptner.v3.user.domain.UserEntity;
import com.aptner.v3.user.dto.SignUpUserDto;
import com.aptner.v3.user.repository.SignUpUserRepository;
import com.aptner.v3.user.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SignUpUserRepository signUpUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUp(SignUpUserDto.Request request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String passwordConfirm = request.getPasswordConfirm();

        passwordMatch(password, passwordConfirm);

        signUpUserRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION);
                });

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .roles(Role.valueOf(request.getRole()))
                .build();

        signUpUserRepository.save(userEntity);
    }


    public void passwordMatch(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION);
        }
    }


    public void logout() {

    }
}
