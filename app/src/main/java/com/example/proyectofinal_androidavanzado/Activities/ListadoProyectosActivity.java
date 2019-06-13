package com.example.proyectofinal_androidavanzado.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.R;
import com.example.proyectofinal_androidavanzado.RecycleViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListadoProyectosActivity extends AppCompatActivity implements RecycleViewAdapter.Listener {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    RecycleViewAdapter recycleViewAdapter;


    List<Proyecto> listadoProyectos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadoproyectos);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("proyectos");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");


//        final Proyecto proyecto = new Proyecto(
//                mFirebaseDatabase.push().getKey(),
//                "eCommerce Google Ads Manager",
//                "Hello Google Ads Experts! Aces Media is looking for a Google Advertiser who has extensive experience with researching, creating, optimizing, and scaling Google Ad campaigns for eCommerce companies and generating profitable results. We have a large list of eCommerce clients who are needing their Google ads to be managed by a designated expert. So, if you have managed large budgets ($100k+) and have been able to generate good results please apply for this position. If you have not had any experience running profitable Google ad campaigns, please DO NOT apply. The candidate that is chosen for this position will be working full-time remotely to deliver Google Ads results to our eCommerce clients and communicate with our team of account managers regularly. Upon applying for this position please share with us some of your past results you have been able to generate on Google and a brief overview of your strategy from creation to scaling. Thanks, Aces Media https://acesmedia.co",
//                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
//                true,
//                100
//
//        );
//        mFirebaseDatabase.child(proyecto.getId()).setValue(proyecto);

        readData();
//        Button logout = (Button)findViewById(R.id.p_logout);
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(ListadoProyectosActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });
        recycleViewAdapter = new RecycleViewAdapter(getApplicationContext(),listadoProyectos,this);

        RecyclerView rv= findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(recycleViewAdapter);

    }

    @Override
    public void onClick(Proyecto proyecto) {
//        Toast.makeText(this,proyecto.getNombre(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,proyecto.getId(), Toast.LENGTH_SHORT).show();
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

                recycleViewAdapter.setListadoProyectos(listadoProyectos);
                recycleViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ERROR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFirebaseDatabase.addValueEventListener(postListener);
    }
}
