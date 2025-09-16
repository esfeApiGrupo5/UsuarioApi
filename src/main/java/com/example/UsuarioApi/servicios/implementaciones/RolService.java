package com.example.UsuarioApi.servicios.implementaciones;

import com.example.UsuarioApi.dtos.RolSalidaDto;
import com.example.UsuarioApi.modelos.Rol;
import com.example.UsuarioApi.repositorios.IRolRepository;
import com.example.UsuarioApi.servicios.interfaces.IRolService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class RolService implements IRolService{
    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<RolSalidaDto> obtenerTodosPaginados(Pageable pageable) {
        Page<Rol> roles = rolRepository.findAll(pageable);
        return roles.map(this::toRolSalidaDto);
    }

    @Override
    public List<RolSalidaDto> obtenerTodos() {
        List<Rol> roles = rolRepository.findAll();
        return roles.stream().map(this::toRolSalidaDto).toList();
    }

    @Override
    public Page<RolSalidaDto> findByNombreContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(String nombre, String descripcion, Pageable pageable) {
        Page<Rol> roles = rolRepository.findByNombreContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(nombre, descripcion, pageable);
        return roles.map(this::toRolSalidaDto);
    }

    @Override
    public RolSalidaDto obtenerPorId(Integer id) {
        Optional<Rol> rolOptional = rolRepository.findById(id);
        return rolOptional.map(this::toRolSalidaDto).orElse(null);
    }

    @Override
    public RolSalidaDto crearOEditar(Rol rol) {
        Rol guardado = rolRepository.save(rol);
        return toRolSalidaDto(guardado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        rolRepository.deleteById(id);
    }

    // Método de mapeo
    private RolSalidaDto toRolSalidaDto(Rol rol) {
        RolSalidaDto dto = new RolSalidaDto();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        // Se debe modificar para mapear la lista de usuarios correctamente usando UsuarioSalidaDto
        // dto.setUsuarios(...);
        return dto;
    }
}
