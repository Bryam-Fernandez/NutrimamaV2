package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChatSession {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer semana;

    private String titulo; 

    private LocalDateTime fechaCreacion;
    
    @Column(nullable = false)
    private boolean fijado = false;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public boolean isFijado() {
	    return fijado;
	}

	public void setFijado(boolean fijado) {
	    this.fijado = fijado;
	}
	
	public ChatSession(Long id, Long userId, Integer semana, String titulo, LocalDateTime fechaCreacion,
			boolean fijado) {
		super();
		this.id = id;
		this.userId = userId;
		this.semana = semana;
		this.titulo = titulo;
		this.fechaCreacion = fechaCreacion;
		this.fijado = fijado;
	}

	public ChatSession() {
		
	}
   
}
