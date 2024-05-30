package com.aptner.v3.global.util;

import com.aptner.v3.auth.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class MemberUtil {
    private static Authentication authentication;

    public static String getUsername() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails)(authentication.getPrincipal())).getUsername();
    }

    public static long getMemberId() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
//        return ((CustomUserDetails)(authentication.getPrincipal())).getId();
        return 1;
    }

    public static User getMember() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public static CustomUserDetails getMember1() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        return null;
    }
}
