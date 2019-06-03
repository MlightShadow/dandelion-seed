
package com.company.project.util;

import com.company.project.dto.AuthorizedInfo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {
    public AuthorizedInfo getAuthInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthorizedInfo) {
            return (AuthorizedInfo) principal;
        }
        return null;
    }
}