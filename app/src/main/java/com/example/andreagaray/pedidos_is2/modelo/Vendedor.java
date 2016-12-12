package com.example.andreagaray.pedidos_is2.modelo;

/**
 * Created by Andrea Garay on 09/11/2016.
 */


public class Vendedor {

    public Integer ci_vendedor;
    public String nom_vendedor;
    public String ape_vendedor;

    public Vendedor(Integer ci_vendedor, String nom_vendedor, String ape_vendedor) {
        this.ci_vendedor = ci_vendedor;
        this.nom_vendedor = nom_vendedor;
        this.ape_vendedor = ape_vendedor;
    }

}
