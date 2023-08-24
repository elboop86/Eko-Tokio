package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Subir una imagen o archivo
@Service
public class UploadFileService {
    private String folder="images//";
    // bucle if: si esta bien transforma el archivo imagen en bytes y la guarda Files,si vacío sale imagen default.
    public String saveImage(MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            byte [] bytes=file.getBytes();
            Path path = Path.of(folder+file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        return "default.jpg";
    }

    // creo método delete, elimina la imgen cuando se borra un producto.

    public void deleteImage(String nombre) {
        String ruta="images//";
        File file = new File(ruta+nombre);
        file.delete();
    }
}
