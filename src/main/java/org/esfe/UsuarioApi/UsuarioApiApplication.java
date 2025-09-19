package org.esfe.UsuarioApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // ✅ Habilitado para Eureka en la nube
public class UsuarioApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApiApplication.class, args);
	}
}