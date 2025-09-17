package org.esfe.UsuarioApi.controladores;

import org.esfe.UsuarioApi.dtos.UsuarioGuardarDto;
import org.esfe.UsuarioApi.dtos.UsuarioModificarDto;
import org.esfe.UsuarioApi.dtos.UsuarioSalidaDto;
import org.esfe.UsuarioApi.servicios.interfaces.IUsuarioService;
import org.esfe.UsuarioApi.servicios.interfaces.IRolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Page<UsuarioSalidaDto>> index(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        Page<UsuarioSalidaDto> usuariosDto = usuarioService.obtenerTodosPaginados(PageRequest.of(currentPage, pageSize));
        return ResponseEntity.ok(usuariosDto);
    }

    // LISTADO SIN PAGINACIÓN
    @GetMapping("/all")
    public ResponseEntity<List<UsuarioSalidaDto>> getAll() {
        List<UsuarioSalidaDto> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<UsuarioSalidaDto> crear(@Valid @RequestBody UsuarioGuardarDto usuarioDto) {
        UsuarioSalidaDto salidaDto = usuarioService.crear(usuarioDto);
        return ResponseEntity.ok(salidaDto);
    }

    // EDITAR
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping
    public ResponseEntity<?> editar(@Valid @RequestBody UsuarioModificarDto usuarioDto) {
        UsuarioSalidaDto usuarioExistente = usuarioService.obtenerPorId(usuarioDto.getId());
        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }
        UsuarioSalidaDto salidaDto = usuarioService.editar(usuarioDto);
        return ResponseEntity.ok(salidaDto);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        UsuarioSalidaDto usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminarPorId(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    // DETALLES
    @GetMapping("/{id}")
    public ResponseEntity<?> detalles(@PathVariable Integer id) {
        UsuarioSalidaDto usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }
}
