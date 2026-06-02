package com.example.demo.repository;

import com.example.demo.model.MamaProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MamaProfileRepository extends JpaRepository<MamaProfile, Long> {

    
    Optional<MamaProfile> findByUserId(Long userId);

   
    boolean existsByUserId(Long userId);
}
