package com.example.controller;

import com.example.model.Producto;
import com.example.model.Usuario;
import com.example.service.ProductoService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping; // puesto por probar

import java.util.Optional;
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
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
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
    // Método editar producto
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto=productoService.get(id);
        producto= optionalProducto.get();

        LOGGER.info("Producto buscado: {}");
        model.addAttribute("producto", producto); //Envia a la vista el objeto buscado
        return "productos/edit";
    }
    // Método subir producto
@PostMapping("/update")
    public String update(Producto producto) {
        productoService.update(producto);
        return "redirect:/productos";
    }

    // Método subir producto
    @GetMapping("/delete/{id")
    public String delete(@PathVariable Integer id) {
        productoService.delete(id);
        return "redirect:/productos";
    }
}
