package com.example.controller;

import com.example.model.Producto;
import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductoController.class);
    // Método para guardar un producto nuevo
    @GetMapping("")
    public String show() {
        return "productos/show";
    }
    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping ("/save")
    public String save(Producto producto) {
        return "redirect:/productos";
    }
}
