package com.example.proyectofinal_androidavanzado.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.Clases.Usuario;
import com.example.proyectofinal_androidavanzado.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String latitud="";
    String longitud="";

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase_Proyecto;
    private DatabaseReference mFirebaseDatabase_Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String idProyecto = intent.getStringExtra("idProyecto");
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase_Proyecto = mFirebaseInstance.getReference("proyectos");
        mFirebaseDatabase_Proyecto = mFirebaseDatabase_Proyecto.child(idProyecto);


        readData();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(getApplicationContext(),latitud+longitud,Toast.LENGTH_LONG).show();
        double lat = Double.parseDouble(latitud);
        double longi = Double.parseDouble(longitud);


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(lat,longi);
//        LatLng sydney = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void readData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Proyecto proyecto = dataSnapshot.getValue(Proyecto.class);
                latitud = proyecto.getLatitud();
                longitud=proyecto.getLongitud();

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapActivity.this);
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
