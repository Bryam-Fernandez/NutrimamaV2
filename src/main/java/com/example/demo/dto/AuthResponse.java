package com.example.demo.dto;

public class AuthResponse {

    private String message;
    private Long userId;
    private String firstName;
    private String lastName;
    private Integer gestationWeek;  

    public AuthResponse() {}

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(String message, Long userId, String firstName, String lastName, Integer gestationWeek) {
        this.message = message;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gestationWeek = gestationWeek; // ← AGREGADO
    }

    public String getMessage() { 
        return message; 
    }
    public void setMessage(String message) { 
        this.message = message; 
    }

    public Long getUserId() { 
        return userId; 
    }
    public void setUserId(Long userId) { 
        this.userId = userId; 
    }

    public String getFirstName() { 
        return firstName; 
    }
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    public Integer getGestationWeek() {  
        return gestationWeek;
    }
    public void setGestationWeek(Integer gestationWeek) {  
        this.gestationWeek = gestationWeek;
    }
}
