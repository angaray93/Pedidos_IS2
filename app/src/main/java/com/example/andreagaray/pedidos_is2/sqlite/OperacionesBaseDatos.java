package com.example.andreagaray.pedidos_is2.sqlite;

/**
 * Created by Andrea Garay on 20/10/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.andreagaray.pedidos_is2.modelo.Cliente;
import com.example.andreagaray.pedidos_is2.modelo.DetalleVenta;
import com.example.andreagaray.pedidos_is2.modelo.Producto;
import com.example.andreagaray.pedidos_is2.modelo.Venta;
import com.example.andreagaray.pedidos_is2.modelo.Vendedor;
import com.example.andreagaray.pedidos_is2.sqlite.BaseDatosPedidos.Tablas;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Clientes;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Productos;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Ventas;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.DetallesVenta;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Vendedores;

import java.util.Date;
import java.util.Locale;

public class OperacionesBaseDatos {

    private static BaseDatosPedidos baseDatos;
    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();

    private OperacionesBaseDatos() {

    }
    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {

        if (baseDatos == null) {
            baseDatos = new BaseDatosPedidos(contexto);
        }
        return instancia;
    }

    // Obtener todas las ventas
    public Cursor obtenerVenta() {

        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(VentaJoinClienteVendedor);
        return builder.query(db, proyVenta, null, null, null, null, null);
    }

    private static final String VentaJoinClienteVendedor = "venta INNER JOIN cliente " +
            "ON venta.id_cliente = cliente.id_cliente " +
            "INNER JOIN vendedor ON venta.ci_vendedor = vendedor.ci_vendedor";

    private final String[] proyVenta = new String[]{

            Tablas.VENTA + "." + Ventas.ID_VENTA,
            Ventas.FECHA_VENTA, Ventas.TOTAL_VENTA, Clientes.NOMBRE,
            Clientes.APELLIDO, Vendedores.NOM_VENDEDOR, Vendedores.APE_VENDEDOR
    };

    // Obtener Ventas por id
    public Cursor obtenerVentasPorId(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String selection = String.format("%s = ?", Ventas.ID_VENTA);
        String selectionArgs[] = {id};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(VentaJoinClienteVendedor);

        String[] proyeccion = {
                Tablas.VENTA + "." + Ventas.ID_VENTA,
                Ventas.TOTAL_VENTA, Ventas.TOTAL_VENTA, Clientes.NOMBRE,
                Clientes.APELLIDO, Vendedores.NOM_VENDEDOR, Vendedores.APE_VENDEDOR
        };
        return builder.query(db, proyeccion, selection, selectionArgs, null, null, null);
    }

    public Cursor obtenerVentasPorIdVenta(String id_venta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s = %s", Tablas.VENTA, Ventas.ID_VENTA, id_venta);
        return db.rawQuery(sql, null);
    }

    // Insertar Venta

    public String insertarVenta(Venta venta) {

        SQLiteDatabase db = baseDatos.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);


        ContentValues valores = new ContentValues();
        valores.put(Ventas.FECHA_VENTA, fecha);
        valores.put(Ventas.TOTAL_VENTA, venta.total_venta);
        valores.put(Ventas.ID_CLIENTE, venta.id_cliente);
        valores.put(Ventas.CI_VENDEDOR, venta.ci_vendedor);

        return String.valueOf(db.insertOrThrow(Tablas.VENTA, null, valores));
    }

    //Actualizar venta
    public boolean actualizarVenta(Venta ventaNueva) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Ventas.FECHA_VENTA, ventaNueva.fecha_venta.toString());
        valores.put(Ventas.TOTAL_VENTA, ventaNueva.total_venta);
        valores.put(Ventas.ID_CLIENTE, ventaNueva.id_cliente);
        valores.put(Ventas.CI_VENDEDOR, ventaNueva.ci_vendedor);

        String whereClause = String.format("%s = ?", Ventas.ID_VENTA);
        String[] whereArgs = {String.valueOf(ventaNueva.id_venta)};/*CAMBIO*/

        int resultado = db.update(Tablas.VENTA, valores, whereClause, whereArgs);
        return resultado > 0;
    }

    //Eliminar venta
    public boolean eliminarVenta(String id_venta) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String whereClause = Ventas.ID_VENTA + "= ?";
        String[] whereArgs = {id_venta};

        int resultado = db.delete(Tablas.VENTA, whereClause, whereArgs);
        return resultado > 0;
    }
    //Obtener Detalle Venta
    /*public Cursor obtenerDetalleVentaPorId(String id_venta){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s= ?", Tablas.DETALLE_VENTA, Ventas.ID_VENTA);
        String[] selectionArgs = {id_venta};

        return db.rawQuery(sql, selectionArgs);
    }*/

    public Cursor obtenerDetalleVenta() {

        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(detalleVentaJoinProducto);
        return builder.query(db, proyVenta, null, null, null, null, null);
    }

    private static final String detalleVentaJoinProducto = "detalle_venta INNER JOIN producto " +
            "ON detalle_venta.id_producto = produto.id_producto " +
            "INNER_JOIN venta ON venta.id_venta = detalle_venta = id_venta";

    private final String[] proyDetalleVenta = new String[]{

            Tablas.DETALLE_VENTA + "." + DetallesVenta.ID_VENTA,
            Ventas.FECHA_VENTA, Productos.NOM_PRODUCTO, Productos.PRECIO_UNITARIO, DetallesVenta.CANTIDAD,
            DetallesVenta.SUB_TOTAL
    };

    // Obtener detalle Venta por id
    public Cursor obtenerDetalleVentaPorId(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String selection = String.format("%s = ?", DetallesVenta.ID_DETALLE_VENTA);
        String selectionArgs[] = {id};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(detalleVentaJoinProducto);

        String[] proyeccion = {
                Tablas.DETALLE_VENTA + "." + DetallesVenta.ID_DETALLE_VENTA,
                Ventas.FECHA_VENTA, Productos.NOM_PRODUCTO, Productos.PRECIO_UNITARIO,
                DetallesVenta.CANTIDAD, DetallesVenta.SUB_TOTAL
        };
        return builder.query(db, proyeccion, selection, selectionArgs, null, null, null);
    }

    public Cursor obtenerDetalleVentaPorIdVenta(String id_venta) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s = %s", Tablas.DETALLE_VENTA, DetallesVenta.ID_VENTA,id_venta);
        return db.rawQuery(sql, null);
    }

    //Insertar detalle Venta
    public String insertarDetalleVenta(DetalleVenta detalle) {

        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DetallesVenta.ID_PRODUCTO, detalle.id_producto);
        valores.put(DetallesVenta.CANTIDAD, detalle.cantidad);
        valores.put(DetallesVenta.ID_VENTA, detalle.id_venta);
        valores.put(DetallesVenta.SUB_TOTAL, detalle.sub_total);



        return String.valueOf(db.insertOrThrow(Tablas.DETALLE_VENTA, null, valores));
    }

    // Actualizar un detalle venta
    public boolean actualizarDetalleVenta(DetalleVenta detalle) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DetallesVenta.ID_VENTA, detalle.id_venta);
        valores.put(DetallesVenta.ID_PRODUCTO, detalle.id_producto);
        valores.put(DetallesVenta.CANTIDAD, detalle.cantidad);
        valores.put(DetallesVenta.SUB_TOTAL, detalle.sub_total);

        String selection = String.format("%s = ?", DetallesVenta.ID_DETALLE_VENTA);
        final String[] whereArgs = {String.valueOf(detalle.id_detalle_venta)};

        int resultado = db.update(Tablas.DETALLE_VENTA, valores, selection, whereArgs);
        return resultado > 0;
    }

    // Eliminar Detalle venta
    public boolean eliminarDetalleVenta(String id_detalle_venta) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String selection = String.format("%s = ?", DetallesVenta.ID_DETALLE_VENTA);
        String[] whereArgs = {id_detalle_venta};

        int resultado = db.delete(Tablas.DETALLE_VENTA, selection, whereArgs);
        return resultado > 0;
    }

    // Producto
    // Obtener todos Productos
    public Cursor obtenerProductos() {

        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s ", Tablas.PRODUCTO);
        return db.rawQuery(sql, null);
    }

    public Cursor obtenerProductosId(String id_producto) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s = %s", Tablas.PRODUCTO, Productos.ID_PRODUCTO, id_producto);
        return db.rawQuery(sql, null);
    }

    // operaciones producto
    // Insertar Producto
    public String insertarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Productos.NOM_PRODUCTO, producto.nom_producto);
        //valores.put(Productos.ID_TIPO, producto.id_tipo);
        valores.put(Productos.PRECIO_UNITARIO, producto.precio_unitario);
        valores.put(Productos.STOCK_ACTUAL, producto.stock_actual);


        return String.valueOf(db.insertOrThrow(Tablas.PRODUCTO, null, valores));
    }

    // Actualizar Prodcuto
    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Productos.NOM_PRODUCTO, producto.nom_producto);
        //valores.put(Productos.ID_TIPO, producto.id_tipo);
        valores.put(Productos.PRECIO_UNITARIO, producto.precio_unitario);
        valores.put(Productos.STOCK_ACTUAL, producto.stock_actual);

        String whereClause = String.format("%s = ?", Productos.ID_PRODUCTO);
        String[] whereArgs = {String.valueOf(producto.id_producto)};

        int resultado = db.update(Tablas.PRODUCTO, valores, whereClause, whereArgs);
        return resultado > 0;
    }

    // Eliminar Producto
    public boolean eliminarProducto(String id_producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s = ?", Productos.ID_PRODUCTO);
        String[] whereArgs = {id_producto};

        int resultado = db.delete(Tablas.PRODUCTO, whereClause, whereArgs);
        return resultado > 0;
    }

    public void actualizarCantidad(int cantidad, int id){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String sql = String.format("UPDATE %s set %s = %s WHERE %s = %s", Tablas.PRODUCTO, Productos.STOCK_ACTUAL, String.valueOf(cantidad),
                Productos.ID_PRODUCTO, String.valueOf(id));
        db.execSQL(sql);
    }

    // Operaciones cliente
    // obtener Clientes
    public Cursor obtenerClientes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CLIENTE);
        return db.rawQuery(sql, null);
    }

    public Cursor obtenerClientesPorId(String id_cliente){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s = %s", Tablas.CLIENTE, Clientes.ID_CLIENTE, id_cliente);
        return db.rawQuery(sql,null);
    }

    public String insertarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.ID_CLIENTE, cliente.id_cliente);
        valores.put(Clientes.NOMBRE, cliente.nombre);
        valores.put(Clientes.APELLIDO, cliente.apellido);
        valores.put(Clientes.RUC, cliente.ruc);
        valores.put(Clientes.DIRECCION, cliente.direccion);
        valores.put(Clientes.EMAIL, cliente.email);

        return String.valueOf(db.insertOrThrow(Tablas.CLIENTE, null, valores));
    }

    //Actualizar Cliene
    public boolean actualizarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.NOMBRE, cliente.nombre);
        valores.put(Clientes.APELLIDO, cliente.apellido);
        valores.put(Clientes.RUC, cliente.ruc);
        valores.put(Clientes.DIRECCION, cliente.direccion);
        valores.put(Clientes.EMAIL, cliente.email);

        String whereClause = String.format("%s = ?", Clientes.ID_CLIENTE);
        String[] whereArgs = {String.valueOf(cliente.id_cliente)};

        int resultado = db.update(Tablas.CLIENTE, valores, whereClause, whereArgs);
        return resultado > 0;
    }

    // Eliminar clientes
    public boolean eliminarCliente(String id_cliente){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s = ?", Clientes.ID_CLIENTE);
        String[] whereArgs = {id_cliente};

        int resultado = db.delete(Tablas.CLIENTE, whereClause, whereArgs);
        return resultado > 0;
    }

    // Operaciones Vendedor
    // obtener vendedores
    public Cursor obtenerVendedores() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s ", Tablas.VENDEDOR);
        return db.rawQuery(sql, null);
    }

    public String insetarVendedor(Vendedor vendedor) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Vendedores.CI_VENDEDOR, vendedor.ci_vendedor);
        valores.put(Vendedores.NOM_VENDEDOR, vendedor.nom_vendedor);
        valores.put(Vendedores.APE_VENDEDOR, vendedor.ape_vendedor);

        return String.valueOf(db.insertOrThrow(Tablas.VENDEDOR, null, valores));
    }

    //Actualizar Vendedor
    public boolean actualizarVendedor(Vendedor vendedor) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Vendedores.NOM_VENDEDOR, vendedor.nom_vendedor);
        valores.put(Vendedores.APE_VENDEDOR, vendedor.ape_vendedor);

        String whereClause = String.format("%s = ?", Vendedores.CI_VENDEDOR);
        String[] whereArgs = {String.valueOf(vendedor.ci_vendedor)};

        int resultado = db.update(Tablas.VENDEDOR, valores, whereClause, whereArgs);
        return resultado > 0;
    }

    // Eliminar Vendedor
    public boolean eliminarVendedor(String ci_vendedor){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s = ?", Vendedores.CI_VENDEDOR);
        String[] whereArgs = {ci_vendedor};

        int resultado = db.delete(Tablas.VENDEDOR, whereClause, whereArgs);
        return resultado > 0;
    }


}

