package com.example.demo.dto;

import lombok.Data;

@Data
public class MamaProfileRequest {
	
	private String nombre;
    private Integer edad;
    private Integer semanas;

    private Double peso;
    private Integer talla;

    private String medicacion;
    private String actividad;
    private Integer horasSueno;

    // Checkboxes (arrays)
    private String[] condiciones;
    private String alergias;  // textarea → String
    private String[] sintomas;
    private String[] habitos;
    private String noGusta;   // textarea → String
    private String[] suplementos;



    public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public String getAlergias() {
	    return alergias;
	}

	public void setAlergias(String alergias) {
	    this.alergias = alergias;
	}

	public Integer getSemanas() {
		return semanas;
	}
	public void setSemanas(Integer semanas) {
		this.semanas = semanas;
	}
	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Integer getTalla() {
		return talla;
	}
	public void setTalla(Integer talla) {
		this.talla = talla;
	}
	public String getMedicacion() {
		return medicacion;
	}
	public void setMedicacion(String medicacion) {
		this.medicacion = medicacion;
	}
	public String getActividad() {
		return actividad;
	}
	public void setActividad(String actividad) {
		this.actividad = actividad;
	}
	public Integer getHorasSueno() {
		return horasSueno;
	}
	public void setHorasSueno(Integer horasSueno) {
		this.horasSueno = horasSueno;
	}
	public String[] getCondiciones() {
		return condiciones;
	}
	public void setCondiciones(String[] condiciones) {
		this.condiciones = condiciones;
	}
	public String[] getSintomas() {
		return sintomas;
	}
	public void setSintomas(String[] sintomas) {
		this.sintomas = sintomas;
	}
	public String[] getHabitos() {
		return habitos;
	}
	public void setHabitos(String[] habitos) {
		this.habitos = habitos;
	}
	public String[] getSuplementos() {
		return suplementos;
	}
	public void setSuplementos(String[] suplementos) {
		this.suplementos = suplementos;
	}
	public String getNoGusta() {
	    return noGusta;
	}

	public void setNoGusta(String noGusta) {
	    this.noGusta = noGusta;
	}

	
	public MamaProfileRequest(String nombre, Integer edad, Integer semanas, Double peso, Integer talla,
			String medicacion, String actividad, Integer horasSueno, String[] condiciones, String alergias,
			String[] sintomas, String[] habitos, String noGusta, String[] suplementos) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.semanas = semanas;
		this.peso = peso;
		this.talla = talla;
		this.medicacion = medicacion;
		this.actividad = actividad;
		this.horasSueno = horasSueno;
		this.condiciones = condiciones;
		this.alergias = alergias;
		this.sintomas = sintomas;
		this.habitos = habitos;
		this.noGusta = noGusta;
		this.suplementos = suplementos;
	}
	
	//Metodo vacio
	public MamaProfileRequest() {
		
	}
}
	
