package com.example.demo.enums;

public enum EstadoEvento {
	 PENDING("Pendiente"),
	 COMPLETED("Completado"),
	 CANCELLED("Cancelado"),
	 RECORDED("Registrado");
	 
	 private final String nombre;
	 
	 EstadoEvento(String nombre) {
	     this.nombre = nombre;
	 }
	 
	 public String getNombre() { return nombre; }
	}