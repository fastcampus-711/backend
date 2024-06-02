package com.aptner.v3.global.util;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.member.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberUtil {
    private static Authentication authentication;

    public static String getUsername() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails)(authentication.getPrincipal())).getUsername();
    }

    public static Member getMember() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getMember();
        }
        return null;
    }
}
