package io.test.config.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FrameworkEndpoint
@RequiredArgsConstructor
public class RevokeTokenEndpoint {

    private final TokenStore tokenStore;
    private final BearerTokenExtractor bearerTokenExtractor;

    @DeleteMapping("/oauth/revoke")
    @ResponseBody
    public void revokeToken(HttpServletRequest request) {

        Authentication authentication = bearerTokenExtractor.extract(request);
        if(authentication != null) {
            String tokenValue = authentication.getPrincipal().toString();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            if (accessToken != null) {
                tokenStore.removeAccessToken(accessToken);
                log.debug(accessToken + " is revoked.");
            }
        }
    }

}
