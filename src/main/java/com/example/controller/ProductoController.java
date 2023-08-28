package com.example.controller;

import com.example.model.Producto;
import com.example.model.Usuario;
import com.example.service.ProductoService;
import com.example.service.UploadFileService;
import com.example.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;



@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER= LoggerFactory.getLogger(ProductoController.class);
    // Método para guardar un producto nuevo, Logger es para registrar mensajes de un componente a un sistema o aplicación.

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UploadFileService upload;

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
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Este es el objeto del producto {}", producto);


        Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        producto.setUsuario(u);

        // imagen
        if(producto.getId()==null) {
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        } else {

        }
        productoService.save(producto);
        return "redirect:/productos";
    }
    // Método editar producto
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto=productoService.get(id);
        producto= optionalProducto.get();

        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto); //Envia a la vista el objeto buscado
        return "productos/edit";
    }
    // Método subir producto
    @PostMapping("/update")
    public String update(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
        Producto p = new Producto();
        p = productoService.get(producto.getId()).get();
        //imagen

            if (file.isEmpty()) { // cuando editamos el producto pero no cambiamos la imagen

                producto.setImagen(p.getImagen());
            } else { // el caso de que si cambio la imagen cuando edito el producto
                // elimina la imagen mientras no sea la imagen default que esta por defecto
                if(p.getImagen().equals("default.jpg")) {
                    upload.deleteImage(p.getImagen());
                }
                String nombreImagen = upload.saveImage(file);
                producto.setImagen(nombreImagen);
            }

        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }

    // Método subir producto
    @GetMapping("/delete/{id")
    public String delete(@PathVariable Integer id ) {
        Producto p= new Producto();
        p= productoService.get(id).get();

        // elimina la imagen mientras no sea la imagen default que esta por defecto
        if(p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }
}