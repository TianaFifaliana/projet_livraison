package com.example.livraison.repository;

import com.example.livraison.entity.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Optional<Client> findByUserId(Long userId);

    boolean existsByEmail(String email);
}
