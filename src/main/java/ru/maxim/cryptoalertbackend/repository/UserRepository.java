package ru.maxim.cryptoalertbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.maxim.cryptoalertbackend.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("from User u where u.id = :id")
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String username);
}
