package com.example.controller;

import com.example.model.Orden;
import com.example.model.Usuario;
import com.example.service.OrdenService;
import com.example.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    HttpSession session;

    BCryptPasswordEncoder passEncode= new BCryptPasswordEncoder(); //Método para el password

    // usuario/registro
    @GetMapping("/registro") // redirecciona bien
    public String create() {

        return "administrador/usuario/registro";
    }
    @PostMapping("/save")
    public String save(Usuario usuario) {
        logger.info("Usuario registro: {}", usuario);
        usuario.setTipo("USER");
        usuario.setPassword(passEncode.encode(usuario.getPassword())); // Aquí se incripta el password del usuario
        usuarioService.save(usuario);
        return "redirect:/";
    }
    // usuario Login
    @GetMapping("/login") //redirecciona bien
    public String login() {
        if (session.getAttribute("idusuario") != null) {
            Optional<Usuario> user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
            this.acceder(user, session);
            return "redirect:/";
        } else {
            return "administrador/usuario/login";
        }
    }
    @GetMapping("/acceder")
    public String acceder(Optional<Usuario> usuario, HttpSession session) {
        logger.info("Accesos: {}", usuario);

        // Obtener usuario que tenga este email.
        Optional<Usuario> user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
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
        return "administrador/usuario/acceder"; // originalmente tenía "redirect:/"
    }

    @GetMapping("/compras") // redirecciona bien
    public String obtenerCompras(Model model, HttpSession session) {
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        List<Orden> ordenes = ordenService.findByUsuario(usuario);
        logger.info("ordenes", ordenes);

        model.addAttribute("ordenes", ordenes);

        return "administrador/usuario/compras";

    }

    @GetMapping("detalle/{id}")
    public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
        logger.info("Id de la orden: {}", id);
        Optional<Orden> orden = ordenService.findById(id);

        model.addAttribute("detalles", orden.get().getDetalle());
        // session
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "administrador/usuario/detalleCompra";
    }
// cerrar sesion del usuario
    @GetMapping("/cerrar")
    public String cerrarSesion( HttpSession session) {
       session.removeAttribute("idusuario");
       return "redirect:/";
    }


}
