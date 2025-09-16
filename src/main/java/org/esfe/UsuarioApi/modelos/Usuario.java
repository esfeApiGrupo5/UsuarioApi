package org.esfe.UsuarioApi.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "Usuario")
public class Usuario implements org.springframework.security.core.userdetails.UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de usuario es requerido")
    private String nombre;

    @Email
    @Column(name = "correo", unique = true, nullable = false)
    @NotBlank(message = "El correo de usuario es requerido")
    private String correo;

    @NotBlank(message = "La contraseña es requerida")
    private String password;

    private int estado;

    @ManyToOne(fetch = FetchType.EAGER) // El rol se carga inmediatamente con el usuario
    @JoinColumn(name = "idRol", nullable = false)
    private Rol rol;

    @OneToMany(mappedBy = "usuario", // "usuario" es el nombre del campo en la entidad tarjeta que mapea esta relación
            fetch = FetchType.LAZY // Cuando cargues un Rol, los Usuarios no se cargarán hasta que los pidas.
            // LAZY es el valor por defecto para OneToMany y es recomendado para rendimiento.
    )

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        java.util.Collection<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();
        if (rol != null) {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return estado == 1;
    }
}
