package com.example.service;

import com.example.model.Producto;

import java.util.List;
import java.util.Optional;

//Defino los métodos save,get, update, delete y findAll().
public interface ProductoService {
    Producto save(Producto producto);
    Optional<Producto> get(Integer id);
    void update(Producto producto);
    void delete(Integer id);

    List<Producto> findAll();
}
