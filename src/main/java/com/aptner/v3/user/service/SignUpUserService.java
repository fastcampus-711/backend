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

@Service
@RequiredArgsConstructor
public class SignUpUserService {

    private final SignUpUserRepository signUpUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUp(SignUpUserDto.Request request) {
        String username = request.getUsername();
        String password = request.getPassword();

        signUpUserRepository.findByUserName(username)
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION);
                });

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .role(Role.valueOf(request.getRole()))
                .build();

        signUpUserRepository.save(userEntity);
    }
}
