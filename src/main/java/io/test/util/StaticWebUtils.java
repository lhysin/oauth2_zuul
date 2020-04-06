package io.test.util;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticWebUtils {

    public static LoginUser getCurrentLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginUser = null;
        if (authentication != null) {
            Object obj = authentication.getPrincipal();
            if (obj instanceof LoginUser) {
                loginUser = LoginUser.class.cast(obj);
            }
        }

        return loginUser;
    }

    public static String getCurrentLoginAdminId() {

        String adminId = null;

        LoginUser loginUser = StaticWebUtils.getCurrentLoginUser();
        if (loginUser != null) {
            adminId = loginUser.getAdminId();
        }

        return adminId;
    }

    public static boolean isAjax(HttpServletRequest request) {
        boolean isAjax = false;
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if(StringUtils.containsIgnoreCase(accept, MediaType.APPLICATION_JSON_VALUE) ||
                StringUtils.containsIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE)) {
            isAjax = true;
        }
        return isAjax;
    }

    /**
     * NOT UNIT TESTED Returns the URL (including query parameters) minus the
     * scheme, host, and context path. This method probably be moved to a more
     * general purpose class.
     */
    public static String getRelativeUrl() {

        HttpServletRequest request = StaticWebUtils.getCurrentRequest();

        String baseUrl = null;

        if ((request.getServerPort() == 80) || (request.getServerPort() == 443)) {
            baseUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        } else {
            baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
        }

        StringBuffer buf = request.getRequestURL();

        if (request.getQueryString() != null) {
            buf.append("?");
            buf.append(request.getQueryString());
        }

        return buf.substring(baseUrl.length());
    }

    /**
     * NOT UNIT TESTED Returns the base url (e.g, <tt>http://myhost:8080/myapp</tt>)
     * suitable for using in a base tag or building reliable urls.
     */
    public static String getBaseUrl() {

        HttpServletRequest request = StaticWebUtils.getCurrentRequest();

        if ((request.getServerPort() == 80) || (request.getServerPort() == 443)) {
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        } else {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
        }
    }

    /**
     * Returns the file specified by <tt>path</tt> as returned by
     * <tt>ServletContext.getRealPath()</tt>.
     */
    public static File getRealFile(HttpServletRequest request, String path) {
        return new File(request.getSession().getServletContext().getRealPath(path));
    }

    public static HttpServletRequest getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.currentRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance).map(ServletRequestAttributes.class::cast)
                .orElseThrow(() -> new RuntimeException("StaticWebUtils getCurrentRequest() current request is null"))
                .getRequest();
    }

    public static String getCurrentIp() {

        try {

            HttpServletRequest request = StaticWebUtils.getCurrentRequest();

            String ip = request.getHeader("X-Forwarded-For");

            if (StringUtils.isEmpty(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        } catch (RuntimeException e) {
            return StaticWebUtils.getHostAddress();
        }
    }

    public static String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e1) {
            return "127.0.0.1";
        }
    }

    private static HttpHeaders getHeadersInfo() {

        HttpServletRequest request = StaticWebUtils.getCurrentRequest();
        HttpHeaders httpHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            httpHeaders.add(key, value);
        }
        return httpHeaders;
    }

    // javascript from encodeURIComponent(btoa('encode'))
    public static String decodeBase64(String encode) {
        String returnStr = encode;
        if (StringUtils.isEmpty(encode)) {
            return returnStr;
        }
        try {
            return new String(Base64.getDecoder().decode(returnStr));
        } catch (Exception e) {
            return returnStr;
        }
    }

    public static HttpHeaders getHeadersInfo(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            httpHeaders.add(key, value);
        }
        return httpHeaders;
    }

    public static void addGrantedAuthority(String authority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> grantedAuthorityList = Lists.newArrayList();
        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            grantedAuthorityList.add(grantedAuthority);
        }
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + authority));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), grantedAuthorityList);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public static List<GrantedAuthority> createGratedAuthorityList(String... roles) {
        return Arrays.stream(roles)
                .map(role -> {
                    if(!StringUtils.contains(role, "ROLE_")) {
                        role = "ROLE_" + role;
                    }
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toList());
    }

    public static boolean containsAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .anyMatch(auth ->
                    StringUtils.equals(auth.getAuthority(), "ROLE_" + authority));
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
