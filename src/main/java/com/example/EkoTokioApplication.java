package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EkoTokioApplication {

	public static void main(String[] args) {
		SpringApplication.run(EkoTokioApplication.class, args);
	}


	/*// Producto
		ProductoRepository productoRepo = context.getBean(ProductoRepository.class);

		Producto producto1 = new Producto(null,"Lechuga","fresca","*",12,1250);
		Producto producto2 = new Producto(null,"Lechuga","fresca","*",12,1250);
		Producto producto3 = new Producto(null,"Lechuga","fresca","*",12,1250);

		*/
}
