package com.example.proyectofinal_androidavanzado.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearProyectoActivity extends AppCompatActivity implements LocationListener {

    private EditText nombre;
    private EditText descripcion;
    private CheckBox remoto;
    private EditText pagoHr;
    private EditText latitud;
    private EditText longitud;
    private Button btnGuardar;
    private Button btnUbicacionActual;

    private DatabaseReference proyectos;
    private FirebaseDatabase firebaseDatebaseInstacia;
    private FirebaseAuth firebaseAuthInstacia;

    private static final String TAG = "CrearProyectoActivity";

    //Para identificar el request de los permisos
    private static final int REQUEST_LOCATION_PERMISSION = 1000;
    //Manejador de lo que tiene que ver con location
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearproyecto);
        initializeUI();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearProyecto();
                startActivity(new Intent(CrearProyectoActivity.this, ListadoProyectosActivity.class));
            }
        });
        btnUbicacionActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLastKnowLocation(null);
            }
        });

    }
    private void initializeUI() {
        nombre = (EditText)findViewById(R.id.editText_nombre);
        descripcion = (EditText)findViewById(R.id.editText_descripcion);
        pagoHr = (EditText)findViewById(R.id.editText_pagoHr);
        latitud = (EditText)findViewById(R.id.editText_latitud);
        longitud = (EditText)findViewById(R.id.editText_logitud);
        remoto = (CheckBox) findViewById(R.id.editText_remoto);
        btnGuardar=(Button)findViewById(R.id.btn_guardarProyecto);
        btnUbicacionActual=(Button)findViewById(R.id.btn_buscarUbicacionActual);

        //FIREBASE
        firebaseDatebaseInstacia = FirebaseDatabase.getInstance();
        firebaseAuthInstacia = FirebaseAuth.getInstance();
        proyectos = firebaseDatebaseInstacia.getReference("proyectos");

        //Obtener el location manager desde el sistema
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!hasPermission())
            this.requestForPermission();
    }

    private void crearProyecto(){
        Proyecto proyecto = new Proyecto(
                proyectos.push().getKey(),
                firebaseAuthInstacia.getCurrentUser().getUid(),
                nombre.getText().toString(),
                descripcion.getText().toString(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
                latitud.getText().toString(),
                longitud.getText().toString(),
                remoto.isChecked() ?true:false,
                Double.parseDouble(pagoHr.getText().toString())
        );
        proyectos.child(proyecto.getId()).setValue(proyecto);
    }

    //solicitar permisos para usar la ubicacion
    private void requestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_LOCATION_PERMISSION
            );
        }
    }

    //verificar si tengo permisos
    private boolean hasPermission() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    //mostrar el location en el textview
    private void showLocation(Location location){
        latitud.setText(String.format("%s", location.getLatitude()));
        longitud.setText(String.format("%s", location.getLongitude()));
    }

    //mostrar la ultima ubicacion
    @SuppressLint("MissingPermission")
    public void showLastKnowLocation(View view) {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        showLocation(location);
    }

    //actualizar loction
    @SuppressLint("MissingPermission")
    public void updateLocation(View view){
        long minTime      = 1000;
        float minDistance = 0.f;
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                this
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // asumimos que el usuario acepto los permisso
        // de querer manejar esto mejor ver la siguiente documentacion
        // https://developer.android.com/guide/topics/permissions/overview
        showLastKnowLocation(null);
    }


    //METODOS DEL LOCATIONLISTENER
    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.wtf(TAG, provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.wtf(TAG, provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.wtf(TAG, provider);

    }
}
