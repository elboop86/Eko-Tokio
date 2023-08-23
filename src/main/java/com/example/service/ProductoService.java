package com.example.service;

import com.example.model.Producto;

import java.util.Optional;

//Defino los métodos
public interface ProductoService {
    public Producto save(Producto producto);
    public Optional<Producto> get(Integer id);
    public void update(Producto producto);
    public void delete(Integer id);
}
