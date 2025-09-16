package com.example.UsuarioApi.servicios.interfaces;


import com.example.UsuarioApi.dtos.UsuarioGuardarDto;
import com.example.UsuarioApi.dtos.UsuarioModificarDto;
import com.example.UsuarioApi.dtos.UsuarioSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {
    Page<UsuarioSalidaDto> obtenerTodosPaginados(Pageable pageable);

    List<UsuarioSalidaDto> obtenerTodos();

    Page<UsuarioSalidaDto> findByNombreContainingIgnoreCaseAndCorreoContainingIgnoreCaseAndRol_IdOrderByIdDesc(String nombre, String correo, Integer idRol, Pageable pageable);

    UsuarioSalidaDto obtenerPorId(Integer id);

    UsuarioSalidaDto crear(UsuarioGuardarDto usuarioGuardarDto);

    UsuarioSalidaDto editar(UsuarioModificarDto usuarioModificarDto);

    UsuarioSalidaDto login(String correo, String password);

    void eliminarPorId(Integer id);
}
