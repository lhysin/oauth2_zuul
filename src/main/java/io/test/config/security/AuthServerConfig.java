package io.test.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AccessTokenConverter accessTokenConverter;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);

//        security.accessDeniedHandler(customAccessDeniedHandler)
//        		.authenticationEntryPoint(customAuthenticationEntryPoint);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter:off
        clients.inMemory()
            .withClient("admin-client")
                .secret(passwordEncoder.encode("admin-secret"))
//                .secret("{noop}")
                .authorizedGrantTypes("password"
                        , "refresh_token" 
//                        ,"authorization_code", "implicit"
                        ) 
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .scopes(
                        "read", "write")
//                .resourceIds("admin")
                .accessTokenValiditySeconds(5)
                .refreshTokenValiditySeconds(10)
                ;
        // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .tokenStore(tokenStore)
            .tokenEnhancer(tokenEnhancer())
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
//            .accessTokenConverter(accessTokenConverter)
            .tokenStore(tokenStore)
            ;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

}

