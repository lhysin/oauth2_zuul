package io.test.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthorize {

    public boolean checkBeforeSmsAuthorize() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isConfirmsSmsAuthExists = false;
        for(GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
                isConfirmsSmsAuthExists = true;
                break;
            }

        return isConfirmsSmsAuthExists;
    }

}
