package org.esfe.UsuarioApi.seguridad.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioLogin {
    private String correo;
    private String password;

    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
}