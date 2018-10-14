package com.example.authserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * User entity acting as {@link UserDetails}.
 */
@Entity
public class User extends AbstractPersistable<Long> implements UserDetails {

    @JsonProperty("id")
    private UUID identifier;
    private String firstname;
    private String lastname;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @JsonIgnore
    private String password;

    public User() {
        super();
    }

    public User(UUID identifier, String firstname, String lastname, String email, String password, List<String> roles) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @JsonIgnore
    @Override
    public Long getId() {
        return super.getId();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("id", getId())
                .append("identifier", identifier)
                .append("firstname", firstname)
                .append("lastname", lastname)
                .append("email", email)
                .append("roles", roles)
                .append("password", "****")
                .toString();
    }
}
