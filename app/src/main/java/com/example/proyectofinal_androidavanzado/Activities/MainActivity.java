package com.example.proyectofinal_androidavanzado.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;
import com.example.proyectofinal_androidavanzado.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            goToActivity(new Intent(this,LoginActivity.class));
        }

//        crearDatosPrueba();

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

    private void crearDatosPrueba(){
        DatabaseReference proyectos = FirebaseDatabase.getInstance().getReference("proyectos");
        FirebaseAuth firebaseAuthInstacia = FirebaseAuth.getInstance();

        Proyecto proyecto = new Proyecto(
                proyectos.push().getKey(),
                firebaseAuthInstacia.getCurrentUser().getUid(),
                "Social Media Video Editor",
                "I am a professional speaker and have a lot of footage of my speaking. I am looking for an experienced video editor who can create short clips of me speaking for social media posts on instagram, Linkedin, Facebook and Pinterest. Perhaps someone to create a longer edited videos as well. I need someone who understands the video format requirements for different social media platforms and can add subtitles to the clips as well.\n" +
                        "\n" +
                        "I require strong English speaking skills so that we can communicate well.",
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
                String.valueOf(19.448372564999996),
                String.valueOf(-70.66557737499997),
                false,
                234
        );
        proyectos.child(proyecto.getId()).setValue(proyecto);

        Proyecto proyecto_1 = new Proyecto(
                proyectos.push().getKey(),
                firebaseAuthInstacia.getCurrentUser().getUid(),
                "Graphical Designer",
                "We are looking for a graphical designer to create material for our website and the campaigns running on the different channels we are present on. We need you to create product pictures, banners for the website and different material for social networks.\n" +
                        "\n" +
                        "We are looking for a candidate who can be responsive and responsible. We would like you to have proven experience with Photoshop and other Adobe products. We would be pleased to check your previous work that you can send to us upon application.\n" +
                        "\n" +
                        "About us: We are a fast growing, niche e-commerce in Europe. We are a very young and dynamic team and we are looking forward to welcoming you!",
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
                null,
                null,
                true,
                120
        );
        proyectos.child(proyecto_1.getId()).setValue(proyecto_1);

        Proyecto proyecto_2 = new Proyecto(
                proyectos.push().getKey(),
                firebaseAuthInstacia.getCurrentUser().getUid(),
                "Junior Front-End Developer",
                "** Please read carefully.\n" +
                        "\n" +
                        "We are an officially listed Shopify expert team based internationally.\n" +
                        "\n" +
                        "We are looking for a junior developer to help us deliver a small Shopify task service. We focus entirely on handling 1hr jobs for Shopify store owners.\n" +
                        "\n" +
                        "We need a team member that wants to grow with us, by doing something challenging and valuable in the Shopify and eCommerce space. We need a go-getter, and someone who wants to dive deep into the world of Shopify and deliver exceptional service to our clients.\n" +
                        "\n" +
                        "As a developer on our team, you will be in direct contact with clients (Shopify users) via email to assess and complete a wide variety of tasks on the Shopify platform. This could include small front-end theme adjustments, app integrations or small scale custom development. Your job will be to get the work done smoothly, in as few emails as possible, so we can maintain the customer as a subscriber or upsell single task buyers to subscriptions. You should be sales minded to help maintain and grow our client base. \n" +
                        "\n" +
                        "You will report to a lead developer.\n" +
                        "\n" +
                        "The work is remote, 40hrs/week, Monday to Friday. The shift to cover is between 8 and 5:30 ET (UTC-5).\n" +
                        "\n" +
                        "SKILLS REQUIRED\n" +
                        "\n" +
                        "Perfect command of written English \n" +
                        "Working knowledge CSS/HTML\n" +
                        "Comfortable with Javascript/jQuery\n" +
                        "Shopify experience an asset (but not necessary)\n" +
                        "Solid understanding of eCommerce functionality\n" +
                        "Basic Photoshop skills\n" +
                        "PERSONAL CHARACTERISTICS\n" +
                        "\n" +
                        "Team player\n" +
                        "Great communication skills\n" +
                        "Growth minded\n" +
                        "Customer service oriented\n" +
                        "Committed, professional attitude to managing the clients\n" +
                        "WHAT’S IN IT FOR YOU\n" +
                        "\n" +
                        "Undisrupted flow of tasks and clients for your skill development\n" +
                        "Constant support by coordinators and teammates\n" +
                        "Great internal training and constant access to our training materials\n" +
                        "Steady and predictable compensation\n" +
                        "Daily communication with our international team via internal communication platform\n" +
                        "Opportunity to work in the comfort of your home office\n" +
                        "For long-term contractors – paid vacation, access to medical fund and other nice perks\n" +
                        "For more, please check our company presentation: https://drive.google.com/file/d/1sdRdhBiB9jCtSPCUrKn40Pr-CP2PsXGL/view?usp=sharing\n" +
                        "\n" +
                        "If that sounds like something you would like to be part of, please contact us! Your ability to adapt quickly to the Shopify ecosystem and framework will be a major evaluation factor in the first month. Good communication skills and attitude will factor heavily in our decision.",
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
                null,
                null,
                true,
                300
        );
        proyectos.child(proyecto_2.getId()).setValue(proyecto_2);
    }
}
