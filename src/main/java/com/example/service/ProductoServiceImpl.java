package com.example.service;

import com.example.model.Producto;
import com.example.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service                    // Métodos CRUD
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private ProductoRepository productoRepository;
    @Override // Implementar Método save
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override // Implementar método get
    public Optional<Producto> get(Integer id) {
        return productoRepository.findById(id);
    }

    @Override // Implementar método subir
    public void update(Producto producto) { productoRepository.save(producto);

    }

    @Override // Implementar método delete
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }
}
