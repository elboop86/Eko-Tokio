package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Producto;
import com.example.model.Usuario;
import com.example.service.UsuarioService;
import com.example.service.ProductoService;
import com.example.service.UploadFileService;
import com.example.service.UsuarioServiceImpl;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UploadFileService upload;

    @GetMapping("/show")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "administrador/productos/show";
    }
    private List<Producto> productos = new ArrayList<>(); // Lista para almacenar productos
    @GetMapping("/create")
    public String create(Model model) {
        // ultimo retoque
       // Producto Producto = new Producto(0,"nombre", "descripcion", "imagen", 1250, 50);
      // productos.add(Producto);
      //  model.addAttribute("productos", productos);

        return "administrador/productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Este es el objeto producto {}",producto);


        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();
        producto.setUsuario(u);

        //imagen
        if (producto.getId()==null) { // cuando se crea un producto
            String nombreImagen= upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }else {

        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto= new Producto();
        Optional<Producto> optionalProducto=productoService.get(id);
        producto= optionalProducto.get();

        LOGGER.info("Producto buscado: {}",producto);
        model.addAttribute("producto", producto);

        return "administrador/productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file ) throws IOException {
        Producto p= new Producto();
        p=productoService.get(producto.getId()).get();

        if (file.isEmpty()) { // editamos el producto pero no cambiamos la imagem

            producto.setImagen(p.getImagen());
        }else {// cuando se edita tbn la imagen

            //eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }
            String nombreImagen= upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Producto p = new Producto();
        p=productoService.get(id).get();

        //eliminar cuando no sea la imagen por defecto
        if (!p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }


}