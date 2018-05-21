package com.example.library.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockLibraryUserSecurityContextFactory.class)
public @interface WithMockLibraryUser {

  /**
   * Convenience mechanism for specifying the username. The default is "user". If {@link
   * #username()} is specified it will be used instead of {@link #value()}
   *
   * @return username
   */
  String value() default "user";

  /**
   * Technical id of the user.
   *
   * @return id
   */
  long id() default 1L;

  /**
   * The username to be used. Note that {@link #value()} is a synonym for {@link #username()}, but
   * if {@link #username()} is specified it will take precedence.
   *
   * @return username
   */
  String username() default "user";

  /**
   * The roles to use. The default is "USER". A {@link GrantedAuthority} will be created for each
   * value within roles. Each value in roles will automatically be prefixed with "ROLE_". For
   * example, the default will result in "ROLE_USER" being used.
   *
   * @return the roles
   */
  String[] roles() default {"USER"};

  /**
   * The first name to be used. The default is "".
   *
   * @return the first name
   */
  String firstName() default "";

  /**
   * The last name to be used. The default is "".
   *
   * @return the last name
   */
  String lastName() default "";

  /**
   * The email to be used. The default is "".
   *
   * @return the email
   */
  String email() default "";

  /**
   * The UUID to be used.
   *
   * @return the UUID as string
   */
  String identifier() default "";
}
