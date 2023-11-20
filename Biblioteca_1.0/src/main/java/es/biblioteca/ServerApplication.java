package es.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ServerApplication {
	

	public static void main(String[] args) {
		
		System.out.println("");
		System.out.println("Servicio Rest -> Cargando el contexto de Spring...");
		SpringApplication.run(ServerApplication.class, args);
		System.out.println("");
		System.out.println("Servicio Rest -> Contexto de Spring cargado");
		System.out.println("");
	}

}
