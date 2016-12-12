package com.example.andreagaray.pedidos_is2.modelo;

import java.util.Date;

/**
 * Created by Andrea Garay on 15/10/2016.
 */

public class Venta {

    public Integer id_venta;
    public String fecha_venta;
    public Integer total_venta;
    public Integer ci_vendedor;
    public Integer id_cliente;

    public Venta(Integer id_venta, String fecha_venta, Integer total_venta, Integer ci_vendedor, Integer id_cliente) {
        this.id_venta = id_venta;
        this.fecha_venta = fecha_venta;
        this.total_venta = total_venta;
        this.ci_vendedor = ci_vendedor;
        this.id_cliente = id_cliente;
    }
}
