package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findByUserIdOrderByFechaCreacionDesc(Long userId);
    List<ChatSession> findByUserIdOrderByFijadoDescFechaCreacionDesc(Long userId);
    Optional<ChatSession> findFirstByUserIdOrderByFechaCreacionDesc(Long userId);

}
