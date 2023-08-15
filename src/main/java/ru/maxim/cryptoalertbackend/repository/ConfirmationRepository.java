package ru.maxim.cryptoalertbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxim.cryptoalertbackend.entities.Confirmation;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation,Long> {
    Optional<Confirmation> findByToken(String token);
}
