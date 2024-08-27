package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Set<Message> findByRecepteurIdOrEmetteurId(Long emetteur, Long recepteur);
}
