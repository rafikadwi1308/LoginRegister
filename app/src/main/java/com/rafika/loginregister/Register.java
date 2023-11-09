package com.rafika.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Register extends AppCompatActivity {

 DatePickerDialog datePickerDialog;
 SimpleDateFormat dateFormatter;
 private EditText etnama, etgllahir, etemail, etalamat, etpass;
 private RadioGroup gender, jab;
 private RadioButton jeniskel, jabatan;
 private Button register;
 TextView elogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etgllahir = findViewById(R.id.tanggal_lahir);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        etgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showDateDialog();

            }
        });


        etnama = findViewById(R.id.nama);
        etgllahir = findViewById(R.id.tanggal_lahir);
        etemail = findViewById(R.id.email);
        etalamat = findViewById(R.id.alamat);
        gender = findViewById(R.id.radio_grup);
        jab = findViewById(R.id.radio_jab);
        etpass = findViewById(R.id.password);
        register = findViewById(R.id.btn_register);
        elogin = findViewById(R.id.log);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioid = gender.getCheckedRadioButtonId();
                jeniskel = (RadioButton) findViewById(radioid);
                String name = etnama.getText().toString();
                String lahir = etgllahir.getText().toString();
                String email = etemail.getText().toString();
                String alamat = etalamat.getText().toString();
                String gen = jeniskel.getText().toString();
                String jbtn = jabatan.getText().toString();
                String pass = etpass.getText().toString();

                if(!(name.isEmpty() || lahir.isEmpty() || email.isEmpty() || alamat.isEmpty() || gen.isEmpty() || jbtn.isEmpty() || pass.isEmpty())) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlRegister, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        protected HashMap<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();

                            params.put("nama", name);
                            params.put("tanggal_lahir", lahir);
                            params.put("email", email);
                            params.put("alamat", alamat);
                            params.put("jenis_kelamin", gen);
                            params.put("jabatan", jbtn);
                            params.put("password", pass);

                            return params;

                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                } else {
                    Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        elogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                etgllahir.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH, calendar.get(Calendar.DAY_OF_MONTH)));
        datePickerDialog.show();;
    }
}