package com.tigeranalystics.userlifecycleserv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tigeranalystics.userlifecycleserv.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails, Serializable {

    private static final long serialversionUID = 129348938L;
    private Long userAccountNumber;
    private String username; // Changed from 'name' to 'username' for clarity
    @JsonIgnore
    private String password;
    private String email;
    private List<GrantedAuthority> authorities;

    public UserInfoDetails(UserInfo userInfo) {
        this.userAccountNumber= userInfo.getId();
        this.username = userInfo.getName(); // Assuming 'name' is used as 'username'
        this.password = userInfo.getPassword();
        this.email=userInfo.getEmail();
        this.authorities = List.of(userInfo.getRoles().split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement your logic if you need this
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement your logic if you need this
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement your logic if you need this
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true; // Implement your logic if you need this
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserAccountNumber() {
        return userAccountNumber;
    }

    public void setUserAccountNumber(Long userAccountNumber) {
        this.userAccountNumber = userAccountNumber;
    }
}
