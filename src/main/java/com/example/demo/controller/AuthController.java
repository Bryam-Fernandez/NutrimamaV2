package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("El correo ya está registrado"));
        }

       
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                encryptedPassword,
                request.getGestationWeek()
        );

        User saved = userRepository.save(user);

        return ResponseEntity.ok(
                new AuthResponse(
                        "Usuario creado correctamente",
                        saved.getId(),
                        saved.getFirstName(),
                        saved.getLastName(),
                        saved.getGestationWeek()
                )
        );
    }

    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpSession session) {

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Correo o contraseña incorrectos"));
        }

        User user = userOpt.get();

       
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Correo o contraseña incorrectos"));
        }

       
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());

        return ResponseEntity.ok(
                new AuthResponse(
                        "Login correcto",
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getGestationWeek()
                )
        );
    }
}
