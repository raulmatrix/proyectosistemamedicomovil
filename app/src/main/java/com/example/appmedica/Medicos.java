package com.example.appmedica;

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

    private ListView listViewUsuarios;
    private ArrayList<String> usuariosList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos);

        listViewUsuarios = findViewById(R.id.lv_medicos);

        usuariosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuariosList);
        listViewUsuarios.setAdapter(adapter);


        obtenerUsuarios();
    }

    private void obtenerUsuarios() {
        String URL = "http://192.168.218.236:9191/sistemamedico/remoto_list_medicos.php";

        // Crear una solicitud de JSON Array
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Iterar sobre los resultados y agregarlos a la lista
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject usuario = response.getJSONObject(i);
                                String nombreCompleto = usuario.getString("nombreMed") + " " +
                                        usuario.getString("apellidoPat") + " " +
                                        usuario.getString("apellidoMat");
                                usuariosList.add(nombreCompleto);
                            }

                            // Notificar al adaptador que los datos han cambiado
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
                        Toast.makeText(getApplicationContext(), "Error al obtener usuarios: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
