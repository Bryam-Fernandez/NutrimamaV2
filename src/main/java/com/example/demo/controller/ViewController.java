package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // 👉 INICIO SIEMPRE EN MAIN
    @GetMapping("/")
    public String root() {
        return "main";
    }

    // 👉 MAIN
    @GetMapping("/main")
    public String main() {
        return "main";
    }

    // 👉 LOGIN
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 👉 REGISTER
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // 👉 LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
