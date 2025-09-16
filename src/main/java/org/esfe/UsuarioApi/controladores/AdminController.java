package org.esfe.UsuarioApi.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Ejemplo: dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(@RequestHeader("X-User-Role") String rol) {
        if (!rol.equalsIgnoreCase("admin")) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }
        return ResponseEntity.ok("Bienvenido al Panel de Administración");
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> usuarios(@RequestHeader("X-User-Role") String rol) {
        if (!rol.equalsIgnoreCase("admin")) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }
        // Aquí llamarías al microservicio de usuarios (via FeignClient o RestTemplate)
        return ResponseEntity.ok("Listado de usuarios (futuro consumo de microservicio)");
    }



    @GetMapping("/roles")
    public ResponseEntity<?> roles(@RequestHeader("X-User-Role") String rol) {
        if (!rol.equalsIgnoreCase("admin")) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }
        // Aquí llamarías al microservicio de roles
        return ResponseEntity.ok("Listado de roles (futuro consumo de microservicio)");
    }
}


