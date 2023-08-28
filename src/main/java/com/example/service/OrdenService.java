package com.example.service;

import com.example.model.Orden;

import java.util.List;

public interface OrdenService {
    List<Orden> findAll();
    Orden save(Orden orden);
    String generarNumeroOrden();
}
