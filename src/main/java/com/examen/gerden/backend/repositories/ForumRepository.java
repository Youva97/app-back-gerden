package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {
}
