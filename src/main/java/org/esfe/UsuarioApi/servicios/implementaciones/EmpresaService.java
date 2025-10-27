package org.esfe.UsuarioApi.servicios.implementaciones;

import org.esfe.UsuarioApi.modelos.Empresa;
import org.esfe.UsuarioApi.repositorios.IEmpresaRepository;
import org.esfe.UsuarioApi.servicios.interfaces.IEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService implements IEmpresaService {

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Override
    public Page<Empresa> obtenerTodosPaginados(Pageable pageable) {
        return empresaRepository.findAll(pageable);
    }

    @Override
    public List<Empresa> obtenerTodos() {
        return empresaRepository.findAll();
    }

    @Override
    public Page<Empresa> findByNombreComercialContainingIgnoreCaseAndRutNitContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(String nombreComercial, String rutNit, String descripcion, Pageable pageable) {
        return empresaRepository.findByNombreComercialContainingIgnoreCaseAndRutNitContainingIgnoreCaseAndDescripcionContainingIgnoreCaseOrderByIdDesc(nombreComercial, rutNit, descripcion, pageable);
    }

    @Override
    public Page<Empresa> findByBroker_IdOrderByIdDesc(Integer brokerId, Pageable pageable) {
        return empresaRepository.findByBroker_IdOrderByIdDesc(brokerId, pageable);
    }

    @Override
    public Page<Empresa> findByUsuario_IdOrderByIdDesc(Integer usuarioId, Pageable pageable) {
        return empresaRepository.findByUsuario_IdOrderByIdDesc(usuarioId, pageable);
    }

    @Override
    public Empresa obtenerPorId(Integer id) {
        Optional<Empresa> optional = empresaRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Empresa crear(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa editar(Empresa empresa) {
        Optional<Empresa> optional = empresaRepository.findById(empresa.getId());
        if (optional.isEmpty()) {
            return null;
        }
        Empresa existente = optional.get();
        existente.setNombreComercial(empresa.getNombreComercial());
        existente.setRutNit(empresa.getRutNit());
        existente.setTelefono(empresa.getTelefono());
        existente.setGeolocalizacion(empresa.getGeolocalizacion());
        existente.setDescripcion(empresa.getDescripcion());
        existente.setUsuario(empresa.getUsuario());
        existente.setBroker(empresa.getBroker());
        return empresaRepository.save(existente);
    }

    @Override
    public void eliminarPorId(Integer id) {
        empresaRepository.deleteById(id);
    }
}
