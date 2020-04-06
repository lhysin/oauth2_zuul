package io.test.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

//    private final ResourceServerTokenServices tokenServices;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
//    private final SessionRegistry sessionRegistry;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources
//        .resourceId("admin")
//        .accessDeniedHandler(customAccessDeniedHandler)
//        .authenticationEntryPoint(customAuthenticationEntryPoint)
//        .tokenServices(tokenServices)
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.cors().disable()
            .csrf().disable()
            .anonymous().disable();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .maximumSessions(-1)
//            .sessionRegistry(sessionRegistry)
            ;

        http
            .authorizeRequests()
//                .antMatchers("/oauth/**", "/actuator/**").permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .exceptionHandling()
//                .accessDeniedHandler(customAccessDeniedHandler)
//                .authenticationEntryPoint(customAuthenticationEntryPoint)
                ;
        // @formatter:on

    }

}