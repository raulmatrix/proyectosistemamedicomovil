package com.example.appmedica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Principal extends AppCompatActivity {
    ImageButton btnImg;
    TextView tvusu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnImg = findViewById(R.id.btnIngresoImg);
        tvusu = findViewById(R.id.tv_usuario);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", -1);
        String nombreUsuario = intent.getStringExtra("nombreUsuario");

        // Mostrar el nombre del usuario en el TextView
        if (nombreUsuario != null) {
            tvusu.setText("Usuario: " + nombreUsuario);
        }

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), Especialidad.class);
                // Puedes pasar el idUsuario si lo necesitas en otras actividades
                it.putExtra("idUsuario", idUsuario);
                it.putExtra("nombreUsuario", nombreUsuario);
                startActivity(it);
            }
        });
    }
}
