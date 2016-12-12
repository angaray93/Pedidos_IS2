package com.example.andreagaray.pedidos_is2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.andreagaray.pedidos_is2.sqlite.OperacionesBaseDatos;

public class detalle_pedido extends AppCompatActivity {

    TableLayout tbListDet;
    TextView cliente, fecha, total;
    private OperacionesBaseDatos datos;
    private Cursor venta;
    private Cursor client;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detalle_pedido );

        tbListDet = (TableLayout) findViewById(R.id.tablaListaDet);
        cliente = (TextView) findViewById(R.id.textView6);
        fecha = (TextView) findViewById(R.id.textView8);
        total = (TextView) findViewById(R.id.textView7);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
        Intent intent = getIntent();
        int codigoVenta = intent.getIntExtra("codigo", 0);

        venta = datos.obtenerVentasPorIdVenta(String.valueOf(codigoVenta));
        venta.moveToFirst();
        client = datos.obtenerClientesPorId(String.valueOf(venta.getInt(3)));
        client.moveToFirst();

        cliente.setText(client.getString(1) + " " + client.getString(2));
        fecha.setText(String.valueOf(venta.getString(1)));
        total.setText(String.valueOf(venta.getInt(2)) + " Gs.");


        c = datos.obtenerDetalleVentaPorIdVenta(String.valueOf(codigoVenta));
        int cantidadRegistros = c.getCount();
        String[] array = new String[cantidadRegistros];
        int i=0;
        if(c.moveToFirst()){
            do{

                Cursor prod = datos.obtenerProductosId(String.valueOf(c.getInt(3)));
                prod.moveToFirst();

                TableRow row = new TableRow(this);
                TextView product = new TextView(this);
                product.setText(prod.getString(1));
                TextView precio = new TextView(this);
                precio.setText(String.valueOf(prod.getInt(2)) + " Gs.");
                TextView cantidad = new TextView(this);
                cantidad.setText(String.valueOf(c.getInt(4)));
                TextView subtotal = new TextView(this);
                subtotal.setText(String.valueOf(c.getInt(2)) + " Gs.");

                row.addView(cantidad);
                row.addView(product);
                row.addView(precio);
                row.addView(subtotal);

                tbListDet.addView(row);


            }while (c.moveToNext());
        }


    }
}
