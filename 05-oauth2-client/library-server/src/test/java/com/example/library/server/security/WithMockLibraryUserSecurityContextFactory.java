package com.example.library.server.security;

import com.example.library.server.business.UserResource;
import com.example.library.server.common.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.UUID;

final class WithMockLibraryUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockLibraryUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockLibraryUser withUser) {

    ArrayList<Role> roles = new ArrayList<>();
    roles.add(Role.USER);
    LibraryUser principal =
        new LibraryUser(
            new UserResource(
                UUID.randomUUID(),
                "test@example.com",
                "Hans",
                "Mustermann", roles
                ));

    for (String role : withUser.roles()) {
      principal.getUserResource().getRoles().add(Role.valueOf(role));
    }

    setIdentifier(withUser, principal);
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            principal, principal.getPassword(), principal.getAuthorities());
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    return context;
  }

  private void setIdentifier(WithMockLibraryUser withUser, LibraryUser principal) {
    if (StringUtils.isBlank(withUser.identifier())) {
      principal.getUserResource().setId(UUID.randomUUID());
    } else {
      principal.getUserResource().setId(UUID.fromString(withUser.identifier()));
    }
  }

}
