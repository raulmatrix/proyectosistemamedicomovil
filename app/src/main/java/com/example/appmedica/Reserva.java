package com.example.appmedica;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Reserva extends AppCompatActivity {

    private TextView textViewNombreUsuario;
    private TextView textViewEspecialidad;
    private TextView textViewHorario;
    private Button buttonConfirmarReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario);
        textViewEspecialidad = findViewById(R.id.textViewEspecialidad);
        textViewHorario = findViewById(R.id.textViewHorario);
        buttonConfirmarReserva = findViewById(R.id.buttonConfirmarReserva);

        // Obtener datos del Intent
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        String especialidad = getIntent().getStringExtra("especialidad");
        String horario = getIntent().getStringExtra("horario");

        textViewNombreUsuario.setText(nombreUsuario);
        textViewEspecialidad.setText(especialidad);
        textViewHorario.setText(horario);

        buttonConfirmarReserva.setOnClickListener(v -> {
            // Aquí debes implementar la lógica para guardar la reserva en la base de datos
            guardarReserva(nombreUsuario, especialidad, horario);
        });
    }

    private void guardarReserva(String nombreUsuario, String especialidad, String horario) {
        String URL = Config.BASE_URL + "/sistemamedico/guardar_reserva.php"; // Asegúrate de crear este archivo PHP

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // Procesar la respuesta del servidor
                    Toast.makeText(Reserva.this, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad actual
                },
                error -> Toast.makeText(Reserva.this, "Error al realizar la reserva: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombreUsuario", nombreUsuario);
                params.put("especialidad", especialidad);
                params.put("horario", horario);
                // Aquí puedes agregar los demás parámetros necesarios, como `Usuario_idUsuario`, `Medico_idMedico`, etc.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
