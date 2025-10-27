package org.esfe.UsuarioApi.servicios.interfaces;

import org.esfe.UsuarioApi.modelos.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmpresaService {
    Page<Empresa> obtenerTodosPaginados(Pageable pageable);

    List<Empresa> obtenerTodos();

    Page<Empresa> findByNombreComercialContainingIgnoreCaseAndRutNitContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(
            String nombreComercial, String rutNit, String descripcion, Pageable pageable);

    Page<Empresa> findByBroker_IdOrderByIdDesc(Integer brokerId, Pageable pageable);

    Page<Empresa> findByUsuario_IdOrderByIdDesc(Integer usuarioId, Pageable pageable);

    Empresa obtenerPorId(Integer id);

    Empresa crear(Empresa empresa);

    Empresa editar(Empresa empresa);

    void eliminarPorId(Integer id);
}

