package com.example.UsuarioApi.repositorios;


import com.example.UsuarioApi.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Metodo para el login
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);

    // Metodo para búsquedas con paginación
    Page<Usuario> findByNombreContainingIgnoreCaseAndCorreoContainingIgnoreCaseAndRol_IdOrderByIdDesc(
            String nombre, String correo, Integer idRol, Pageable pageable);
}
