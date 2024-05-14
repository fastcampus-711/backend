package com.aptner.v3.security.service;

import com.aptner.v3.user.domain.User;
import com.aptner.v3.user.dto.CustomUserDetailsDto;
import com.aptner.v3.user.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
    private final UserDetailsRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (userData != null) {
            return new CustomUserDetailsDto(userData);
        }

        return null;
    }
}
