package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.TipoEvento;
import com.example.demo.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    // Eventos de un usuario en un rango de fechas
    List<Evento> findByUsuarioIdAndFechaBetweenOrderByFechaAscHoraAsc(
        Long usuarioId, LocalDate inicio, LocalDate fin);
    
    // Eventos de hoy
    @Query("SELECT e FROM Evento e WHERE e.usuario.id = :usuarioId AND e.fecha = CURRENT_DATE ORDER BY e.hora ASC")
    List<Evento> findHoyByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    // Próximos eventos (desde mañana)
    @Query("SELECT e FROM Evento e WHERE e.usuario.id = :usuarioId AND e.fecha > CURRENT_DATE ORDER BY e.fecha ASC, e.hora ASC")
    List<Evento> findProximosByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    // Eventos por tipo y fecha
    List<Evento> findByUsuarioIdAndTipoAndFecha(
        Long usuarioId, TipoEvento tipo, LocalDate fecha);
}