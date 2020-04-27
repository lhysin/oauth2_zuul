package io.test.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import io.test.util.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthApiController {

//    @SuppressWarnings("rawtypes")
//    private final FindByIndexNameSessionRepository findByIndexNameSessionRepository;
    private final SessionRegistry sessionRegistry;
    private final TokenStore tokenStore;

    // 테스트
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<Object> listLoggedInUsers(OAuth2Authentication authentication, HttpSession session) {
        SecurityContextHolder.getContext().getAuthentication();
        
        OAuth2AccessToken oAuth2AccessToken = tokenStore.getAccessToken(authentication);
        
        List<OAuth2AccessToken> oAuth2AccessTokenList = Lists.newArrayList(tokenStore.findTokensByClientIdAndUserName("admin-client", authentication.getName()));
        
        List<OAuth2Authentication> oAuth2AuthenticationList = oAuth2AccessTokenList.stream()
                .map(token -> tokenStore.readAuthentication(token))
                .collect(Collectors.toList());
        
        List<Object> objectList = oAuth2AuthenticationList.stream()
            .map(auth -> auth.getPrincipal())
            .collect(Collectors.toList());
        
        List<SessionInformation> sessionInformationList = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
        
        return ResponseEntity.ok(oAuth2AuthenticationList);
    }

    @PreAuthorize("@customAuthorize.checkBeforeSmsAuthorize()")
    @GetMapping("/chg/pwd")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> changePassword() {
        return ResponseEntity.ok("success!!");
    }

    @PreAuthorize("@customAuthorize.checkBeforeSmsAuthorize()")
    @PostMapping("/send/sms")
    @ResponseStatus(HttpStatus.OK)
    public void sendSms() {
    }

    @PreAuthorize("@customAuthorize.checkBeforeSmsAuthorize()")
    @PutMapping("/verify/sms/{authNo}")
    @ResponseStatus(HttpStatus.OK)
    public void verifySms(@PathVariable("authNo") String authNo, HttpSession httpSession) {
    }

    @PreAuthorize("@customAuthorize.checkBeforeSmsAuthorize()")
    @GetMapping("/me")
    public ResponseEntity<Object> me() {
        return ResponseEntity.ok(ImmutableMap.of(
                "loginUser", ImmutableMap.of(
                    "adminId", "superAdmin", 
                    "adminNm", "슈퍼관리자",
                    "accessMenus", Lists.newArrayList(
                        ImmutableMap.of("menuCd", "DASHBOARD", "read", "true", "write", "true"),
                        ImmutableMap.of("menuCd", "PARTNER_MANAGE", "read", "true", "write", "false"),
                        ImmutableMap.of("menuCd", "PARTNER_DETAIL", "read", "true", "write", "false")
                        ))
                )
            );
    }

    @PreAuthorize("@customAuthorize.checkBeforeSmsAuthorize()")
    @PutMapping("/success/sms")
    @ResponseStatus(HttpStatus.OK)
    public void successSms(HttpSession httpSession) {
    }

    @PreAuthorize("@customAuthorize.checkBeforeSmsAuthorize()")
    @GetMapping("/refresh-cookie")
    @ResponseStatus(HttpStatus.OK)
    public void refreshCookie(HttpSession httpSession, LoginUser loginUser) {
        log.debug("refresh cookie success. admin ID : " + loginUser.getAdminId());
    }

//    private void checkDuplicateLogin(String adminId) {
//
//        List<Session> sessionList = this.findAllSessionListWithoutCurrentSession(adminId);
//        for(Session session : sessionList) {
//
//            UsernamePasswordAuthenticationToken token = this.convertSessionToUsernamePasswordAuthenticationToken(session);
//            if(token != null) {
//
//                boolean isSuccessSmsAuthExists = token.getAuthorities()
//                        .stream()
//                        .anyMatch(auth ->  StringUtils.equals(auth.getAuthority(),
//                                "ROLE_SUCCESS_SMS_AUTH"
//                                ));
//
//                if(isSuccessSmsAuthExists) {
//                    throw new BusinessException(ErrorType.ALREADY_ANOTHER_LOGIN, adminId);
//                }
//            }
//        }
//    }
//
//    private void forceDuplicateLogin(String adminId) {
//
//        for(Session session : this.findAllSessionListWithoutCurrentSession(adminId)) {
//            UsernamePasswordAuthenticationToken token = this.convertSessionToUsernamePasswordAuthenticationToken(session);
//            if(token != null) {
//
//                boolean isSuccessSmsAuthExists = token.getAuthorities()
//                        .stream()
//                        .anyMatch(auth ->  StringUtils.equals(auth.getAuthority(),
//                                "ROLE_SUCCESS_SMS_AUTH"
//                                ));
//
//                if(isSuccessSmsAuthExists) {
//                    this.changeGrantedAuthority(session, token.getPrincipal(), token.getCredentials(), Lists.newArrayList(), AdminFoConstants.SECURITY_DETECTED_ANOTHER_LOGIN);
//                }
//            }
//        }
//    }
//
//    private @NotNull List<Session> findAllSessionListWithoutCurrentSession(String adminId){
//
//        HttpServletRequest request = StaticWebUtils.getCurrentRequest();
//        String currentSessionId = request.getSession().getId();
//
//        List<Session> sessions = Lists.newArrayList();
//        for(Object obj : findByIndexNameSessionRepository.findByPrincipalName(adminId).values()) {
//            if(Session.class.isInstance(obj)) {
//                sessions.add(Session.class.cast(obj));
//            }
//        }
//
//        List<SessionInformation> sessionInfoListWithoutCurrentSessionId = sessionRegistry.getAllSessions(adminId, false)
//                .stream()
//                .filter(info -> !StringUtils.equals(currentSessionId, info.getSessionId()))
//                .filter(info -> !info.isExpired())
//                .collect(Collectors.toList());
//
//        return sessions.stream()
//                .filter(se -> !se.isExpired())
//                .filter(se -> sessionInfoListWithoutCurrentSessionId
//                        .stream()
//                        .anyMatch(info -> StringUtils.equals(info.getSessionId(), se.getId()))
//                       )
//                .filter(se -> !StringUtils.equals(currentSessionId, se.getId()))
//                .collect(Collectors.toList());
//    }
//
//    private UsernamePasswordAuthenticationToken convertSessionToUsernamePasswordAuthenticationToken(Session session) {
//
//        UsernamePasswordAuthenticationToken token = null;
//
//        Object obj = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
//
//        if(SecurityContextImpl.class.isInstance(obj)) {
//
//            SecurityContextImpl securityContextImpl = Optional.ofNullable(SecurityContextImpl.class.cast(obj))
//                    .orElseThrow(() -> new NullPointerException("AuthApiController convertSessionToUsernamePasswordAuthenticationToken() : securityContextImpl is null."));
//
//            Authentication authentication = securityContextImpl.getAuthentication();
//
//            if(UsernamePasswordAuthenticationToken.class.isInstance(authentication)) {
//                token = UsernamePasswordAuthenticationToken.class.cast(authentication);
//            }
//        }
//
//        return token;
//    }
//
//    private void changeGrantedAuthority(Session session, Object principal, Object credentials, Collection<? extends GrantedAuthority> oldAuthorites, String newAuthority) {
//
//        if(session == null) {
//            return;
//        }
//
//        List<GrantedAuthority> grantedAuthorityList = Lists.newArrayList();
//        for (GrantedAuthority grantedAuthority : oldAuthorites) {
//            grantedAuthorityList.add(grantedAuthority);
//        }
//        grantedAuthorityList.add(new SimpleGrantedAuthority(AdminFoConstants.SECURITY_ROLE_PREFIX + newAuthority));
//
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, credentials, grantedAuthorityList);
//
//        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, new SecurityContextImpl(newAuth));
//    }
}
