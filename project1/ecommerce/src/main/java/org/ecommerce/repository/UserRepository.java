package org.ecommerce.repository;

import org.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>
{
    @Query(value = """
            SELECT *
                FROM user
                    WHERE email = :email
    """, nativeQuery = true)
    Optional<User> findByEmail(String email);


    @Query(value = """
            SELECT COUNT(*) >0
                FROM user
                    WHERE email = :email
                  """, nativeQuery = true)
    long existsByEmail(String email);
}

