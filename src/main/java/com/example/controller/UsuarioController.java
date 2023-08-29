package com.example.controller;

import com.example.model.Usuario;
import com.example.service.UsuarioService;
import com.mysql.cj.x.protobuf.MysqlxCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    @Autowired
    private UsuarioService usuarioService;

    // usuario/registro
    @GetMapping("/registro")
    public String create() {
        return "usuario/registro";
    }
    @PostMapping("/save")
    public String save(Usuario usuario) {
        logger.info("Usuario registro: {}", usuario);
        usuario.setTipo("USER");
        usuarioService.save(usuario);
        return "redirect:/";
    }
    // usuario Login
    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }
    @PostMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session) {
        logger.info("Accesos: {}", usuario);

        // Obtener usuario que tenga este email.
        Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
        logger.info("Usuario de base de datos: {}", user.get());

        // si esta presente el usuario se crea la sesion.
        if(user.isPresent()) {
            session.setAttribute("idusuario", user.get().getId());
            if(user.get().getTipo().equals("ADMIN")) {
                return "redirect:/administrador";
            }else {
                return "redirect:/";
            }
        } else {
            logger.info("Usuario no existe");
        }
        return "redirect:/";
    }

    @GetMapping("/compras")
    public String obtenerCompras(Model model, HttpSession session) {
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "usuario/compras";

    }

}
