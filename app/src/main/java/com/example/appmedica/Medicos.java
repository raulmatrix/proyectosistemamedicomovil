package com.example.appmedica;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Medicos extends AppCompatActivity {

    private ListView listViewMedicos;
    private ArrayList<String> medicosList;
    private ArrayList<Integer> medicosIdList; // Nueva lista para almacenar los IDs de los médicos
    private ArrayAdapter<String> adapter;
    private String especialidad;
    private int idUsuario; // Para almacenar el idUsuario
    private String nombreUsuario; // Para almacenar el nombreUsuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos);

        listViewMedicos = findViewById(R.id.lv_medicos);

        medicosList = new ArrayList<>();
        medicosIdList = new ArrayList<>(); // Inicializamos la lista de IDs de médicos

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicosList);
        listViewMedicos.setAdapter(adapter);

        // Obtener los datos desde el Intent
        Intent intent = getIntent();
        especialidad = intent.getStringExtra("especialidad");
        idUsuario = intent.getIntExtra("idUsuario", -1); // Recibir el idUsuario
        nombreUsuario = intent.getStringExtra("nombreUsuario"); // Recibir el nombreUsuario

        if (especialidad != null && !especialidad.isEmpty()) {
            obtenerMedicosPorEspecialidad(especialidad);
        } else {
            Toast.makeText(this, "Especialidad no recibida", Toast.LENGTH_SHORT).show();
        }

        // Configurar el listener para el click en un médico
        listViewMedicos.setOnItemClickListener((parent, view, position, id) -> {
            String nombreMedico = medicosList.get(position); // Obtener el nombre completo del médico seleccionado
            int idMedico = medicosIdList.get(position); // Obtener el id del médico seleccionado

            // Enviar al siguiente Activity el nombre y el ID del médico, además de idUsuario y nombreUsuario
            Intent intent2 = new Intent(Medicos.this, Horarios.class);
            intent2.putExtra("nombreMedico", nombreMedico); // Pasar el nombre del médico
            intent2.putExtra("idMedico", idMedico); // Pasar el ID del médico
            intent2.putExtra("idUsuario", idUsuario); // Pasar el idUsuario
            intent2.putExtra("nombreUsuario", nombreUsuario); // Pasar el nombreUsuario
            intent2.putExtra("especialidad", especialidad); // Pasar la especialidad
            startActivity(intent2); // Iniciar el nuevo Activity
        });
    }

    // Método para obtener médicos según la especialidad
    private void obtenerMedicosPorEspecialidad(String especialidad) {
        // Incluir la especialidad en la URL como un parámetro GET
        String URL = Config.BASE_URL + "/sistemamedico/remoto_list_medicos.php?especialidad=" + especialidad;

        // Crear una solicitud de JSON Array
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Limpiar la lista antes de agregar nuevos datos
                            medicosList.clear();
                            medicosIdList.clear(); // Limpiar también la lista de IDs de médicos

                            // Iterar sobre los resultados y agregarlos a la lista
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject medico = response.getJSONObject(i);

                                // Obtener los datos del médico
                                String nombreCompleto = medico.getString("nombreMed") + " " +
                                        medico.getString("apellidoPat") + " " +
                                        medico.getString("apellidoMat");
                                int idMedico = medico.getInt("idMedico");

                                // Agregar a las listas
                                medicosList.add(nombreCompleto);
                                medicosIdList.add(idMedico);
                            }

                            // Notificar al adaptador que los datos cambiaron
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al procesar los datos", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al obtener médicos: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Añadir la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
