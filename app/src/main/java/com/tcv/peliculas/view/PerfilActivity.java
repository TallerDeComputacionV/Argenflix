package com.tcv.peliculas.view;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tcv.peliculas.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PerfilActivity extends AppCompatActivity {
    private static final int REQUEST_TOMAR_FOTO = 1;
    private static final int PERMISOS = 2;
    String[] permisosNecesarios = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private ImageView ivPerfil;
    private TextView tvNoTieneFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        inicializarToolbar();
        tvNoTieneFoto = findViewById(R.id.tvNoTieneFoto);
        ivPerfil = findViewById(R.id.ivPerfil);

        if (tieneFotoPerfil()) {
            getFotoPerfil();
        } else
            tvNoTieneFoto.setVisibility(View.VISIBLE);

        ivPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (tengoPermisos())
                    sacarFoto();
                else
                    solicitarPermisos();
            }
        });
    }

    protected void solicitarPermisos() {
        ActivityCompat.requestPermissions(this, permisosNecesarios, PERMISOS);
    }

    private void sacarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_TOMAR_FOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISOS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sacarFoto();
            }
        }
    }

    protected Boolean tengoPermisos() {
        for (String permiso : permisosNecesarios) {
            if (ActivityCompat.checkSelfPermission(PerfilActivity.this, permiso) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TOMAR_FOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPerfil.setImageBitmap(imageBitmap);
            guardarEnSharedPreferences(imageBitmap);

            if (tvNoTieneFoto.getVisibility() == View.VISIBLE)
                tvNoTieneFoto.setVisibility(View.GONE);
        }
    }

    private String guardarImagen(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(getString(R.string.app_name), Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void guardarEnSharedPreferences(Bitmap imageBitmap) {
        String rutaFoto = guardarImagen(imageBitmap);
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(getString(R.string.app_name),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_picture", rutaFoto);
        editor.commit();
    }

    private boolean tieneFotoPerfil() {
        SharedPreferences sp = PerfilActivity.this.getSharedPreferences(
                getString(R.string.app_name), Context.MODE_PRIVATE);
        return sp.contains("profile_picture");
    }

    public void getFotoPerfil() {
        SharedPreferences sp = PerfilActivity.this.getSharedPreferences(
                getString(R.string.app_name), Context.MODE_PRIVATE);
        String rutaArchivo = sp.getString("profile_picture", "");

        File f = new File(rutaArchivo);
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ivPerfil.setImageBitmap(b);

    }

    private void inicializarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}



