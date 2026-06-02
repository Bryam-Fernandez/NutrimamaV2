package com.example.demo.controller;

import com.example.demo.dto.ChatSessionDTO;
import com.example.demo.dto.SintomaRequest;
import com.example.demo.model.ChatMessage;
import com.example.demo.model.ChatSession;
import com.example.demo.model.MamaProfile;
import com.example.demo.model.User;
import com.example.demo.repository.MamaProfileRepository;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.ChatSessionRepository;

import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/chatbot")
public class ChatBotController {

	private final UserRepository userRepository;
	private final MamaProfileRepository mamaProfileRepository;
	private final ChatSessionRepository chatSessionRepository;
	private final ChatMessageRepository chatMessageRepository;

	public ChatBotController(UserRepository userRepository, MamaProfileRepository mamaProfileRepository,
			ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository) {
		this.userRepository = userRepository;
		this.mamaProfileRepository = mamaProfileRepository;
		this.chatSessionRepository = chatSessionRepository;
		this.chatMessageRepository = chatMessageRepository;
	}

	@PostMapping("/nueva")
	@ResponseBody
	public ChatSession nuevaConversacion(HttpSession session) {

	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) return null;

	    ChatSession chatSession = new ChatSession();
	    chatSession.setUserId(userId);
	    chatSession.setTitulo("Nueva conversación");
	    chatSession.setFechaCreacion(LocalDateTime.now());

	    chatSession = chatSessionRepository.save(chatSession);

	    
	    session.setAttribute("chatSessionId", chatSession.getId());

