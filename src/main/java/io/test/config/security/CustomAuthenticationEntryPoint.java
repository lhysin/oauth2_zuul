package io.test.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.test.exception.ErrorType;
import io.test.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String method = request.getMethod();
        String uri = request.getRequestURL().toString();
        log.trace("CustomAuthorize.checkFullAuthorize() false user access denied for : " + uri + " and method : " + method);

        ResponseUtils.createErrorResponseEntityAndPopulate(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED_EXCEPTION, response);
    }
}
