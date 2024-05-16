package com.example.attendance.config.security;

import com.example.attendance.domain.member.entity.Role;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final String loginId;

    private final String password;

    private final Long memberId;

    private final Role role;

    @Builder
    public CustomUserDetails(String loginId, String password, Long memberId, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.memberId = memberId;
        this.role = role;
    }

    public static CustomUserDetails forSession(Long memberId, Role role) {
        return CustomUserDetails.builder()
                .memberId(memberId)
                .role(role)
                .build();
    }

    public static CustomUserDetails forLogin(String loginId, String password, Long memberId, Role role) {
        return CustomUserDetails.builder()
                .loginId(loginId)
                .password(password)
                .memberId(memberId)
                .role(role)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getKey()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getMemberId() {
        return memberId;
    }
}
