package com.example.demo.controller;

import com.example.demo.dto.MamaProfileRequest;
import com.example.demo.model.MamaProfile;
import com.example.demo.model.User;
import com.example.demo.repository.MamaProfileRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MamaProfileRepository mamaProfileRepository;

    // 👉 Mostrar formulario
    @GetMapping("/formulario-mama")
    public String formularioMama(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        User usuario = userRepository.findById(userId).orElse(null);
        if (usuario == null) return "redirect:/login";

        // ✅ VALIDACIÓN CLAVE
        if (mamaProfileRepository.existsByUserId(userId)) {
            return "redirect:/chatbot";
        }

        MamaProfileRequest dto = new MamaProfileRequest();
        dto.setNombre(usuario.getFirstName());
        dto.setSemanas(usuario.getGestationWeek());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mamaProfileRequest", dto);

        return "formulario-mama";
    }


    // 👉 Guardar formulario
    @PostMapping("/formulario-mama")
    public String guardarFormularioMama(
            @ModelAttribute("mamaProfileRequest") MamaProfileRequest form,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        User usuario = userRepository.findById(userId).orElse(null);
        if (usuario == null) return "redirect:/login";

        // ✅ VALIDACIÓN CLAVE
        if (mamaProfileRepository.existsByUserId(userId)) {
            return "redirect:/chatbot";
        }

        MamaProfile perfil = new MamaProfile();
        perfil.setUser(usuario);

        perfil.setNombre(form.getNombre());
        perfil.setEdad(form.getEdad());
        perfil.setSemanas(form.getSemanas());
        perfil.setPeso(form.getPeso());
        perfil.setTalla(form.getTalla());
        perfil.setMedicacion(form.getMedicacion());
        perfil.setActividad(form.getActividad());
        perfil.setHorasSueno(form.getHorasSueno());

        perfil.setCondiciones(join(form.getCondiciones()));
        perfil.setSintomas(join(form.getSintomas()));
        perfil.setHabitos(join(form.getHabitos()));
        perfil.setSuplementos(join(form.getSuplementos()));

        perfil.setAlergias(form.getAlergias());
        perfil.setNoGusta(form.getNoGusta());

        mamaProfileRepository.save(perfil);

        return "redirect:/chatbot";
    }


    private String join(String[] arr) {
        if (arr == null || arr.length == 0) return "";
        return String.join(", ", arr);
    }
}
