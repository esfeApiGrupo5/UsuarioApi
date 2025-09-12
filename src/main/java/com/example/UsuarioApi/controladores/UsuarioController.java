package com.example.UsuarioApi.controladores;

import com.example.UsuarioApi.modelos.Usuario;
import com.example.UsuarioApi.servicios.interfaces.IUsuarioService;
import com.example.UsuarioApi.servicios.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;

    // INDEX: Listado de usuarios con paginación
    @GetMapping
    public ResponseEntity<Page<Usuario>> index(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Page<Usuario> usuarios = usuarioService.obtenerTodosPaginados(PageRequest.of(currentPage, pageSize));
        return ResponseEntity.ok(usuarios);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.crearOEditar(usuario);
        return ResponseEntity.ok(nuevo);
    }

    // EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setPassword(usuario.getPassword());
        usuarioExistente.setRol(usuario.getRol());
        usuarioExistente.setEstado(usuario.getEstado());

        Usuario actualizado = usuarioService.crearOEditar(usuarioExistente);
        return ResponseEntity.ok(actualizado);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminarPorId(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    // DETALLES
    @GetMapping("/{id}")
    public ResponseEntity<?> detalles(@PathVariable Integer id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }
}
