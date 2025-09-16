package org.esfe.UsuarioApi.servicios.implementaciones;

import org.esfe.UsuarioApi.dtos.RolSalidaDto;
import org.esfe.UsuarioApi.dtos.RolGuardarDto;
import org.esfe.UsuarioApi.dtos.RolModificarDto;
import org.esfe.UsuarioApi.modelos.Rol;
import org.esfe.UsuarioApi.repositorios.IRolRepository;
import org.esfe.UsuarioApi.servicios.interfaces.IRolService;
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
    public void eliminarPorId(Integer id) {
        rolRepository.deleteById(id);
    }

    @Override
    public RolSalidaDto crear(RolGuardarDto rolGuardarDto) {
        Rol rol = new Rol();
        rol.setNombre(rolGuardarDto.getNombre());
        rol.setDescripcion(rolGuardarDto.getDescripcion());
        Rol guardado = rolRepository.save(rol);
        return toRolSalidaDto(guardado);
    }

    @Override
    public RolSalidaDto editar(RolModificarDto rolModificarDto) {
        Optional<Rol> rolOptional = rolRepository.findById(rolModificarDto.getId());
        if (rolOptional.isEmpty()) {
            return null;
        }
        Rol rol = rolOptional.get();
        rol.setNombre(rolModificarDto.getNombre());
        rol.setDescripcion(rolModificarDto.getDescripcion());
        Rol actualizado = rolRepository.save(rol);
        return toRolSalidaDto(actualizado);
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
