package io.test.config.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import io.test.util.LoginUser;
import io.test.util.StaticWebUtils;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        final Map<String, Object> additionalInfo = new HashMap<>();
//
//        additionalInfo.put("sessinId", StaticWebUtils.getCurrentRequest().getSession().getId());
//        additionalInfo.put("authorities", loginUser.getAuthorities());
//
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

}