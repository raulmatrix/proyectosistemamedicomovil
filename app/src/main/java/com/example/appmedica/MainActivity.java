package com.example.appmedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText usuario, passw;
    Button btnIng, btnInsertar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        usuario = findViewById(R.id.edtUsuario);
        passw = findViewById(R.id.edtPassword);
        btnIng = findViewById(R.id.btnIng);
        btnInsertar =findViewById(R.id.btnCrearUsu);

        btnIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarLogin("http://192.168.0.101:9191/sistemamedico/remoto_validalogin.php");
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(),RegistroUsuario.class);
                startActivity(it);
            }
        });



    }

    private void validarLogin(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        // Autenticación exitosa
                        Intent intent = new Intent(getApplicationContext(), Principal.class);
                        startActivity(intent);
                    } else {
                        // Autenticación fallida
                        Toast.makeText(MainActivity.this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("password", passw.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




}
