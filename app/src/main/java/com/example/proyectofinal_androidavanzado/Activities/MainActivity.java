package com.example.proyectofinal_androidavanzado.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.proyectofinal_androidavanzado.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            goToActivity(new Intent(this,LoginActivity.class));
        }
//        else{
//            goToActivity(new Intent(this,LoginActivity.class));
//
//        }

    }

    public void gotoPerfil(View view){
        goToActivity(new Intent(this,PerfilUsuarioActivity.class));
    }

    public void gotoCerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                goToActivity(intent);
                finish();
        goToActivity(new Intent(this,MainActivity.class));
    }


    public void gotoCrearProyecto(View view){
        goToActivity(new Intent(this,CrearProyectoActivity.class));
    }
    public void gotoListadoProyectos(View view){
        goToActivity(new Intent(this,ListadoProyectosActivity.class));
    }
    private void goToActivity(Intent intent){
        startActivity(intent);
    }
}
