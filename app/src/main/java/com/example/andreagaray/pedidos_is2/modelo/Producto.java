package com.example.andreagaray.pedidos_is2.modelo;

/**
 * Created by Andrea Garay on 15/10/2016.
 */

public class Producto {

    public Integer id_producto;
    public String nom_producto;
    public Integer precio_unitario;
    public Integer stock_actual;

    public Producto(Integer id_producto, String nom_producto, Integer precio_unitario, Integer stock_actual) {
        this.id_producto = id_producto;
        this.nom_producto = nom_producto;
        this.precio_unitario = precio_unitario;
        this.stock_actual = stock_actual;
    }
}