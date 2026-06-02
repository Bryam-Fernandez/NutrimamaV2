package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ChatMessage;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySessionIdOrderByFecha(Long sessionId);
    List<ChatMessage> findBySessionIdOrderByFechaAsc(Long sessionId);
    void deleteBySessionId(Long sessionId);

}
