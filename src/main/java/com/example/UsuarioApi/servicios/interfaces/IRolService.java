package com.example.UsuarioApi.servicios.interfaces;

import com.example.UsuarioApi.modelos.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolService {
    Page<Rol> obtenerTodosPaginados(Pageable pageable);

    List<Rol> obtenerTodos();

    Page<Rol> findByNombreContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(String nombre, String descripcion, Pageable pageable);

    Rol obtenerPorId(Integer id);

    Rol crearOEditar(Rol rol);

    void eliminarPorId(Integer id);
}
