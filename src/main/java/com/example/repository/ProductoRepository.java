package com.example.repository;


import com.example.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Spring Data JPA proporcionará automáticamente los métodos de búsqueda básicos
}
