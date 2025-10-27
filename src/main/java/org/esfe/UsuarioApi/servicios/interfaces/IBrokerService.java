package org.esfe.UsuarioApi.servicios.interfaces;

import org.esfe.UsuarioApi.modelos.Broker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBrokerService {
    Page<Broker> obtenerTodosPaginados(Pageable pageable);

    List<Broker> obtenerTodos();

    Page<Broker> findByLicenciaContainingIgnoreCaseAndGeolocalizacionContainingIgnoreCaseOrderByIdDesc(String licencia, String geolocalizacion, Pageable pageable);

    Page<Broker> findByUsuario_IdOrderByIdDesc(Integer usuarioId, Pageable pageable);

    Broker obtenerPorId(Integer id);

    Broker crear(Broker broker);

    Broker editar(Broker broker);

    void eliminarPorId(Integer id);
}

