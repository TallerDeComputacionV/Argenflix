package com.tcv.peliculas.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcv.peliculas.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PerfilActivity extends AppCompatActivity {
    private static final int REQUEST_TOMAR_FOTO = 1;
    private static final int REQUEST_OBTENER_GALERIA = 2;
    private static final int PERMISOS_SACAR_FOTO = 2;
    private static final int PERMISOS_OBTENER_GALERIA = 3;

    private ImageView ivPerfil;
    private TextView tvNoTieneFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        inicializarToolbar();
        tvNoTieneFoto = findViewById(R.id.tvNoTieneFoto);
        ivPerfil = findViewById(R.id.ivPerfil);
        TextView tvNombreUsuario = findViewById(R.id.tvNombreUsuario);

        tvNombreUsuario.setText(getNombreUsuario(this));

        if (tieneFotoPerfil(this)) {
            ivPerfil.setImageBitmap(getFotoPerfil(this));
        } else
            tvNoTieneFoto.setVisibility(View.VISIBLE);

        ivPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{getString(R.string.sacarFoto), getString(R.string.obtenerGaleria)};

                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
                builder.setCancelable(false);


                builder.setTitle(R.string.seleccionarFotoPerfil);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public static final int SACAR_FOTO = 0;

                    @Override
                    public void onClick(DialogInterface dialog, int option) {
                        if (option == SACAR_FOTO) {
                            if (tengoPermisos(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                                sacarFoto();
                            else
                                solicitarPermisos(PERMISOS_SACAR_FOTO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        } else if (tengoPermisos(Manifest.permission.READ_EXTERNAL_STORAGE))
                            obtenerFotoGaleria();
                        else
                            solicitarPermisos(PERMISOS_OBTENER_GALERIA, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void obtenerFotoGaleria() {
        Intent obtenerFotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(obtenerFotoIntent, REQUEST_OBTENER_GALERIA);
    }

    private void sacarFoto() {
        Intent sacarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (sacarFotoIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(sacarFotoIntent, REQUEST_TOMAR_FOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISOS_SACAR_FOTO) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sacarFoto();
            }
        } else {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerFotoGaleria();
            }
        }
    }

    protected void solicitarPermisos(int permisoInfo, String... permisosASolicitar) {
        ActivityCompat.requestPermissions(this, permisosASolicitar, permisoInfo);
    }

    protected Boolean tengoPermisos(String... permisosNecesarios) {
        for (String permiso : permisosNecesarios) {
            if (ActivityCompat.checkSelfPermission(PerfilActivity.this, permiso) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TOMAR_FOTO) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivPerfil.setImageBitmap(imageBitmap);
                guardarEnSharedPreferences(imageBitmap);

                if (tvNoTieneFoto.getVisibility() == View.VISIBLE)
                    tvNoTieneFoto.setVisibility(View.GONE);
            } else {
                Uri imagenSeleccionada = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imagenSeleccionada));
                    ivPerfil.setImageBitmap(bitmap);
                    guardarEnSharedPreferences(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String guardarImagen(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(getString(R.string.app_name), Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return directory.getAbsolutePath() + "/profile.jpg";
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

    public static boolean tieneFotoPerfil(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sp.contains("profile_picture");
    }

    public static Bitmap getFotoPerfil(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String rutaArchivo = sp.getString("profile_picture", "");

        Bitmap myBitmap = BitmapFactory.decodeFile(rutaArchivo);
        return myBitmap;
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

    public static String getNombreUsuario(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sp.getString("usuario", "");
    }
}



