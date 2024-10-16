package com.example.appmedica;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Horarios extends AppCompatActivity {

    private TextView textViewNombreMedico;
    private ListView listViewHorarios;
    private String nombreMedico;
    private int idMedico;
    private String especialidad;
    private int idUsuario; // Para almacenar el idUsuario
    private String nombreUsuario; // Para almacenar el nombreUsuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        textViewNombreMedico = findViewById(R.id.textViewNombreMedico);
        listViewHorarios = findViewById(R.id.listViewHorarios);

        // Obtener el Intent que viene de la actividad Medicos y extraer los datos
        Intent intent = getIntent();
        nombreMedico = intent.getStringExtra("nombreMedico");
        idMedico = intent.getIntExtra("idMedico", -1);
        especialidad = intent.getStringExtra("especialidad");
        idUsuario = intent.getIntExtra("idUsuario", -1); // Recibir idUsuario
        nombreUsuario = intent.getStringExtra("nombreUsuario"); // Recibir nombreUsuario

        // Verificar si se recibieron correctamente los datos y mostrarlos
        if (nombreMedico != null) {
            textViewNombreMedico.setText(nombreMedico);
        } else {
            Toast.makeText(this, "Error: No se recibió el nombre del médico", Toast.LENGTH_LONG).show();
        }

        // Aquí podrías mostrar o usar el idUsuario y nombreUsuario si es necesario
        // Por ejemplo:
        Toast.makeText(this, "Usuario: " + nombreUsuario + " (ID: " + idUsuario + ")", Toast.LENGTH_LONG).show();

        // Obtener y mostrar los horarios
        List<String> horarios = obtenerHorarios();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, horarios);
        listViewHorarios.setAdapter(adapter);

        // Listener para cuando se selecciona un horario
        listViewHorarios.setOnItemClickListener((parent, view, position, id) -> {
            String horarioSeleccionado = horarios.get(position);

            // Crear un intent para enviar los detalles a la actividad ConfirmacionReserva
            Intent intentConfirmacion = new Intent(Horarios.this, ConfirmacionReserva.class);
            intentConfirmacion.putExtra("nombreMedico", nombreMedico);
            intentConfirmacion.putExtra("idMedico", idMedico);  // Pasa el ID del médico
            intentConfirmacion.putExtra("especialidad", especialidad);
            intentConfirmacion.putExtra("horario", horarioSeleccionado);
            intentConfirmacion.putExtra("idUsuario", idUsuario); // Pasa el idUsuario
            intentConfirmacion.putExtra("nombreUsuario", nombreUsuario); // Pasa el nombreUsuario

            // Iniciar la actividad de confirmación
            startActivity(intentConfirmacion);
        });
    }

    // Método para generar los horarios
    private List<String> obtenerHorarios() {
        List<String> horarios = new ArrayList<>();
        int startHour = 8;
        int endHour = 16;

        for (int hour = startHour; hour < endHour; hour++) {
            horarios.add(hour + ":00 AM");
            horarios.add(hour + ":30 AM");
        }
        horarios.add(endHour + ":00 PM");
        return horarios;
    }
}
