package com.example.service;

import com.example.model.Orden;
import com.example.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenServiceImpl implements OrdenService{
    @Autowired
    private OrdenRepository ordenRepository;
    @Override
    public Orden save(Orden orden) {
        return ordenRepository.save(orden);
    }
}
