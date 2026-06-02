package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mama_profile")
public class MamaProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String nombre;
    private Integer edad;
    private Integer semanas;       

    private Double peso;           
    private Integer talla;         

    private String medicacion;     
    private String actividad;      
    private Integer horasSueno;    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	public String getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}

	public String getAlergias() {
		return alergias;
	}

	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}

	public String getSintomas() {
		return sintomas;
	}

	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}

	public String getHabitos() {
		return habitos;
	}

	public void setHabitos(String habitos) {
		this.habitos = habitos;
	}

	public String getNoGusta() {
		return noGusta;
	}

	public void setNoGusta(String noGusta) {
		this.noGusta = noGusta;
	}

	public String getSuplementos() {
		return suplementos;
	}

	public void setSuplementos(String suplementos) {
		this.suplementos = suplementos;
	}
	
	public MamaProfile(Long id, User user, String nombre, Integer edad, Integer semanas, Double peso, Integer talla,
			String medicacion, String actividad, Integer horasSueno, String condiciones, String alergias,
			String sintomas, String habitos, String noGusta, String suplementos) {
		super();
		this.id = id;
		this.user = user;
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
	
	public MamaProfile() {
		
	}


    // Listas que guardamos como texto separado por comas
    @Column(columnDefinition = "TEXT")
    private String condiciones;    // ["anemia"] → "anemia"

    @Column(columnDefinition = "TEXT")
    private String alergias;       // ["mariscos"] → "mariscos"

    @Column(columnDefinition = "TEXT")
    private String sintomas;       // ["nauseas, acidez"]

    @Column(columnDefinition = "TEXT")
    private String habitos;        // ["vegetariana"]

    @Column(columnDefinition = "TEXT")
    private String noGusta;        // ["yogurt"]

    @Column(columnDefinition = "TEXT")
    private String suplementos;    // ["hierro"]
}
