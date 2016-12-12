package com.example.andreagaray.pedidos_is2.sqlite;

/**
 * Created by Andrea Garay on 18/10/2016.
 */

public class PedidosContract {

    interface  ColumnasCliente{
        String ID_CLIENTE = "id_cliente";
        String NOMBRE = "nombre";
        String APELLIDO = "apellido";
        String RUC = "ruc";
        String DIRECCION = "direccion";
        String EMAIL= "email";
    }

    interface ColumnasVendedor{
        String CI_VENDEDOR = "ci_vendedor";
        String NOM_VENDEDOR = "nom_vendedor";
        String APE_VENDEDOR = "ape_vendedor";
    }

    interface ColumnasProducto{
        String ID_PRODUCTO = "id_producto";
        String ID_TIPO = "id_tipo";
        String NOM_PRODUCTO = "nom_produto";
        String PRECIO_UNITARIO = "precio_unitario";
        String STOCK_ACTUAL = "stock_actual";
    }

    /*interface ColumnasTipoProducto{
        String ID_TIPO = "id_tipo";
        String DESCRIPCION_TIPO = "descripcion_tipo";
    }*/

    interface ColumnasVenta{
        String ID_VENTA = "id_venta";
        String FECHA_VENTA = "fecha_venta";
        String TOTAL_VENTA = "total_venta";
        String CI_VENDEDOR = "ci_vendedor";
        String ID_CLIENTE = "id_cliente";
    }

    interface ColumnasDetalleVenta{
        String ID_DETALLE_VENTA = "id_detalle_venta";
        String ID_VENTA = "id_venta";
        String SUB_TOTAL = "sub_total";
        String ID_PRODUCTO = "id_producto";
        String CANTIDAD = "cantidad";
    }

    public static class Clientes implements ColumnasCliente{

    }

    public static class Vendedores implements ColumnasVendedor{

    }

    public static class Productos implements ColumnasProducto{

    }

    public static class Ventas implements ColumnasVenta{

    }

    public static class DetallesVenta implements ColumnasDetalleVenta{

    }

    /*public static class TiposProducto implements ColumnasTipoProducto{

    }

    private PedidosContract() {

    }*/

}

