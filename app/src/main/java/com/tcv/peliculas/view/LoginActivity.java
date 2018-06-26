package com.tcv.peliculas.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tcv.peliculas.R;

public class LoginActivity extends AppCompatActivity {
    private static final int OLVIDO_USUARIO = 0;
    private static final int OLVIDO_CONTRASEÑA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        String usuario = getCredenciales();
        if (!usuario.isEmpty()) {
            Intent intent =
                    new Intent(LoginActivity.this,
                            CategoriasActivity.class);
            intent.putExtra("usuario",usuario);
            LoginActivity.this.startActivity(intent);
            LoginActivity.this.finish();
        } else {
            inicializarVista();
        }
    }

    private String getCredenciales() {
        SharedPreferences sp = LoginActivity.this.getSharedPreferences(
                getString(R.string.app_name), Context.MODE_PRIVATE);
        return sp.getString("usuario", "");
    }

    private void inicializarVista() {
        final EditText etUsuario = (EditText) findViewById(R.id.etUsuario);
        final EditText etContraseña = (EditText) findViewById(R.id.etContraseña);
        Button btnIngresar = (Button) findViewById(R.id.btnIngresar);

        inicializarBotonNoPuedoAcceder();

        btnIngresar.setOnClickListener(new View.OnClickListener() {

            private int intentos = 0;

            @Override
            public void onClick(View view) {
                String usuario = etUsuario.getText().toString();
                String contraseña = etContraseña.getText().toString();

                if (usuario.equals("alan") && contraseña.equals("1234")) {
                    persistirCredenciales("alan", "1234");
                    Intent intent = new Intent(LoginActivity.this,
                            CategoriasActivity.class);
                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    intentos++;
                    if (intentos == 3) {
                        Toast.makeText(LoginActivity.this,
                                R.string.masDe3Intentos,
                                Toast.LENGTH_LONG).show();
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                R.string.userOPassIncorrecto,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void inicializarBotonNoPuedoAcceder() {
        final TextView btnNoPuedoAcceder = findViewById(R.id.btnNoPuedoAcceder);
        btnNoPuedoAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{getString(R.string.olvideUsuario), getString(R.string.olvideContraseña)};

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setTitle(R.string.recuperarAcceso);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int option) {
                        if (option == OLVIDO_USUARIO)
                            enviarMail(OLVIDO_USUARIO);
                        else
                            enviarMail(OLVIDO_CONTRASEÑA);
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

            void enviarMail(int option) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "contacto@argenflix.com.ar", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.problemaIngresar));
                emailIntent.putExtra(Intent.EXTRA_TEXT, option == OLVIDO_USUARIO ? getString(R.string.olvideUsuario) : getString(R.string.olvideContraseña));
                startActivity(Intent.createChooser(emailIntent, "Enviar email"));
            }
        });
    }

    private void persistirCredenciales(String usuario, String contraseña) {
        //SharedPreferences
        SharedPreferences sharedPreferences =
                LoginActivity.this.getSharedPreferences(getString(R.string.app_name),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario);
        editor.commit();
    }
}