	    return chatSession;
	}


	@GetMapping("/session/{id}")
	@ResponseBody
	public Map<String, Object> abrirChat(@PathVariable Long id, HttpSession session) {

	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    ChatSession sessionDb = chatSessionRepository.findById(id).orElse(null);

	
	    if (sessionDb == null || !sessionDb.getUserId().equals(userId)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
	    }

	  
	    session.setAttribute("chatSessionId", id);

	    List<ChatMessage> mensajes =
	            chatMessageRepository.findBySessionIdOrderByFechaAsc(id);

	    Map<String, Object> response = new HashMap<>();
	    response.put("mensajes", mensajes);

	    return response;
	}


	/*
	 * ======================= VISTA DEL CHAT ========================
	 */
	@GetMapping
	public String showChat(Model model, HttpSession session) {
	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) return "redirect:/login";

	    User user = userRepository.findById(userId).orElse(null);
	    if (user == null) {
	        session.invalidate();
	        return "redirect:/login";
	    }

	    MamaProfile perfil = mamaProfileRepository.findByUserId(userId).orElse(null);
	    
	    List<ChatSession> sesiones = 
	        chatSessionRepository.findByUserIdOrderByFijadoDescFechaCreacionDesc(userId);
	    
	    // 🔥 SOLUCIÓN: Si no hay sesiones, crear una automáticamente
	    if (sesiones.isEmpty()) {
	        ChatSession nueva = new ChatSession();
	        nueva.setUserId(userId);
	        nueva.setTitulo("Nueva conversación");
	        nueva.setFechaCreacion(LocalDateTime.now());
	        nueva = chatSessionRepository.save(nueva);
	        session.setAttribute("chatSessionId", nueva.getId());
	        sesiones = List.of(nueva); // Actualizar la lista
	    }
	    // Si hay sesiones pero no hay una activa, usar la más reciente
	    else if (session.getAttribute("chatSessionId") == null) {
	        session.setAttribute("chatSessionId", sesiones.get(0).getId());
	    }
	    
	    model.addAttribute("usuarioNombre", user.getFirstName());
	    model.addAttribute("usuarioCorreo", user.getEmail());
	    model.addAttribute("sesiones", sesiones);
	    model.addAttribute("perfil", perfil);
	    
	    return "chatbot";
	}

	/*
	 * ======================= API DEL CHAT ========================
	 */
	@PostMapping("/sintoma")
	@ResponseBody
	public Map<String, Object> consultarSintoma(@RequestBody SintomaRequest request, HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");
		if (userId == null)
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión expirada");


		User user = userRepository.findById(userId).orElse(null);
		if (user == null)
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");


		MamaProfile profile = mamaProfileRepository.findByUserId(userId).orElse(null);

		if (profile == null) {
		    throw new ResponseStatusException(
		        HttpStatus.BAD_REQUEST,
		        "Por favor completa tu perfil antes de usar el chatbot 🤍"
		    );
		}


		String prompt = buildPrompt(profile, request);

		// 1️⃣ Ver si ya hay una sesión de chat activa
		Long chatSessionId = (Long) session.getAttribute("chatSessionId");
		ChatSession chatSession;
		
		if (chatSessionId == null) {
		    throw new ResponseStatusException(
		        HttpStatus.BAD_REQUEST,
		        "No hay conversación activa"
		    );
		}

		chatSession = chatSessionRepository.findById(chatSessionId)
		    .orElseThrow(() ->
		        new ResponseStatusException(
		            HttpStatus.NOT_FOUND,
		            "La conversación ya no existe"
		        )
		    );


		


		// Guardar mensaje del USUARIO (siempre)
		ChatMessage msgUser = new ChatMessage();
		msgUser.setSession(chatSession);
		msgUser.setRol("USER");
		msgUser.setContenido(request.getMensaje());
		msgUser.setFecha(LocalDateTime.now());

		chatMessageRepository.save(msgUser);
		
		if (chatSession.getTitulo().equals("Nueva conversación")) {

		    String titulo;

		    if (request.getSintomas() != null && !request.getSintomas().isEmpty()) {
		        titulo = request.getSintomas().get(0); // primer síntoma
		    } else {
		        titulo = request.getMensaje();
		    }

		    // limpiar y cortar
		    titulo = titulo.trim();
		    if (titulo.length() > 30) {
		        titulo = titulo.substring(0, 30) + "...";
		    }

		    chatSession.setTitulo(titulo);
		    chatSessionRepository.save(chatSession);
		}


		try {
			String respuesta = callOllama(prompt);

			ChatMessage msgBot = new ChatMessage();
			msgBot.setSession(chatSession);
			msgBot.setRol("BOT");
			msgBot.setContenido(respuesta);
			msgBot.setFecha(LocalDateTime.now());
			
			Map<String, Object> response = new HashMap<>();
			response.put("respuesta", respuesta);
			response.put("titulo", chatSession.getTitulo());
			response.put("sessionId", chatSession.getId());

			chatMessageRepository.save(msgBot);

			return response;


		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(
				    HttpStatus.INTERNAL_SERVER_ERROR,
				    "Ocurrió un error al consultar a MamaBot 🤖"
				);

		}

	}

	/*
	 * ======================= PROMPT INTELIGENTE ========================
	 */
	private String buildPrompt(MamaProfile profile, SintomaRequest request) {

		StringBuilder prompt = new StringBuilder();

		Integer semanas = profile.getSemanas() != null ? profile.getSemanas() : profile.getUser().getGestationWeek();

		// Contexto humano y natural
		prompt.append("Estás conversando con ").append(profile.getNombre()).append(", una mamá embarazada de ")
				.append(semanas).append(" semanas.\n");

		if (profile.getCondiciones() != null && !profile.getCondiciones().isBlank()) {
			prompt.append("Tiene en cuenta estas condiciones: ").append(profile.getCondiciones()).append(".\n");
		}

		if (profile.getAlergias() != null && !profile.getAlergias().isBlank()) {
			prompt.append("Debe evitar: ").append(profile.getAlergias()).append(".\n");
		}

		if (profile.getSuplementos() != null && !profile.getSuplementos().isBlank()) {
			prompt.append("Actualmente toma: ").append(profile.getSuplementos()).append(".\n");
		}

		List<String> sintomas = request.getSintomas();
		if (sintomas != null && !sintomas.isEmpty()) {
			prompt.append("Ahora mismo se siente con: ").append(String.join(", ", sintomas)).append(".\n");
		}

		prompt.append("\nElla dice:\n");
		prompt.append(request.getMensaje()).append("\n");

		prompt.append("""
				Respóndele como si ya estuvieran conversando.
				Habla de forma cercana, tranquila y comprensiva.
				Usa frases cortas y naturales (máx. 4–5 líneas).
				Evita listas largas y lenguaje técnico.
				""");

		return prompt.toString();
	}

	@Transactional
	@PostMapping("/session/{id}/delete")
	@ResponseBody
	public String eliminarChat(@PathVariable Long id, HttpSession session) {

	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    // 🔑 obtener la sesión de chat
	    ChatSession sessionDb = chatSessionRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    // 🔐 validar que el chat pertenece al usuario
	    if (!sessionDb.getUserId().equals(userId)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
	    }

	    // 🗑️ eliminar mensajes y sesión
	    chatMessageRepository.deleteBySessionId(id);
	    chatSessionRepository.deleteById(id);

	    // 🧹 limpiar sesión activa si era ese chat
	    Long activeSessionId = (Long) session.getAttribute("chatSessionId");
	    if (activeSessionId != null && activeSessionId.equals(id)) {
	        session.removeAttribute("chatSessionId");
	    }

	    return "ok";
	}
	
	@PostMapping("/session/{id}/fijar")
	@ResponseBody
	public void fijarChat(@PathVariable Long id, HttpSession session) {

	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null)
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

	    ChatSession chat = chatSessionRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    if (!chat.getUserId().equals(userId))
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN);

	    chat.setFijado(!chat.isFijado());
	    chatSessionRepository.save(chat);
	}
	
	@GetMapping("/sessions")
	@ResponseBody
	public List<ChatSessionDTO> getSessions(HttpSession session) {
	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }
	    
	    List<ChatSession> sesiones = 
	        chatSessionRepository.findByUserIdOrderByFijadoDescFechaCreacionDesc(userId);
	    
	    Long activeSessionId = (Long) session.getAttribute("chatSessionId");
	    
	    return sesiones.stream().map(s -> {
	        ChatSessionDTO dto = new ChatSessionDTO();
	        dto.setId(s.getId());
	        dto.setTitulo(s.getTitulo());
	        dto.setFijado(s.isFijado());
	        dto.setActiva(s.getId().equals(activeSessionId));
	        dto.setFechaCreacion(s.getFechaCreacion());
	        return dto;
	    }).collect(Collectors.toList());
	}



	private String callOllama(String userPrompt) throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();

		JSONObject body = new JSONObject();
		body.put("model", "gpt-oss:20b-cloud");
		body.put("temperature", 0.4);
		body.put("num_predict", 80);

		// JSONObject body = new JSONObject();
		// body.put("model", "llama3:latest");
		// body.put("temperature", 0.4);
		// body.put("num_predict", 120);

		JSONArray messages = new JSONArray();

		JSONObject system = new JSONObject();
		system.put("role", "system");
		system.put("content", """
				Eres MamaBot, una acompañante cálida y cercana durante el embarazo.
				Hablas como una persona real, con empatía y sencillez.
				Das consejos prácticos sin sonar médica ni técnica.
				""");

		JSONObject user = new JSONObject();
		user.put("role", "user");
		user.put("content", userPrompt);

		messages.put(system);
		messages.put(user);

		body.put("messages", messages);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:11434/v1/chat/completions"))
				.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body.toString()))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new IOException("Error Ollama: " + response.body());
		}

		JSONObject json = new JSONObject(response.body());
		return json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
	}

}
