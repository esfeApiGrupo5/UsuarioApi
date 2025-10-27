package org.esfe.UsuarioApi.servicios.implementaciones;

import org.esfe.UsuarioApi.modelos.Broker;
import org.esfe.UsuarioApi.repositorios.IBrokerRepository;
import org.esfe.UsuarioApi.servicios.interfaces.IBrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrokerService implements IBrokerService {

    @Autowired
    private IBrokerRepository brokerRepository;

    @Override
    public Page<Broker> obtenerTodosPaginados(Pageable pageable) {
        return brokerRepository.findAll(pageable);
    }

    @Override
    public List<Broker> obtenerTodos() {
        return brokerRepository.findAll();
    }

    @Override
    public Page<Broker> findByLicenciaContainingIgnoreCaseAndGeolocalizacionContainingIgnoreCaseOrderByIdDesc(String licencia, String geolocalizacion, Pageable pageable) {
        return brokerRepository.findByLicenciaContainingIgnoreCaseAndGeolocalizacionContainingIgnoreCaseOrderByIdDesc(licencia, geolocalizacion, pageable);
    }

    @Override
    public Page<Broker> findByUsuario_IdOrderByIdDesc(Integer usuarioId, Pageable pageable) {
        return brokerRepository.findByUsuario_IdOrderByIdDesc(usuarioId, pageable);
    }

    @Override
    public Broker obtenerPorId(Integer id) {
        Optional<Broker> optional = brokerRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Broker crear(Broker broker) {
        return brokerRepository.save(broker);
    }

    @Override
    public Broker editar(Broker broker) {
        Optional<Broker> optional = brokerRepository.findById(broker.getId());
        if (optional.isEmpty()) {
            return null;
        }
        Broker existente = optional.get();
        existente.setLicencia(broker.getLicencia());
        existente.setPorcentajeComisionBase(broker.getPorcentajeComisionBase());
        existente.setGeolocalizacion(broker.getGeolocalizacion());
        existente.setUsuario(broker.getUsuario());
        return brokerRepository.save(existente);
    }

    @Override
    public void eliminarPorId(Integer id) {
        brokerRepository.deleteById(id);
    }
}

