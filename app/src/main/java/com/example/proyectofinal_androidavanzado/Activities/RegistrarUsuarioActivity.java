package com.example.proyectofinal_androidavanzado.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyectofinal_androidavanzado.Clases.Usuario;
import com.example.proyectofinal_androidavanzado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegistrarUsuarioActivity extends AppCompatActivity{
    private EditText email, password;
    private Button loginBtn;
    private Button btnChooseImage;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri filePath;

    FirebaseStorage firebaseStorageInstacia;
    private FirebaseAuth firebaseAuthInstacia;
    private FirebaseDatabase firebaseDatebaseInstacia;

    private static final String TAG = "RegistrarUsuarioActivity";
    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarusuario);
        initializeUI();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirImangenLocal();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });
    }
    private void initializeUI() {
        email = (EditText)findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
        btnChooseImage = (Button) findViewById(R.id.btn_chooseImage);
        imageView = (ImageView) findViewById(R.id.imgView);
        loginBtn = (Button)findViewById(R.id.btn_guardarUsuario);
        progressBar = (ProgressBar)findViewById(R.id.p_progressBar);


        //FIREBASE
        firebaseDatebaseInstacia = FirebaseDatabase.getInstance();
        firebaseAuthInstacia = FirebaseAuth.getInstance();
        firebaseStorageInstacia = FirebaseStorage.getInstance();
        
    }

    private void crearUsuario(){
        final String idImagen = uploadImage();
        firebaseAuthInstacia.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Usuario usuario = new Usuario(firebaseAuthInstacia.getCurrentUser().getUid(),email.getText().toString(),password.getText().toString(),idImagen );
                            firebaseDatebaseInstacia.getReference("usuarios").child(usuario.getId()).setValue(usuario);
                            Intent intent = new Intent(RegistrarUsuarioActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void elegirImangenLocal() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private String uploadImage() {

        String idImagen="";

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            idImagen= UUID.randomUUID().toString(); //NOMBRE CON EL QUE LA IMAGEN SE SUBIRA

            StorageReference ref = firebaseStorageInstacia.getReference().child("images/"+ idImagen);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrarUsuarioActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrarUsuarioActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        return idImagen;
    }
    


}
