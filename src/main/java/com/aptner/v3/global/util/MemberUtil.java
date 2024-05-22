package com.aptner.v3.global.util;

import com.aptner.v3.auth.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberUtil {
    private static Authentication authentication;

    public static String getUsername() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails)(authentication.getPrincipal())).getUsername();
    }

    public static long getMemberId() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
//        return ((CustomUserDetails)(authentication.getPrincipal())).getId();
        return 2;
    }
}
