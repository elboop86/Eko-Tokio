package com.example.controller;

import com.example.model.DetalleOrden;
import com.example.model.Orden;
import com.example.model.Producto;
import com.example.model.Usuario;
import com.example.service.DetalleOrdenService;
import com.example.service.OrdenService;
import com.example.service.ProductoService;
import com.example.service.UsuarioService;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private DetalleOrdenService detalleOrdenService;


    // Almacena los detalles de la orden
    List<DetalleOrden> detalles = new ArrayList<>();

    Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        //HttpSession session = request.getSession();
        log.info("Session del usuario: {}", session.getAttribute("idusuario"));

        model.addAttribute("productos", productoService.findAll());

        // session

        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "administrador/usuario/home";
    }

    @GetMapping("/productohome/{id}")
    public String productoHome(@PathVariable Integer id,  Model model) {
        log.info("Id producto enviado como parámetro {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);


        return "administrador/usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        log.info("Product añadido: {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);
        producto = optionalProducto.get();
        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        // validar que el producto no se añada dos veces
        Integer idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId()== idProducto);

        if(!ingresado) {
            detalles.add(detalleOrden);
        }



        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        Orden orden = new Orden();  // Crear un nuevo objeto Orden
        orden.setTotal(sumaTotal);


        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "administrador/usuario/carrito";
    }

    // Quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model) {

        List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

        for (DetalleOrden detalleOrden : detalles) {
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNueva.add(detalleOrden);
            }
        }
        detalles = ordenesNueva;

        double sumaTotal = 0;

        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        Orden orden = new Orden();  // Crear un nuevo objeto Orden
        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "administrador/usuario/carrito";
    }

    @GetMapping("/getCart") // funciona se muestra bien pero sin resultados
    public String getCart(Model model, HttpSession session) {
        Orden orden = new Orden();  // Crear un nuevo objeto Orden
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        //sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "administrador/usuario/carrito";
    }

    @GetMapping("/order") //funciona pero no se muestra más que header y footer
    public String order(Model model, HttpSession session) {
        // obtengo el usuario
        Usuario usuario =usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        Orden orden = new Orden();  // Crear un nuevo objeto Orden
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);
        return "administrador/usuario/resumenorden";
    }
// guardar la orden
    @GetMapping("/saveOrder") // funciona pero no se muestra más que header y footer
    public String saveOrder(HttpSession session) {
        Date fechaCreacion = new Date();
        Orden orden = new Orden();  // Crear un nuevo objeto Orden
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());
        // Guarda la session de  usuario
        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        orden.setUsuario(usuario);
        ordenService.save(orden);

        //guardar detalles
        for(DetalleOrden dt: detalles) {
            dt.setOrden(orden);
            detalleOrdenService.save(dt);
        }
        // limpiarla lista y la orden
        orden = new Orden();
        detalles.clear();


        return "redirect:/";
    }

    //busqueda
    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model) {
        log.info("Nombre del producto: {}", nombre);
        List<Producto> productos= productoService.findAll().stream().filter(p ->p.getNombre().contains(nombre)).collect(Collectors.toList());

        model.addAttribute("productos", productos);
        return "administrador/usuario/home";
    }
}
