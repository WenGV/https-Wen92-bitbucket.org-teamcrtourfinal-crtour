package com.projectandroid.kevinjimezb.appcrtour;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilNombre;
    private TextInputLayout tilApellido;
    private TextInputLayout tilTelefono;
    private TextInputLayout tilCorreo;
    private TextInputLayout tilEdad;
    private TextInputLayout tilPassword;
    private EditText campoNombre;
    private EditText campoApellido;
    private EditText campoTelefono;
    private EditText campoCorreo;
    private EditText campoEdad;
    private EditText campoPassword;
    private PrefManager prefManager;
    private String idGenero;
    private RadioGroup rgSexo;

    String item;
    private RequestQueue requestQueue;
    private static final String URL = "http://192.168.1.10:8080/APIcrtour/Insertuser.php";
    private StringRequest request;
    int itemPosition;
    JSONObject result;

     MaterialBetterSpinner materialBetterSpinner ;

    String[] SPINNER_DATA = {"CR","USA"};

    MaterialBetterSpinner materialBetterSpinner1 ;

    String[] SPINNER_DATA1 = {"Hombre","Mujer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Referencias TILs
       tilNombre = (TextInputLayout) findViewById(R.id.til_nombre);
       // tilApellido = (TextInputLayout) findViewById(R.id.til_apellido);
       // tilTelefono = (TextInputLayout) findViewById(R.id.til_telefono);
        tilCorreo = (TextInputLayout) findViewById(R.id.til_correo);
      //  tilEdad = (TextInputLayout) findViewById(R.id.til_edad);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);

        // Referencias ETs
        campoNombre = (EditText) findViewById(R.id.campo_nombre);
       // campoApellido = (EditText) findViewById(R.id.campo_apellido);
       /// campoTelefono = (EditText) findViewById(R.id.campo_telefono);
        campoCorreo = (EditText) findViewById(R.id.campo_correo);
      //  campoEdad = (EditText) findViewById(R.id.campo_edad);
        campoPassword = (EditText) findViewById(R.id.campo_password);

        rgSexo = (RadioGroup) findViewById(R.id.rgSexo);
        rgSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                checkedId=rgSexo.getCheckedRadioButtonId();

                switch (checkedId){
                    case R.id.man:
                        idGenero = "1";
                        break;
                    case R.id.fem:
                        idGenero = "2";
                        break;
                }
            }
        });
//        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);
//        materialBetterSpinner1 = (MaterialBetterSpinner)findViewById(R.id.material_spinner2);

//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);
//
//        materialBetterSpinner.setAdapter(adapter);
//
//        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA1);
//
//        materialBetterSpinner1.setAdapter(adapter1);




        campoNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilNombre.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        campoCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esCorreoValido(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        requestQueue = Volley.newRequestQueue(this);

        // Referencia Botón
        Button botonAceptar = (Button) findViewById(R.id.boton_aceptar);

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDatos())
                {
                    request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error to get JSON" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("nombre", campoNombre.getText().toString());
                            hashMap.put("apellido", "Moraga");//Campo quemado
                            hashMap.put("telefono", "12345678");//Campo quemado
                            hashMap.put("correo", campoCorreo.getText().toString());
                            hashMap.put("nacionalidad", "1");//Campo quemado
                            hashMap.put("edad", "22");//Campo quemado
                            hashMap.put("sexo", idGenero.toString());//Campo quemado
                            hashMap.put("password", campoPassword.getText().toString());

                            return hashMap;
                        }

                    };


                    requestQueue.add(request);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Datos invalidos, verifiquelos", Toast.LENGTH_LONG).show();
                }

            }
        });


    }//fin

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
            tilNombre.setError("Nombre inválido");
            return false;
        } else {
            tilNombre.setError(null);
        }

        return true;
    }

    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.setError("Correo electrónico inválido");
            return false;
        } else {
            tilCorreo.setError(null);
        }

        return true;
    }

    private boolean validarDatos() {
        String nombre = tilNombre.getEditText().getText().toString();
        String correo = tilCorreo.getEditText().getText().toString();

        boolean validateName = esNombreValido(nombre);
        boolean validateMail = esCorreoValido(correo);

        if (validateName && validateMail) {
            // OK, se pasa validateName la siguiente acción
            Toast.makeText(this, "Tu cuenta ha sido creada!", Toast.LENGTH_LONG).show();
            Intent goToMap=new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(goToMap);
            finish();
            return true;

        }

        return false;
    }

}//fin
