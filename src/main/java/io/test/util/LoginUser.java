package io.test.util;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
//@EqualsAndHashCode(of = "adminId", callSuper=false)
public class LoginUser extends org.springframework.security.core.userdetails.User {

    private String adminId;
    private Integer passwdFailCnt;
    private LocalDate passwdChgDt;

    public LoginUser(String adminId, String password, List<GrantedAuthority> authorities) {
        super(adminId, password, authorities);
        this.adminId = adminId;
    }

    public void injectPasswdFailCnt(Integer passwdFailCnt) {
        this.passwdFailCnt = passwdFailCnt;
    }

    public void injectPasswdChgDt(LocalDate passwdChgDt) {
        this.passwdChgDt = passwdChgDt;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public Integer getPasswdFailCnt() {
        return this.passwdFailCnt;
    }

    public LocalDate getPasswdChgDt() {
        return this.passwdChgDt;
    }
}
