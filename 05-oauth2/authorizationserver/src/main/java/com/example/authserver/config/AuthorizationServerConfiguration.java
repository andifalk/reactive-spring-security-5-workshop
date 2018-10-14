package com.example.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    AuthorizationServerConfiguration(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("library-client")
                .secret("secret")
                .authorizedGrantTypes("authorization_code")
                .scopes("profile")
                .redirectUris("http://localhost:8081/login/oauth2/code/login-client");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(this.tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(this.authenticationManager);
    }

    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwtdemo.jks"),
                "secret".toCharArray());
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(factory.getKeyPair("jwtdemo"));
        return jwtAccessTokenConverter;
    }

    @Bean
    TokenStore tokenStore() {
        return new JwtTokenStore(this.jwtAccessTokenConverter());
    }
}
