package com.example.controller;

import com.example.model.Orden;
import com.example.model.Producto;
import com.example.service.OrdenService;
import com.example.service.ProductoService;
import com.example.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private OrdenService ordenService;

    private Logger logg = LoggerFactory.getLogger(AdministradorController.class);
    @GetMapping("/administrador")
    public String home(Model model) {
        List<Producto> productos= productoService.findAll();
        model.addAttribute("productos", productos);

        return "administrador/administrador/home";
    }
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());

        return "administrador/administrador/usuarios";
    }
    @GetMapping("/ordenes")
    public String ordenes(Model model) {
        model.addAttribute("ordenes", ordenService.findAll());
        return "administrador/administrador/ordenes";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(Model model, @PathVariable Integer id) {
        logg.info("Id de la orden {}",id);
        Orden orden = ordenService.findById(id).get();

        model.addAttribute("detalles", orden.getDetalle());
      return "administrador/administrador/detalleorden";
    }
}
