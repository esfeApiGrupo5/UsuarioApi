package com.example.UsuarioApi.dtos;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RolSalidaDto implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
    private List<UsuarioSalidaDto> usuarios;

    public void setDescripcion(String descripcion) {this.descripcion = descripcion; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setId(Integer id) { this.id = id; }
}

