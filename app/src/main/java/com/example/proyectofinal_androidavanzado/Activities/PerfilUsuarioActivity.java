package com.example.proyectofinal_androidavanzado.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.proyectofinal_androidavanzado.Clases.Usuario;
import com.example.proyectofinal_androidavanzado.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;



public class PerfilUsuarioActivity extends AppCompatActivity {
    private DatabaseReference usuarioSeleccionado;
    private FirebaseDatabase firebaseDatebaseInstacia;
    private FirebaseAuth firebaseAuthInstacia;
    FirebaseStorage firebaseStorageInstacia;
    TextView textView_email;
    ImageView my_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilusuario);
        initializeUI();
        readData();

    }

    private void readData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                textView_email.setText(usuario.getEmail());

                firebaseStorageInstacia.getReference().child("images/"+usuario.getIdImagen()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.get().load(uri.toString()).into(my_image);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
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
        usuarioSeleccionado.addValueEventListener(postListener);



    }

    private void initializeUI() {
        textView_email = (TextView)findViewById(R.id.textView_email);
        my_image=(ImageView)findViewById(R.id.my_image);


        //FIREBASE
        firebaseDatebaseInstacia = FirebaseDatabase.getInstance();
        firebaseAuthInstacia = FirebaseAuth.getInstance();
        usuarioSeleccionado = firebaseDatebaseInstacia.getReference("usuarios").child(firebaseAuthInstacia.getCurrentUser().getUid());
        firebaseStorageInstacia = FirebaseStorage.getInstance();

    }
}
