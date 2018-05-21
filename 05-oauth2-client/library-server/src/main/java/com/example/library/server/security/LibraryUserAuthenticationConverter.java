package com.example.library.server.security;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class LibraryUserAuthenticationConverter implements UserAuthenticationConverter {

  private static final String USER_NAME_ENTRY = "user_name";

  private static final String CLIENT_ID_ENTRY = "client_id";

  private static final Logger LOGGER =
      LoggerFactory.getLogger(LibraryUserAuthenticationConverter.class);

  private final UserDetailsService userDetailsService;

  public LibraryUserAuthenticationConverter(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
    Assert.notNull(userDetailsService, "userDetailsService must be set");
  }

  @Override
  public Map<String, ?> convertUserAuthentication(Authentication authentication) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put(USERNAME, authentication.getName());

    if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
      response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
    }
    return response;
  }

  @Override
  public Authentication extractAuthentication(Map<String, ?> map) {

    LOGGER.debug("Contents of JWT token {}", map);

    // Check if token had expected client id
    String clientValue = (String) map.get(CLIENT_ID_ENTRY);

    if (!clientValue.equalsIgnoreCase("library-client")) {
      LOGGER.warn("Invalid client id {} detected", clientValue);
      throw new BadClientCredentialsException();
    }

    if (map.containsKey(USER_NAME_ENTRY)) {
      UserDetails principal = getUserDetails((String) map.get(USER_NAME_ENTRY), map);
      return new UsernamePasswordAuthenticationToken(
          principal, "n/a", ((LibraryUser) principal).getAuthorities());
    }

    throw new BadClientCredentialsException();
  }

  private UserDetails getUserDetails(String userId, Map<String, ?> map) {
    UserDetails principal;
    try {
      principal = userDetailsService.loadUserByUsername(userId);
    } catch (UsernameNotFoundException ex) {
      // do not rethrow original exception (not reveal details to user)
      if (map == null) {
        LOGGER.warn("Could not load user", ex);
        throw new BadClientCredentialsException();
      } else {
        LOGGER.info("Could not load user. Register as new user.", ex);
        throw new BadClientCredentialsException();
      }
    }
    return principal;
  }

}
