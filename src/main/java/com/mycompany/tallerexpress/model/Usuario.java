
package com.mycompany.tallerexpress.model;

import java.util.Date;


    public class Usuario {
    private int id;
    private String username;
    private String password;
    private Role role;
    private String estado;
    private Date createdAt;

    // Getters y Setters habituales...
    public int getId() {
        return id;
    }

    public void setId(int id) { // <-- Este es el método que busca el Repository
        this.id = id;
    }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

