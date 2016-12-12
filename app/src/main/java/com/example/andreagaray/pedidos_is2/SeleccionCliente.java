package com.example.andreagaray.pedidos_is2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.andreagaray.pedidos_is2.sqlite.OperacionesBaseDatos;

public class SeleccionCliente extends AppCompatActivity {

    OperacionesBaseDatos datos;
    Cursor c;
    ListView listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_cliente);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
        c = datos.obtenerClientes(); //cursor de registros de clientes
        int cantidadRegistros = c.getCount(); //cantidad de datos que se extrae en el cursor
        int i = 0;
        String[] array = new String[cantidadRegistros];
        Integer[] arrayCodigo = new Integer[cantidadRegistros];
        if (c.moveToFirst()) {
            do {
                String linea = c.getString(1) + " " + c.getString(2); //almacena en un String los datos de la columna 1 del cursor "c" concatenado con los datos de la columna 2
                Integer lineaCodigo = c.getInt(0); // almacena en una variable Int los datos de la columna 0 del cursor, que se conoce que es un entero
                array[i] = linea; //almacenamos en el array las cadenas almacenadas en la variable linea
                arrayCodigo[i] = lineaCodigo; //almacenamos en el arrayCodigo el entero almacenado en la variable lineaCodigo
                i++;
            } while (c.moveToNext());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, array);
        final ArrayAdapter<Integer> adapterCodigo = new ArrayAdapter<Integer>(this, android.R.layout.simple_expandable_list_item_1, arrayCodigo);
        listaClientes = (ListView) findViewById(R.id.ListViewClientes);
        listaClientes.setAdapter(adapter);


        listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), LevantamientoPedido.class);
                Integer codigo = adapterCodigo.getItem(position);
                intent.putExtra("codigo", codigo);
                String cliente = adapter.getItem(position);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }
        });

    }
}
