package com.example.appmedica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Especialidad extends AppCompatActivity {

    private ListView listViewEspecialidad;
    private String[] especialidades;
    private int idUsuario; // Para almacenar el idUsuario
    private String nombreUsuario; // Para almacenar el nombreUsuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidad);

        listViewEspecialidad = findViewById(R.id.lv_especialidad);

        // Obtener el array de especialidades
        especialidades = getResources().getStringArray(R.array.especialidad);

        // Obtener los datos de idUsuario y nombreUsuario desde la actividad anterior (Principal)
        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("idUsuario", -1);
        nombreUsuario = intent.getStringExtra("nombreUsuario");

        // Agregar un listener para los clics en las especialidades
        listViewEspecialidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener la especialidad seleccionada
                String especialidadSeleccionada = especialidades[position];

                // Mostrar un mensaje (opcional)
                Toast.makeText(Especialidad.this, "Seleccionaste: " + especialidadSeleccionada, Toast.LENGTH_SHORT).show();

                // Crear un intent para navegar a la Activity de Medicos
                Intent intent = new Intent(Especialidad.this, Medicos.class);

                // Pasar la especialidad seleccionada, idUsuario y nombreUsuario al siguiente Activity
                intent.putExtra("especialidad", especialidadSeleccionada);
                intent.putExtra("idUsuario", idUsuario);
                intent.putExtra("nombreUsuario", nombreUsuario);

                // Iniciar la nueva Activity
                startActivity(intent);
            }
        });
    }
}
