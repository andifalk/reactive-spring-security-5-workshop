package com.example.library.server.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class GrantedAuthoritiesGroupExtractor extends JwtAuthenticationConverter {
    private static final String WELL_KNOWN_SCOPE_ATTRIBUTE_NAME = "groups";
    private static final String SCOPE_AUTHORITY_PREFIX = "SCOPE_";


    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        return this.getScopes(jwt)
                .stream()
                .map(authority -> SCOPE_AUTHORITY_PREFIX + authority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Collection<String> getScopes(Jwt jwt) {
        Object scopes = jwt.getClaims().get(WELL_KNOWN_SCOPE_ATTRIBUTE_NAME);
        if (scopes instanceof String) {
            if (StringUtils.hasText((String) scopes)) {
                return Arrays.asList(((String) scopes).split(" "));
            } else {
                return Collections.emptyList();
            }
        } else if (scopes instanceof Collection) {
            return (Collection<String>) scopes;
        }

        return Collections.emptyList();
    }
}
