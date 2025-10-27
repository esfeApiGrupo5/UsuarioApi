package org.esfe.UsuarioApi.repositorios;

import org.esfe.UsuarioApi.modelos.Broker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBrokerRepository extends JpaRepository<Broker, Integer> {

    // Búsqueda paginada por licencia y geolocalización (filtros opcionales)
    Page<Broker> findByLicenciaContainingIgnoreCaseAndGeolocalizacionContainingIgnoreCaseOrderByIdDesc(
            String licencia, String geolocalizacion, Pageable pageable);

    // Buscar brokers por usuario asociado
    Page<Broker> findByUsuario_IdOrderByIdDesc(Integer usuarioId, Pageable pageable);
}

