package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.ConversationPrivee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationPriveeRepository extends JpaRepository<ConversationPrivee, Long> {
}
