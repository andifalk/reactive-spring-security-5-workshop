package com.example.library.server.security;

import com.example.library.server.business.UserResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class LibraryUser implements UserDetails {

    private final UserResource userResource;

    public LibraryUser(UserResource userResource) {
        this.userResource = userResource;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils
                .commaSeparatedStringToAuthorityList(
                        userResource.getRoles().stream()
                                .map(rn -> "ROLE_" + rn.name())
                                .collect(Collectors.joining(",")));
    }

    @Override
    public String getPassword() {
        return "n/a";
    }

    @Override
    public String getUsername() {
        return userResource.getEmail();
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

    public UserResource getUserResource() {
        return userResource;
    }
}
