package com.example.proyectofinal_androidavanzado.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.Clases.Usuario;
import com.example.proyectofinal_androidavanzado.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProyectoActivity extends AppCompatActivity{
    private static final String TAG = ProyectoActivity.class.getSimpleName();

    //para identificar el request de los permisos
    private static final int REQUEST_LOCATION_PERMISSION = 1000;
    //manejador de todo lo que tiene que ver con location
    private LocationManager locationManager;

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase_Proyecto;
    private DatabaseReference mFirebaseDatabase_Usuario;

    TextView textView_name;
    TextView textView_remoto;
    TextView textView_local;
    TextView textView_description;
    TextView textView_pagoHr;
    TextView textView_fechaPublicacion;
    TextView textView_email;
    Button btn_ubicacion;
    Proyecto proyectoActual = new Proyecto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto);
        textView_name = (TextView)findViewById(R.id.textView_nombre);
        textView_remoto = (TextView)findViewById(R.id.textView_remoto);
        textView_local = (TextView)findViewById(R.id.textView_oficina);
        textView_description = (TextView)findViewById(R.id.textView_descripcion);
        textView_pagoHr = (TextView)findViewById(R.id.textView_pagoHr);
        textView_fechaPublicacion = (TextView)findViewById(R.id.textView_fechaPublicacion);
        btn_ubicacion = (Button)findViewById(R.id.btn_ubicacion);
        textView_email = (TextView)findViewById(R.id.textView_email);

        Intent intent = getIntent();
        String idProyecto = intent.getStringExtra("idProyecto");

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase_Proyecto = mFirebaseInstance.getReference("proyectos");
        mFirebaseDatabase_Usuario = mFirebaseInstance.getReference("usuarios");
        mFirebaseDatabase_Proyecto = mFirebaseDatabase_Proyecto.child(idProyecto);
        readData();
        btn_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProyectoActivity.this, MapActivity.class);
                intent.putExtra("idProyecto", proyectoActual.getId());
                startActivity(intent);
            }
        });
    }

    private void readData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Proyecto proyecto = dataSnapshot.getValue(Proyecto.class);
                proyectoActual=proyecto;
                textView_name.setText(proyecto.getNombre());
                textView_description.setText(proyecto.getDescripcion());

                textView_fechaPublicacion.setText(proyecto.getFechaPublicacion());

                if(proyecto.isRemoto()){
                    textView_remoto.setVisibility(View.VISIBLE);//set visibility to false on create
                    textView_local.setVisibility(View.INVISIBLE);//set visibility to false on create
                    btn_ubicacion.setVisibility(View.INVISIBLE);//set visibility to false on create
                }
                else {
                    textView_remoto.setVisibility(View.INVISIBLE);//set visibility to false on create
                    textView_local.setVisibility(View.VISIBLE);//set visibility to false on create
                    btn_ubicacion.setVisibility(View.VISIBLE);//set visibility to false on create
                }
                textView_pagoHr.setText("RD$"+String.format("%.2f", proyecto.getPagoHora())+"/hr");
                mFirebaseDatabase_Usuario = mFirebaseDatabase_Usuario.child(proyecto.getIdUsuario());

                mFirebaseDatabase_Usuario.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        textView_email.setText(usuario.getEmail());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("ERROR", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ERROR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFirebaseDatabase_Proyecto.addValueEventListener(postListener);
    }
}
