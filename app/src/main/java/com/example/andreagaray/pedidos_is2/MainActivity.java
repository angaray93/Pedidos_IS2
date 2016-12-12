package com.example.andreagaray.pedidos_is2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.andreagaray.pedidos_is2.modelo.Cliente;
import com.example.andreagaray.pedidos_is2.modelo.Producto;
import com.example.andreagaray.pedidos_is2.sqlite.OperacionesBaseDatos;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private OperacionesBaseDatos datos;
    Button btnNuevo;
    Button btnListar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());


        btnNuevo= (Button) findViewById(R.id.botonNuevo);
        btnNuevo.setOnClickListener(this);

        btnListar= (Button) findViewById(R.id.botonListar);
        btnListar.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.botonNuevo:
                Intent intent= new Intent(this, SeleccionCliente.class);
                startActivity(intent);
                break;
            case R.id.botonListar:
                Intent intent2= new Intent(this, ListarPedidos.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}

