package org.esfe.UsuarioApi.modelos;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Broker")
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String licencia;

    private BigDecimal porcentajeComisionBase;

    private String geolocalizacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Puede ser null

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }

    public BigDecimal getPorcentajeComisionBase() { return porcentajeComisionBase; }
    public void setPorcentajeComisionBase(BigDecimal porcentajeComisionBase) { this.porcentajeComisionBase = porcentajeComisionBase; }

    public String getGeolocalizacion() { return geolocalizacion; }
    public void setGeolocalizacion(String geolocalizacion) { this.geolocalizacion = geolocalizacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
