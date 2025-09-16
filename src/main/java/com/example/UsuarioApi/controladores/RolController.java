package com.example.UsuarioApi.controladores;

import com.example.UsuarioApi.dtos.RolGuardarDto;
import com.example.UsuarioApi.dtos.RolModificarDto;
import com.example.UsuarioApi.dtos.RolSalidaDto;
import com.example.UsuarioApi.servicios.interfaces.IRolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private IRolService rolService;

    // INDEX: Listado de roles con paginación
    @GetMapping
    public ResponseEntity<Page<RolSalidaDto>> index(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        Page<RolSalidaDto> rolesDto = rolService.obtenerTodosPaginados(PageRequest.of(currentPage, pageSize));
        return ResponseEntity.ok(rolesDto);
    }

    // LISTADO SIN PAGINACIÓN
    @GetMapping("/all")
    public ResponseEntity<List<RolSalidaDto>> getAll() {
        List<RolSalidaDto> roles = rolService.obtenerTodos();
        return ResponseEntity.ok(roles);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<RolSalidaDto> crear(@Valid @RequestBody RolGuardarDto rolDto) {
        RolSalidaDto salidaDto = rolService.crear(rolDto);
        return ResponseEntity.ok(salidaDto);
    }

    // EDITAR
    @PutMapping
    public ResponseEntity<?> editar(@Valid @RequestBody RolModificarDto rolDto) {
        RolSalidaDto rolExistente = rolService.obtenerPorId(rolDto.getId());
        if (rolExistente == null) {
            return ResponseEntity.notFound().build();
        }
        RolSalidaDto salidaDto = rolService.editar(rolDto);
        return ResponseEntity.ok(salidaDto);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        RolSalidaDto rol = rolService.obtenerPorId(id);
        if (rol == null) {
            return ResponseEntity.notFound().build();
        }
        rolService.eliminarPorId(id);
        return ResponseEntity.ok("Rol eliminado correctamente");
    }

    // DETALLES
    @GetMapping("/{id}")
    public ResponseEntity<?> detalles(@PathVariable Integer id) {
        RolSalidaDto rol = rolService.obtenerPorId(id);
        if (rol == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rol);
    }
}

