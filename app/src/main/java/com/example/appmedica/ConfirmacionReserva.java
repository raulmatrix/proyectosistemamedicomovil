package com.example.appmedica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfirmacionReserva extends AppCompatActivity {

    private TextView textViewDetallesReserva;
    private String nombreUsuario;
    private String especialidad;
    private String nombreMedico;
    private String horario; // Este debe ser el idHorarios, no la hora en sí
    private int idMedico;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_reserva);

        textViewDetallesReserva = findViewById(R.id.textViewDetallesReserva);

        // Obtener los detalles desde el Intent
        Intent intent = getIntent();
        nombreMedico = intent.getStringExtra("nombreMedico");
        idMedico = intent.getIntExtra("idMedico", -1);
        especialidad = intent.getStringExtra("especialidad");
        horario = intent.getStringExtra("horario"); // Asegúrate que esto es el idHorarios
        idUsuario = intent.getIntExtra("idUsuario", -1);
        nombreUsuario = intent.getStringExtra("nombreUsuario");

        // Mensajes de depuración
        Log.d("ConfirmacionReserva", "ID Usuario: " + idUsuario);
        Log.d("ConfirmacionReserva", "Nombre Usuario: " + nombreUsuario);

        // Verificar si los detalles llegaron correctamente
        if (nombreMedico == null || especialidad == null || horario == null || idMedico == -1 || idUsuario == -1) {
            Toast.makeText(this, "Error: Faltan detalles para la reserva", Toast.LENGTH_LONG).show();
        } else {
            // Mostrar los detalles de la reserva
            String detallesReserva = "Usuario: " + nombreUsuario + "\n" +
                    "Médico: " + nombreMedico + "\n" +
                    "Especialidad: " + especialidad + "\n" +
                    "Horario: " + horario; // Asegúrate que esto es el idHorarios
            textViewDetallesReserva.setText(detallesReserva);

            // Realizar la inserción en la base de datos
            insertarReservaEnBD(idUsuario, idMedico, horario); // Asegúrate que horario es idHorarios
        }
    }

    // Método para insertar la reserva en la base de datos
    private void insertarReservaEnBD(int idUsuario, int idMedico, String horario) {
        // URL del servidor para realizar la inserción de la reserva
        String URL = Config.BASE_URL + "/sistemamedico/remoto_reserva.php";

        // Crear la solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Mostrar la respuesta del servidor para depuración
                        Log.d("ConfirmacionReserva", "Respuesta del servidor: " + response);
                        // Mostrar mensaje de éxito
                        Toast.makeText(getApplicationContext(), "Reserva realizada con éxito", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error
                Toast.makeText(getApplicationContext(), "Error al realizar la reserva: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                // Crear un objeto SimpleDateFormat con el formato deseado
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta el formato según tus necesidades

                // Obtener la fecha actual
                String fechaActual = dateFormat.format(new Date());

                // Agregar los parámetros de la reserva
                params.put("idUsuario", String.valueOf(idUsuario));  // ID del usuario
                params.put("idMedico", String.valueOf(idMedico));    // ID del médico
                params.put("horario", horario);                      // Asegúrate que esto es el idHorarios
                params.put("fecha", fechaActual);                    // Fecha actual
                params.put("estado", "activo");                      // Estado inicial de la reserva

                return params;
            }
        };

        // Añadir la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
