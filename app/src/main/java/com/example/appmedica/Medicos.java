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
    private ArrayAdapter<String> adapter;
    private String especialidad;  // Nueva variable para la especialidad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos);

        listViewMedicos = findViewById(R.id.lv_medicos);

        medicosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicosList);
        listViewMedicos.setAdapter(adapter);

        // Obtener la especialidad desde el Intent
        Intent intent = getIntent();
        especialidad = intent.getStringExtra("especialidad");

        if (especialidad != null && !especialidad.isEmpty()) {
            obtenerMedicosPorEspecialidad(especialidad);
        } else {
            Toast.makeText(this, "Especialidad no recibida", Toast.LENGTH_SHORT).show();
        }
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

                            // Iterar sobre los resultados y agregarlos a la lista
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject medico = response.getJSONObject(i);
                                String nombreCompleto = medico.getString("nombreMed") + " " +
                                        medico.getString("apellidoPat") + " " +
                                        medico.getString("apellidoMat");
                                medicosList.add(nombreCompleto);
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
