package org.esfe.UsuarioApi.controladores;

import org.esfe.UsuarioApi.dtos.UsuarioGuardarDto;
import org.esfe.UsuarioApi.dtos.UsuarioSalidaDto;
import org.esfe.UsuarioApi.seguridad.dtos.UsuarioLogin;
import org.esfe.UsuarioApi.seguridad.dtos.UsuarioToken;
import org.esfe.UsuarioApi.seguridad.servicios.JwtService;
import org.esfe.UsuarioApi.seguridad.servicios.TokenBlacklistService;
import org.esfe.UsuarioApi.servicios.interfaces.IUsuarioService;
import org.esfe.UsuarioApi.repositorios.IUsuarioRepository;
import org.esfe.UsuarioApi.modelos.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private TokenBlacklistService tokenBlacklistService; // ← NUEVA DEPENDENCIA

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

    // ✅ NUEVO ENDPOINT DE LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Token no encontrado"));
        }

        String token = authHeader.substring(7);

        try {
            // Verificar que el token sea válido antes de invalidarlo
            String username = jwtService.getUsernameFromToken(token);

            if (username != null) {
                // Añadir el token a la lista negra
                tokenBlacklistService.blacklistToken(token);

                // Limpiar el contexto de seguridad
                SecurityContextHolder.clearContext();

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Sesión cerrada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Token inválido"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Error al cerrar sesión: " + e.getMessage()));
        }
    }

    // ✅ ENDPOINT DE VALIDACIÓN CORREGIDO
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Token no encontrado"));
        }

        String token = authHeader.substring(7);

        try {
            // ✅ VERIFICAR SI EL TOKEN ESTÁ EN LA LISTA NEGRA
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token ha sido invalidado"));
            }

            String username = jwtService.getUsernameFromToken(token);
            Long userId = jwtService.getUserIdFromToken(token);

            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(username);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Usuario no encontrado"));
            }

            Usuario usuario = usuarioOpt.get();
            boolean isValid = jwtService.isTokenValid(token, usuario);

            if (isValid) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("userId", userId);
                response.put("username", username);
                response.put("nombre", usuario.getNombre());
                response.put("rol", usuario.getRol().getNombre());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token inválido"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Error validando token: " + e.getMessage()));
        }
    }

    // ENDPOINTS DE DEBUG - REMOVER EN PRODUCCIÓN
    @PostMapping("/debug-password")
    public ResponseEntity<String> debugPassword(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String passwordRaw = request.get("password");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        boolean matches = passwordEncoder.matches(passwordRaw, usuario.getPassword());

        return ResponseEntity.ok(String.format(
                "Usuario: %s\nPassword en DB: %s\nPassword raw: %s\nCoinciden: %s\nUsuario habilitado: %s\nRol: %s",
                usuario.getCorreo(),
                usuario.getPassword().substring(0, 20) + "...", // Solo primeros 20 caracteres
                passwordRaw,
                matches,
                usuario.isEnabled(),
                usuario.getRol() != null ? usuario.getRol().getNombre() : "Sin rol"
        ));
    }

    @PostMapping("/debug-userdetails")
    public ResponseEntity<String> debugUserDetails(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");

        try {
            UserDetailsService userDetailsService = createUserDetailsService();
            UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

            return ResponseEntity.ok(String.format(
                    "Username: %s\nPassword: %s\nEnabled: %s\nAuthorities: %s",
                    userDetails.getUsername(),
                    userDetails.getPassword().substring(0, 20) + "...",
                    userDetails.isEnabled(),
                    userDetails.getAuthorities()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Método helper para crear UserDetailsService
    private UserDetailsService createUserDetailsService() {
        return username -> usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    @PostMapping("/regenerar-password")
    public ResponseEntity<String> regenerarPassword(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String nuevaPassword = request.get("password");

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            String passwordHasheada = passwordEncoder.encode(nuevaPassword);

            String infoPrevio = String.format(
                    "ANTES:\nPassword antigua: %s\nPassword nueva raw: %s\nPassword nueva hasheada: %s",
                    usuario.getPassword().substring(0, 20) + "...",
                    nuevaPassword,
                    passwordHasheada.substring(0, 20) + "..."
            );

            usuario.setPassword(passwordHasheada);
            usuarioRepository.save(usuario);

            boolean matches = passwordEncoder.matches(nuevaPassword, passwordHasheada);

            return ResponseEntity.ok(String.format(
                    "%s\n\nDESPUÉS:\nPassword actualizada correctamente\nVerificación: %s",
                    infoPrevio,
                    matches ? "✅ EXITOSA" : "❌ FALLÓ"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}