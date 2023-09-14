package com.example.service;

import com.example.model.Orden;
import com.example.model.Usuario;
import com.example.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenServiceImpl implements OrdenService{
    @Autowired
    private OrdenRepository ordenRepository;

    @Override
    public List<Orden> findAll() {
        return ordenRepository.findAll();
    }

    @Override
    public Optional<Orden> findById(Integer id) {
        return ordenRepository.findById(id);
    }

    public String generarNumeroOrden() {
        int numero = 0;
        String numeroConcatenado = "";

        List<Orden> ordenes = findAll();

        List<Integer> numeros = new ArrayList<Integer>();

        ordenes.forEach(o -> {
            String numeroStr = o.getNumero();
            try {
                int numeroOrden = Integer.parseInt(numeroStr);
                numeros.add(numeroOrden);
            } catch (NumberFormatException e) {
                // Manejar el caso en el que el valor no sea numérico
                // Por ejemplo, puedes ignorar este valor o registrar un mensaje de error
                // dependiendo de tus requisitos
                // También puedes omitirlo de la lista de números si es necesario
            }
        });

        // La numeración de las ordenes hasta 10.000, se ordena un numero ordenado a mayor a cada orden realizada.
        if (ordenes.isEmpty()) {
            numero = 1;
        } else {
            numero = numeros.stream().max(Integer::compare).orElse(0); // Usar 0 si no hay números válidos
            numero++;
        }

        if (numero < 10) {
            numeroConcatenado = "000000000" + String.valueOf(numero);
        } else if (numero < 100) {
            numeroConcatenado = "00000000" + String.valueOf(numero);
        } else if (numero < 1000) {
            numeroConcatenado = "0000000" + String.valueOf(numero);
        } else if (numero < 10000) {
            numeroConcatenado = "000000" + String.valueOf(numero);
        }

        return numeroConcatenado;
    }



    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }

    @Override
    public Orden save(Orden orden) {
        return ordenRepository.save(orden);
    }
}
