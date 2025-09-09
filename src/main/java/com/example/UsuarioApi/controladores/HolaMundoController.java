package com.example.UsuarioApi.controladores;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaMundoController {

    @GetMapping("/hola")
    public String holaMundo() {
        return "¡Hola Mundo desde Spring Boot! get";
    }


    @PostMapping("/hola")
    public String holaMundo2() {
        return "¡Hola Mundo desde Spring Boot! post";
    }
    @DeleteMapping("/hola")
    public String holaMundo3() {
        return "¡Hola Mundo desde Spring Boot! delete";
    }
    @PutMapping("/hola")
    public String holaMundo4() {
        return "¡Hola Mundo desde Spring Boot! put";
    }

}
