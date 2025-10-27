package org.esfe.UsuarioApi.repositorios;

import org.esfe.UsuarioApi.modelos.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpresaRepository extends JpaRepository<Empresa, Integer> {

    // Búsqueda paginada por nombre comercial, RUT/NIT y descripción
    Page<Empresa> findByNombreComercialContainingIgnoreCaseAndRutNitContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(
            String nombreComercial, String rutNit, String descripcion, Pageable pageable);

    // Buscar empresas por broker asociado
    Page<Empresa> findByBroker_IdOrderByIdDesc(Integer brokerId, Pageable pageable);

    // Buscar empresas por usuario asociado
    Page<Empresa> findByUsuario_IdOrderByIdDesc(Integer usuarioId, Pageable pageable);
}
