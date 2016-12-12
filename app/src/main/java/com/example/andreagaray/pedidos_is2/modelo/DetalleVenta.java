package com.example.andreagaray.pedidos_is2.modelo;

/**
 * Created by Andrea Garay on 15/10/2016.
 */

public class DetalleVenta {
    public Integer id_detalle_venta;
    public Integer id_venta;
    public Integer sub_total;
    public Integer id_producto;
    public Integer cantidad;

    public DetalleVenta(Integer id_detalle_venta, Integer id_venta, Integer sub_total, Integer id_producto, Integer cantidad) {
        this.id_detalle_venta = id_detalle_venta;
        this.id_venta = id_venta;
        this.sub_total = sub_total;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
    }
}
