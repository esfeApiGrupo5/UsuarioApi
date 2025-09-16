package com.example.UsuarioApi.servicios.interfaces;

import com.example.UsuarioApi.modelos.Rol;
import com.example.UsuarioApi.dtos.RolSalidaDto;
import com.example.UsuarioApi.dtos.RolGuardarDto;
import com.example.UsuarioApi.dtos.RolModificarDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolService {
    Page<RolSalidaDto> obtenerTodosPaginados(Pageable pageable);

    List<RolSalidaDto> obtenerTodos();

    Page<RolSalidaDto> findByNombreContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(String nombre, String descripcion, Pageable pageable);

    RolSalidaDto obtenerPorId(Integer id);

    RolSalidaDto crear(RolGuardarDto rolGuardarDto);
    RolSalidaDto editar(RolModificarDto rolModificarDto);

    void eliminarPorId(Integer id);
}
