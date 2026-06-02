// Archivo: ChatSessionDTO.java
package com.example.demo.dto;

import java.time.LocalDateTime;

public class ChatSessionDTO {
    private Long id;
    private String titulo;
    private boolean fijado;
    private boolean activa;
    private LocalDateTime fechaCreacion;
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public boolean isFijado() { return fijado; }
    public void setFijado(boolean fijado) { this.fijado = fijado; }
    
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}