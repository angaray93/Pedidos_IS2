package com.example.andreagaray.pedidos_is2.modelo;

/**
 * Created by Andrea Garay on 15/10/2016.
 */

public class Cliente {

    public Integer id_cliente;
    public String nombre;
    public String apellido;
    public String ruc;
    public String direccion;
    public String email;

    public Cliente(Integer id_cliente, String nombre, String apellido, String ruc, String direccion, String email) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ruc = ruc;
        this.direccion = direccion;
        this.email = email;
    }
}
