package io.test.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.test.util.LoginUser;
import io.test.util.StaticWebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {

        LoginUser loginUser = new LoginUser(adminId, "{noop}test",
                StaticWebUtils.createGratedAuthorityList("beforeSMS"));

        return loginUser;
    }

}
