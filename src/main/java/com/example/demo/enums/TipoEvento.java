package com.example.demo.enums;

public enum TipoEvento {
	 MEDICAL("🩺", "#52c41a"),      // Citas médicas
	 SYMPTOM("🤢", "#eb2f96"),      // Síntomas
	 REMINDER("💊", "#fa8c16"),     // Recordatorios
	 APPOINTMENT("📅", "#1890ff"),  // Citas generales
	 CLASS("🎓", "#722ed1"),        // Clases prenatales
	 TEST("🧪", "#13c2c2"),         // Exámenes
	 ULTRASOUND("👶", "#eb2f96"),   // Ecografías
	 OTHER("📌", "#666666");        // Otros
	 
	 private final String icono;
	 private final String color;
	 
	 TipoEvento(String icono, String color) {
	     this.icono = icono;
	     this.color = color;
	 }
	 
	 public String getIcono() { return icono; }
	 public String getColor() { return color; }
	}
