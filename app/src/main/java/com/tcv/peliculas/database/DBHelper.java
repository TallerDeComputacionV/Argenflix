package com.tcv.peliculas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tcv.peliculas.model.Favorito;
import com.tcv.peliculas.model.Pelicula;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "argenflix";
    private final static int DB_VERSION = 5;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Favorito.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionActual) {
        db.execSQL("DROP TABLE IF EXISTS " + Favorito.TABLE_NAME);
        onCreate(db);
    }

    private ContentValues mapperContentValues(Pelicula pelicula) {
        ContentValues registro = new ContentValues();
        registro.put(Favorito.COLUMN_PELICULA_ID, pelicula.getId());
        registro.put(Favorito.COLUMN_PELICULA_TITULO, pelicula.getTitulo());
        registro.put(Favorito.COLUMN_PELICULA_IMAGEN, pelicula.getImagen());
        registro.put(Favorito.COLUMN_PELICULA_GENERO, pelicula.getGenero());
        return registro;
    }

    public void quitarFavorito(Pelicula pelicula) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = Favorito.COLUMN_PELICULA_ID + "= ?";
        db.delete(Favorito.TABLE_NAME, where, new String[]{String.valueOf(pelicula.getId())});
        db.close();
    }

    public long agregarFavorito(Pelicula pelicula) {
        SQLiteDatabase db = this.getWritableDatabase();
        long row = db.insert(Favorito.TABLE_NAME, null, mapperContentValues(pelicula));
        db.close();
        return row;
    }

    public Boolean esFavorito(Pelicula pelicula) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        String sql = "SELECT " + Favorito.COLUMN_ID + " FROM " + Favorito.TABLE_NAME +
                " WHERE " + Favorito.COLUMN_PELICULA_ID + "=" + pelicula.getId();
        cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ArrayList<Favorito> getFavoritos() {
        ArrayList list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] campos = new String[]
                {Favorito.COLUMN_ID, Favorito.COLUMN_PELICULA_TITULO, Favorito.COLUMN_PELICULA_IMAGEN,
                        Favorito.COLUMN_PELICULA_GENERO, Favorito.COLUMN_PELICULA_ID};
        Cursor c = db.query(Favorito.TABLE_NAME, campos, null, null, null, null, null);

        try {
            while (c.moveToNext()) {
                Favorito favorito = new Favorito();
                favorito.setId(c.getInt(0));
                favorito.setTitulo(c.getString(1));
                favorito.setImagen(c.getString(2));
                favorito.setGenero(c.getString(3));
                favorito.setPeliculaId(c.getInt(4));

                list.add(favorito);
            }
        } finally {
            db.close();
        }

        return list;
    }
}
