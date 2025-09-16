package com.example.UsuarioApi.servicios.implementaciones;

import com.example.UsuarioApi.dtos.UsuarioGuardarDto;
import com.example.UsuarioApi.dtos.UsuarioModificarDto;
import com.example.UsuarioApi.dtos.UsuarioSalidaDto;
import com.example.UsuarioApi.modelos.Usuario;
import com.example.UsuarioApi.repositorios.IUsuarioRepository;
import com.example.UsuarioApi.servicios.interfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private com.example.UsuarioApi.repositorios.IRolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Método de login
    @Override
    public UsuarioSalidaDto login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreoAndPassword(correo, password)
                .orElse(null);
        return usuario != null ? toUsuarioSalidaDto(usuario) : null;
    }

    @Override
    public Page<UsuarioSalidaDto> obtenerTodosPaginados(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findAll(pageable);
        List <UsuarioSalidaDto> usuariosDto = page.stream().map(usuario -> modelMapper.map(usuario, UsuarioSalidaDto.class)).collect(Collectors.toList());

        return new PageImpl<>(usuariosDto, pageable, page.getTotalElements());
    }

    @Override
    public List<UsuarioSalidaDto> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuario -> modelMapper.map(usuario, UsuarioSalidaDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<UsuarioSalidaDto> findByNombreContainingIgnoreCaseAndCorreoContainingIgnoreCaseAndRol_IdOrderByIdDesc(
            String nombre, String correo, Integer idRol, Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findByNombreContainingIgnoreCaseAndCorreoContainingIgnoreCaseAndRol_IdOrderByIdDesc(
                nombre, correo, idRol, pageable);
        return usuarios.map(this::toUsuarioSalidaDto);
    }

    @Override
    public UsuarioSalidaDto obtenerPorId(Integer id) {
        return modelMapper.map(usuarioRepository.findById(id).get(), UsuarioSalidaDto.class);
    }

    @Override
    public UsuarioSalidaDto crear(UsuarioGuardarDto usuarioGuardarDto) {
        Usuario usuario = modelMapper.map(usuarioGuardarDto, Usuario.class);
        usuario.setId(null); // Asegura que el id sea null al crear
        // Asignar el rol correctamente
        if (usuarioGuardarDto.getIdRol() != null) {
            usuario.setRol(rolRepository.findById(usuarioGuardarDto.getIdRol()).orElse(null));
        } else {
            usuario.setRol(null);
        }
        Usuario guardado = usuarioRepository.save(usuario);
        return modelMapper.map(guardado, UsuarioSalidaDto.class);
    }

    @Override
    public UsuarioSalidaDto editar(UsuarioModificarDto usuarioModificarDto) {
        Usuario usuario = usuarioRepository.save(modelMapper.map(usuarioModificarDto, Usuario.class));
        return modelMapper.map(usuario, UsuarioSalidaDto.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Método de mapeo
    private UsuarioSalidaDto toUsuarioSalidaDto(Usuario usuario) {
        UsuarioSalidaDto dto = new UsuarioSalidaDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());
        dto.setEstado(usuario.getEstado());
        // Se debe modificar para mapear el rol correctamente usando RolSalidaDto
        // dto.setRol(...);
        return dto;
    }
}
