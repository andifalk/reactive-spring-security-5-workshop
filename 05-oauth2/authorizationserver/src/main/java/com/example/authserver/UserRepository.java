package com.example.authserver;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link User users}.
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findOneByEmail(String email);
}
