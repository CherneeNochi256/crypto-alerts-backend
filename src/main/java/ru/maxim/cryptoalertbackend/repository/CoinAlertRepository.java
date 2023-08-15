package ru.maxim.cryptoalertbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxim.cryptoalertbackend.entities.CoinAlert;
import ru.maxim.cryptoalertbackend.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinAlertRepository extends JpaRepository<CoinAlert,Long> {

    Optional<List<CoinAlert>> findAllByCoinReachedDesiredPrice(Boolean isReached);
    Optional<List<CoinAlert>> findAllByUser(User user);

    Optional<List<CoinAlert>> findAllBySeenAndUser(Boolean isSeen,User currentUser);
 }
