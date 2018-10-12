package com.example.library.server.config;

import com.example.library.server.security.LibraryUserAuthenticationConverter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.StringUtils;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final ResourceServerProperties resource;
    private final LibraryUserAuthenticationConverter libraryUserAuthenticationConverter;

    public ResourceServerConfiguration(ResourceServerProperties resource, LibraryUserAuthenticationConverter libraryUserAuthenticationConverter) {
        this.resource = resource;
        this.libraryUserAuthenticationConverter = libraryUserAuthenticationConverter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/books").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/books").hasRole("CURATOR")
                .antMatchers(HttpMethod.DELETE, "/books").hasRole("CURATOR")
                .antMatchers("/users/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

    @Primary
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(libraryJwtTokenEnhancer());
    }

    @Primary
    @Bean
    public JwtAccessTokenConverter libraryJwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String keyValue = this.resource.getJwt().getKeyValue();
        if (StringUtils.hasText(keyValue) && !keyValue.startsWith("-----BEGIN")) {
            converter.setSigningKey(keyValue);
        }
        converter.setVerifierKey(keyValue);
        converter.setAccessTokenConverter(libraryAccessTokenConverter());
        return converter;
    }

    @Bean
    AccessTokenConverter libraryAccessTokenConverter() {
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(libraryUserAuthenticationConverter);
        return defaultAccessTokenConverter;
    }
}
