package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Long> {
}
