package com.example.andreagaray.pedidos_is2.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.DetallesVenta;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Productos;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Ventas;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Vendedores;
import com.example.andreagaray.pedidos_is2.sqlite.PedidosContract.Clientes;

/**
 * Created by Andrea Garay on 19/10/2016.
 */

public class BaseDatosPedidos extends SQLiteOpenHelper{

    private static final String NOMBRE_BASE_DATOS = "pedidos.db";
    private static final int VERSION_ACTUAL = 1;
    private final Context contexto;
    OperacionesBaseDatos datos;

    interface Tablas{
        String CLIENTE = "cliente";
        String VENDEDOR = "vendedor";
        String PRODUCTO = "producto";
        String VENTA = "venta";
        String DETALLE_VENTA = "detalle_venta";
    }

    interface Referencias{
        String ID_PRODUCTO = String.format("REFERENCES %s(%s)", Tablas.PRODUCTO, Productos.ID_PRODUCTO);

        String ID_VENTA = String.format("REFERENCES %s(%s)", Tablas.VENTA, Ventas.ID_VENTA);

        String ID_CLIENTE = String.format("REFERENCES %s(%s)", Tablas.CLIENTE, Clientes.ID_CLIENTE);

        String CI_VENDEDOR = String.format("REFERENCES %s(%s)", Tablas.VENDEDOR, Vendedores.CI_VENDEDOR);
    }

    public BaseDatosPedidos(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT ,%s TEXT,%s TEXT,%s TEXT, %s TEXT)",
                Tablas.CLIENTE, Clientes.ID_CLIENTE, Clientes.NOMBRE,
                Clientes.APELLIDO, Clientes.RUC, Clientes.DIRECCION,  Clientes.EMAIL));

        db.execSQL(String.format("CREATE TABLE %s( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s TEXT, %s TEXT)",
                Tablas.VENDEDOR, Vendedores.CI_VENDEDOR, Vendedores.NOM_VENDEDOR, Vendedores.APE_VENDEDOR));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s INTEGER, %s INTEGER)",
                Tablas.PRODUCTO, Productos.ID_PRODUCTO, Productos.NOM_PRODUCTO,
                Productos.PRECIO_UNITARIO, Productos.STOCK_ACTUAL));


        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s DATETIME CURRENT_DATE, %s INTEGER, %s INTEGER NOT NULL %s, %s INTEGER NOT NULL %s)",
                Tablas.VENTA, Ventas.ID_VENTA, Ventas.FECHA_VENTA, Ventas.TOTAL_VENTA,
                Ventas.ID_CLIENTE, Referencias.ID_CLIENTE, Ventas.CI_VENDEDOR, Referencias.CI_VENDEDOR));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER NOT NULL %s, %s INTEGER, %s INTEGER NOT NULL %s, %s INTEGER)",
                Tablas.DETALLE_VENTA, DetallesVenta.ID_DETALLE_VENTA, DetallesVenta.ID_VENTA, Referencias.ID_VENTA, DetallesVenta.SUB_TOTAL,
                DetallesVenta.ID_PRODUCTO, Referencias.ID_PRODUCTO, DetallesVenta.CANTIDAD));



        db.execSQL("INSERT INTO " + Tablas.CLIENTE + " VALUES(null, 'Donald', 'Trump','2536987-0', 'Avda. R. de Francia 587', 'dtrump@gmail.com')");
        db.execSQL("INSERT INTO " + Tablas.CLIENTE + " VALUES(null, 'Hillary', 'Clinton','5874512-5', 'De las Llanas 458', 'hclinton@gmail.com')");
        db.execSQL("INSERT INTO " + Tablas.CLIENTE + " VALUES(null, 'Angelina', 'Jolie','235665-9', 'Ruta Mcal. Estigarribia no. 255', 'ajolie@gmail.com')");

        db.execSQL("INSERT INTO " + Tablas.PRODUCTO + " VALUES(null, 'Empanada', 1000, 100)");
        db.execSQL("INSERT INTO " + Tablas.PRODUCTO + " VALUES(null, 'Gaseosa', 2000, 100)");
        db.execSQL("INSERT INTO " + Tablas.PRODUCTO + " VALUES(null, 'Bollo', 500, 100)");

        db.execSQL("INSERT INTO " + Tablas.VENDEDOR + " VALUES(null, 'Vendedor', 'Vendedor')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.VENDEDOR);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.VENTA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DETALLE_VENTA);

        onCreate(db);

    }
}