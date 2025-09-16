package org.esfe.UsuarioApi.seguridad.dtos;

public class UsuarioToken {
    private String token;

    public UsuarioToken(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}