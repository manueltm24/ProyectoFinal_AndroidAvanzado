package com.example.proyectofinal_androidavanzado.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.Clases.Usuario;
import com.example.proyectofinal_androidavanzado.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase_Usuario;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;

    FirebaseStorage storage;
    StorageReference storageRef;
    TextView textView_email;
    ImageView my_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilusuario);
        textView_email = (TextView)findViewById(R.id.textView_email);
        my_image=(ImageView)findViewById(R.id.my_image);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase_Usuario = mFirebaseInstance.getReference("usuarios");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase_Usuario = mFirebaseDatabase_Usuario.child(mAuth.getCurrentUser().getUid());

        readData();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();




    }

    private void readData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                textView_email.setText(usuario.getEmail());

                storageRef.child("images/"+usuario.getIdImagen()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        mFirebaseDatabase_Usuario.addValueEventListener(postListener);
    }
}
