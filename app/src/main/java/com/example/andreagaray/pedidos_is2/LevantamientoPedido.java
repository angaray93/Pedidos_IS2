package com.example.andreagaray.pedidos_is2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreagaray.pedidos_is2.modelo.Producto;
import com.example.andreagaray.pedidos_is2.modelo.DetalleVenta;

import com.example.andreagaray.pedidos_is2.modelo.Venta;
import com.example.andreagaray.pedidos_is2.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;
import java.util.List;

public class LevantamientoPedido extends AppCompatActivity implements View.OnClickListener {

    TextView clientePantalla, nroPedido, total;
    OperacionesBaseDatos datos;
    Cursor c;
    int pedido;
    int cant = 1;
    Button botonAgregar, botonRegistrar;
    String nombreProd;
    Integer precio;

    Spinner cantidad;
    Cursor cu;
    Producto prod;
    ArrayList<Producto> productoList = new ArrayList<Producto>();
    ArrayList<DetalleVenta> detalleList = new ArrayList<DetalleVenta>();

    Spinner producto;
    private int codigoClient;
    private int suma;
    private DetalleVenta d;
    private int posicionProducto;
    private Venta venta;
    private int stockArray;
    private int stockAlert;
    private int bandera;
    private Cursor a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_levantamiento_pedido );

        clientePantalla = (TextView) findViewById( R.id.txtCliente2 );
        nroPedido = (TextView) findViewById( R.id.nroPedido );
        cantidad = (Spinner) findViewById( R.id.spCant );
        producto = (Spinner) findViewById( R.id.spProd );
        total = (TextView) findViewById(R.id.txtotal);


        datos = OperacionesBaseDatos.obtenerInstancia( getApplicationContext() );
        c = datos.obtenerVenta();
        c.moveToLast();
        if (c.getCount() == 0) {
            pedido = 1;
        } else {
            pedido = c.getInt( 0 ) + 1;
        }
        if (pedido < 10) {
            nroPedido.setText( String.valueOf( "000" + pedido ) );
        } else if (pedido > 10 && pedido < 100) {
            nroPedido.setText( String.valueOf( "00" + pedido ) );
        } else {
            nroPedido.setText( String.valueOf( "0" + pedido ) );
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            codigoClient = intent.getIntExtra("codigo", 0);
            String cliente = intent.getStringExtra("cliente");
            clientePantalla.setText(cliente);
        }

        cu = datos.obtenerProductos();
        String[] array = new String[cu.getCount() + 1];
        int i = 0;
        array[i] = "Seleccione un producto";
        i = 1;
        if (cu.moveToFirst()) {
            do {
                array[i] = cu.getString( 1 ) +"  Gs." + cu.getString( 2 );
                prod = new Producto( cu.getInt( 0 ), cu.getString( 1 ), cu.getInt( 2 ), cu.getInt( 3 ) );
                productoList.add( prod );
                i++;
            } while (cu.moveToNext());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_dropdown_item_1line, array );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        producto.setAdapter(adapter);
        producto.setSelection( 0 );

        producto.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.BLACK);

                if (position == 0) {

                    bandera = 1;
                    stockArray = 1;
                    String[] arrayStock = new String[stockArray];
                    arrayStock[0] = "0";

                    final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_spinner_item, arrayStock );
                    adapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

                    cantidad.setAdapter( adapter1 );
                    cantidad.setSelection( 0 );
                    cantidad.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cant = Integer.parseInt(adapter1.getItem(position));
                           //((TextView) view).setTextColor( Color.BLACK );

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    } );



                } else {
                    bandera = 0;
                    nombreProd = productoList.get(position - 1).nom_producto;
                    precio = productoList.get(position - 1).precio_unitario;
                    posicionProducto = position - 1;
                    stockArray = productoList.get(position - 1).stock_actual + 1;

                    String[] arrayStock = new String[stockArray];
                    if (stockArray == 1) {
                        arrayStock[0] = "Sin Stock";
                        //stockAlert = 1;
                    } else {
                        for (int x = 0; x < stockArray; x++) {
                            arrayStock[x] = String.valueOf(x);
                            //stockAlert = 0;
                        }
                    }


                    final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_spinner_item, arrayStock );
                    adapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                    cantidad.setAdapter( adapter1 );
                    cantidad.setSelection( 0 );
                    cantidad.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(adapter1.getItem(position) != "Sin Stock"){
                                cant = Integer.parseInt(adapter1.getItem(position));
                                stockAlert = 0;
                            }else if (adapter1.getItem(position) == "Sin Stock") {
                                stockAlert = 1;
                            }
                            ((TextView) view).setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonAgregar = (Button) findViewById( R.id.btnAgregar );
        botonAgregar.setOnClickListener(this);

        botonRegistrar = (Button) findViewById( R.id.btnRegistrar );
        botonRegistrar.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnRegistrar:

                if(detalleList.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Debe Agregar al menos un Producto!!!", Toast.LENGTH_SHORT).show();

                }else{

                    venta = new Venta(null, null , suma, 1, codigoClient);
                    String resultadoVenta, resultadoDetalleVenta = new String();
                    try {
                        datos.getDb().beginTransaction();
                        resultadoVenta= datos.insertarVenta(venta);
                        datos.getDb().setTransactionSuccessful();

                    } finally {
                        datos.getDb().endTransaction();
                    }

                    try {
                        datos.getDb().beginTransaction();

                        for(int x = 0; x < detalleList.size(); x++){

                            resultadoDetalleVenta= datos.insertarDetalleVenta(detalleList.get(x));
                            a= datos.obtenerProductosId(String.valueOf(detalleList.get(x).id_producto));
                            a.moveToFirst();
                            int z = a.getInt(3);
                            datos.actualizarCantidad(z-detalleList.get(x).cantidad, detalleList.get(x).id_producto);
                            if(resultadoDetalleVenta == "-1") break;

                        }
                        datos.getDb().setTransactionSuccessful();

                    } finally {
                        datos.getDb().endTransaction();
                    }

                    if(resultadoVenta == "-1" || resultadoDetalleVenta == "-1"){
                        Toast.makeText(getApplicationContext(), "Error al Registrar el pedido", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Pedido Registrado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                    producto.setSelection(0);
                    cantidad.setSelection(0);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;


            case R.id.btnAgregar:

                if(bandera == 1){

                    Toast.makeText(getApplicationContext(),"Debe seleccionar un Producto!!!", Toast.LENGTH_SHORT).show();

                }else if(cant == 0 && stockAlert==0){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar una Cantidad!!!", Toast.LENGTH_SHORT).show();

                }else if (stockAlert == 1){
                    Toast.makeText(getApplicationContext(), "Producto fuera de Stock!!!", Toast.LENGTH_SHORT).show();

                }else{
                    d = new DetalleVenta(null , pedido, precio * cant, productoList.get(posicionProducto).id_producto, cant);
                    detalleList.add(d);
                    productoList.get(posicionProducto).stock_actual-=cant;
                    suma=0;
                    for (int con = 0; con < detalleList.size(); con++){
                        suma+= detalleList.get(con).sub_total;
                    }
                }


                final TableLayout tableLayout = (TableLayout) findViewById(R.id.tablaDetalle);
                TableRow row = new TableRow(this);
                TextView nomProducto = new TextView(this);
                nomProducto.setText(nombreProd);

                TextView precioUnitario = new TextView(this);
                precioUnitario.setText(String.valueOf(precio));

                TextView txtCantidad = new TextView(this);
                txtCantidad.setText(String.valueOf(cant));

                TextView txtSub = new TextView(this);
                txtSub.setText(String.valueOf(cant * precio));

                row.addView(precioUnitario);
                row.addView(txtCantidad);
                row.addView(nomProducto);
                row.addView(txtSub);
                total.setText(String.valueOf(suma) + " Gs.");

                Button borrar = new Button( this );
                borrar.setText("Borrar");
                borrar.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                row.addView( borrar );

                borrar.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TableRow parent = (TableRow) v.getParent();
                        int posicion = tableLayout.indexOfChild(parent);
                        tableLayout.removeView(parent);

                        productoList.get(detalleList.get(posicion-1).id_producto -1).stock_actual+= detalleList.get(posicion-1).cantidad;
                        detalleList.remove(posicion -1);
                        suma = 0;
                        for (int con = 0; con < detalleList.size(); con++){
                            suma+= detalleList.get(con).sub_total;
                        }
                        total.setText(String.valueOf(suma));
                        producto.setSelection(0);
                        cantidad.setSelection(0);

                    }
                });

                tableLayout.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                producto.setSelection( 0 );
                cantidad.setSelection( 0 );
                break;
            default:
                break;




        }
    }
}