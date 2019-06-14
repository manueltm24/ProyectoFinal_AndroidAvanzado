package com.example.proyectofinal_androidavanzado.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.R;
import com.example.proyectofinal_androidavanzado.RecycleViewListadoProyectosAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListadoProyectosActivity extends AppCompatActivity implements RecycleViewListadoProyectosAdapter.Listener {

    private DatabaseReference proyectos;
    private FirebaseDatabase firebaseDatebaseInstacia;
    RecycleViewListadoProyectosAdapter recycleViewListadoProyectosAdapter;

    ImageButton btn_exit;


    List<Proyecto> listadoProyectos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadoproyectos);
        initializeUI();
        readData();

        recycleViewListadoProyectosAdapter = new RecycleViewListadoProyectosAdapter(getApplicationContext(),listadoProyectos,this);
        RecyclerView rv= findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(recycleViewListadoProyectosAdapter);
    }

    @Override
    public void onClick(Proyecto proyecto) {
        Intent intent = new Intent(ListadoProyectosActivity.this, ProyectoActivity.class);
        intent.putExtra("idProyecto",proyecto.getId());
        startActivity(intent);
    }

    private void readData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Proyecto proyecto = postSnapshot.getValue(Proyecto.class);
                    listadoProyectos.add(proyecto);

                    Log.d("PROYECTO",proyecto.getNombre());
                }
                Log.d("CANTIDAD DE PROYECTOS",String.valueOf(listadoProyectos.size()));

                recycleViewListadoProyectosAdapter.setListadoProyectos(listadoProyectos);
                recycleViewListadoProyectosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ERROR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        proyectos.addValueEventListener(postListener);
    }

    private void initializeUI() {
        firebaseDatebaseInstacia = FirebaseDatabase.getInstance();
        proyectos = firebaseDatebaseInstacia.getReference("proyectos");

    }
}
