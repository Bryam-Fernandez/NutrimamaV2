package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.EventoDTO;
import com.example.demo.dto.EventoRequest;
import com.example.demo.dto.SintomaRequest;
import com.example.demo.enums.EstadoEvento;
import com.example.demo.enums.TipoEvento;
import com.example.demo.model.Evento;
import com.example.demo.model.User;
import com.example.demo.repository.EventoRepository;
import com.example.demo.repository.UserRepository;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/calendario")
public class CalendarioController {
    
    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;
    
    public CalendarioController(EventoRepository eventoRepository, UserRepository userRepository) {
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
    }
    
    // Obtener eventos del mes
    @GetMapping("/mes")
    @ResponseBody
    public Map<String, Object> getEventosMes(
            @RequestParam int year,
            @RequestParam int month,
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        LocalDate inicio = LocalDate.of(year, month, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        
        List<Evento> eventos = eventoRepository.findByUsuarioIdAndFechaBetweenOrderByFechaAscHoraAsc(
            userId, inicio, fin);
        
        Map<String, List<EventoDTO>> eventosPorDia = eventos.stream()
            .collect(Collectors.groupingBy(
                e -> e.getFecha().toString(),
                Collectors.mapping(this::convertirADTO, Collectors.toList())
            ));
        
        Map<String, Object> response = new HashMap<>();
        response.put("eventos", eventosPorDia);
        response.put("hoy", LocalDate.now().toString());
        
        return response;
    }
    
    // Obtener eventos de hoy
    @GetMapping("/hoy")
    @ResponseBody
    public List<EventoDTO> getEventosHoy(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        return eventoRepository.findHoyByUsuarioId(userId)
            .stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    // Obtener próximos eventos
    @GetMapping("/proximos")
    @ResponseBody
    public List<EventoDTO> getProximosEventos(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        return eventoRepository.findProximosByUsuarioId(userId)
            .stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    
    // Crear nuevo evento
    @PostMapping("/nuevo")
    @ResponseBody
    public EventoDTO crearEvento(@RequestBody EventoRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        User usuario = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        Evento evento = new Evento();
        evento.setTitulo(request.getTitulo());
        evento.setDescripcion(request.getDescripcion());
        evento.setTipo(request.getTipo());
        evento.setFecha(request.getFecha());
        evento.setHora(request.getHora());
        evento.setEstado(EstadoEvento.PENDING);
        evento.setUsuario(usuario);
        
        evento = eventoRepository.save(evento);
        
        return convertirADTO(evento);
    }
    
    // Registrar síntoma como evento
    @PostMapping("/registrar-sintoma")
    @ResponseBody
    public EventoDTO registrarSintoma(@RequestBody SintomaRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        User usuario = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        Evento evento = new Evento();
        evento.setTitulo("Síntoma: " + request.getSintomas());
        evento.setDescripcion(request.getDescripcion());
        evento.setTipo(TipoEvento.SYMPTOM);
        evento.setFecha(request.getFecha() != null ? request.getFecha() : LocalDate.now());
        evento.setHora(LocalTime.now());
        evento.setEstado(EstadoEvento.RECORDED);
        evento.setUsuario(usuario);
        
        evento = eventoRepository.save(evento);
        
        return convertirADTO(evento);
    }
    
    private EventoDTO convertirADTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setId(evento.getId());
        dto.setTitulo(evento.getTitulo());
        dto.setDescripcion(evento.getDescripcion());
        dto.setTipo(evento.getTipo());
        dto.setFecha(evento.getFecha());
        dto.setHora(evento.getHora());
        dto.setEstado(evento.getEstado());
        dto.setIcono(evento.getTipo().getIcono());
        dto.setColor(evento.getTipo().getColor());
        
        // Calcular días restantes
        if (evento.getFecha().isAfter(LocalDate.now())) {
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), evento.getFecha());
            dto.setDiasRestantes((int) dias);
        }
        
        return dto;
    }
}

