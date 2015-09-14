package com.example.gabriel.ribbit.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.gabriel.ribbit.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;


public class ViewImageActivity extends AppCompatActivity {

    public static final String TAG  = ViewImageActivity.class.getSimpleName();
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        //Activa el toolbar y lo pone como actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        //Activa para poder regresar a su actividad padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        //Consigue el ImageView del layout
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        /*URI es una cadena de caracteres que identifica los recursos de una red de forma unívoca.
        *El codigo abajo consigue el intent de la actividad que inicio esta y consigue
        *la data necesaria para usar osea el Uri*/
        Uri imageUri = getIntent().getData();

        /*Picasso es una libreria utilizado para poder visualizar imagenes almacenas en intener
        * El codigo siguiente la imagen de internet
         * La explicacion seria con(EstaActividad).carga(Uri.toString).hacia(ImageView)*/
        //Picasso.with(this).load(imageUri.toString()).into(imageView);
        Picasso.with(this)
                .load(imageUri.toString())
                .into(imageView , new Callback() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        mProgressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                    }
                });

        //Se agrega un timer para la actividad se cierre despues de 10 segundos

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 20000);

    }


}
