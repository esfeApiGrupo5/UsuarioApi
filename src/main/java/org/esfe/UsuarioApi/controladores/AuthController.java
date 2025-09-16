package org.esfe.UsuarioApi.controladores;

import org.esfe.UsuarioApi.dtos.UsuarioGuardarDto;
import org.esfe.UsuarioApi.dtos.UsuarioSalidaDto;
import org.esfe.UsuarioApi.seguridad.dtos.UsuarioLogin;
import org.esfe.UsuarioApi.seguridad.dtos.UsuarioToken;
import org.esfe.UsuarioApi.seguridad.servicios.JwtService;
import org.esfe.UsuarioApi.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Registro de usuario
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioSalidaDto> registrar(@Valid @RequestBody UsuarioGuardarDto usuarioDto) {
        UsuarioSalidaDto usuarioCreado = usuarioService.crear(usuarioDto);
        return ResponseEntity.ok(usuarioCreado);
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<UsuarioToken> login(@Valid @RequestBody UsuarioLogin loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getCorreo(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Obtener el usuario autenticado
        org.esfe.UsuarioApi.modelos.Usuario usuario = (org.esfe.UsuarioApi.modelos.Usuario) authentication.getPrincipal();
        String token = jwtService.getToken(usuario);
        UsuarioToken usuarioToken = new UsuarioToken(token);
        return ResponseEntity.ok(usuarioToken);
    }
}
