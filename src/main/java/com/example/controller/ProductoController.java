package com.example.controller;

import com.example.model.Producto;
import com.example.model.Usuario;
import com.example.service.ProductoService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductoController.class);
    // Método para guardar un producto nuevo, Logger es para registrar mensajes de un componente a un sistema o aplicación.
    @Autowired
    private ProductoService productoService;
    // Método mostrar producto
    @GetMapping("")
    public String show() {
        return "productos/show";
    }

    // Método crear producto
    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }
// Método guardar producto
    @PostMapping ("/save")
    public String save(Producto producto) {
        LOGGER.info("Mensaje correcto");
        Usuario u = new Usuario(1,"","","","","","","");
        producto.setUsuario(u);
        productoService.save(producto);
        return "redirect:/productos";
    }
}
