package com.example.proyectofinal_androidavanzado.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.Clases.Usuario;
import com.example.proyectofinal_androidavanzado.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ProyectoActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatebaseInstacia;
    DatabaseReference usuarios;
    DatabaseReference proyectos;
    DatabaseReference proyectoSeleccionado;

    TextView textView_name;
    TextView textView_remoto;
    TextView textView_local;
    TextView textView_description;
    TextView textView_pagoHr;
    TextView textView_fechaPublicacion;
    TextView textView_email;
    Button btn_ubicacion;
    Button emailBtn;
    Proyecto proyectoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto);
        initializeUI();


        readData();
        btn_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProyectoActivity.this, MapActivity.class);
                intent.putExtra("idProyecto", proyectoActual.getId());
                startActivity(intent);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailto = "mailto:bob@example.org" +
                        "?cc=" + "alice@example.com" +
                        "&subject=" + Uri.encode(proyectoActual.getDescripcion()) +
                        "&body=" + Uri.encode(proyectoActual.getDescripcion());

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                }
            }
        });
    }

    private void initializeUI() {
        textView_name = (TextView)findViewById(R.id.textView_nombre);
        textView_remoto = (TextView)findViewById(R.id.textView_remoto);
        textView_local = (TextView)findViewById(R.id.textView_oficina);
        textView_description = (TextView)findViewById(R.id.textView_descripcion);
        textView_pagoHr = (TextView)findViewById(R.id.textView_pagoHr);
        textView_fechaPublicacion = (TextView)findViewById(R.id.textView_fechaPublicacion);
        btn_ubicacion = (Button)findViewById(R.id.btn_ubicacion);
        emailBtn = (Button)findViewById(R.id.btn_email);
        textView_email = (TextView)findViewById(R.id.textView_email);

        //FIREBASE
        firebaseDatebaseInstacia = FirebaseDatabase.getInstance();
        usuarios = firebaseDatebaseInstacia.getReference("usuarios");
        proyectos = firebaseDatebaseInstacia.getReference("proyectos");
        Intent intent = getIntent();
        String idProyecto = intent.getStringExtra("idProyecto");
        proyectoSeleccionado = proyectos.child(idProyecto);

        proyectoActual = new Proyecto();
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
                textView_pagoHr.setText("RD$"+String.format("%.2f", proyecto.getPagoHora())+"/hr");

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
                DatabaseReference usuario = usuarios.child(proyecto.getIdUsuario());
                usuario.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        textView_email.setText(usuario.getEmail());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("ERROR", "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERROR", "loadPost:onCancelled", databaseError.toException());
            }
        };
        proyectoSeleccionado.addValueEventListener(postListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_action_provider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Oferta de trabajo!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, proyectoActual.getNombre() + "\n" + proyectoActual.getDescripcion() +"\nPago/hr: "+"RD$"+String.format("%.2f", proyectoActual.getPagoHora())+"/hr");
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
