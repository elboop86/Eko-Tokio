package com.example.repository;

import com.example.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {
    // Spring Data JPA proporcionará automáticamente los métodos de búsqueda básicos
}
