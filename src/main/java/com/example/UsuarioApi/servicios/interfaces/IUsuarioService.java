package com.example.UsuarioApi.servicios.interfaces;


import com.example.UsuarioApi.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {
    Page<Usuario> obtenerTodosPaginados(Pageable pageable);

    List<Usuario> obtenerTodos();

    Page<Usuario> findByNombreContainingIgnoreCaseAndCorreoContainingIgnoreCaseAndRol_IdOrderByIdDesc(String nombre, String correo, Integer idRol, Pageable pageable);

    Usuario obtenerPorId(Integer id);

    Usuario crearOEditar(Usuario usuario);

    void eliminarPorId(Integer id);

    Usuario login(String correo, String password);

}
