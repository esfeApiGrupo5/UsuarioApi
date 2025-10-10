package org.esfe.UsuarioApi.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "Empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombreComercial;
    private String rutNit;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Puede ser null

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public String getRutNit() { return rutNit; }
    public void setRutNit(String rutNit) { this.rutNit = rutNit; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}

