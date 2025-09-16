package org.esfe.UsuarioApi.dtos;

import java.io.Serializable;

public class UsuarioSalidaDto implements Serializable {
    private Integer id;
    private String nombre;
    private String correo;
    private int estado;
    private RolSalidaDto rol;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public RolSalidaDto getRol() { return rol; }
    public void setRol(RolSalidaDto rol) { this.rol = rol; }

    public void setPassword(String password) {
        // No hacer nada, ya que no queremos exponer la contraseña

    }
}
