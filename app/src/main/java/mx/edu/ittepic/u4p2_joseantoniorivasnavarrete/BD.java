package mx.edu.ittepic.u4p2_joseantoniorivasnavarrete;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BD extends SQLiteOpenHelper {
    public BD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Propietario(Id INTEGER NOT NULL PRIMARY KEY, Nombre VARCHAR(200), " +
                "Domicilio VARCHAR(500),Telefono VARCHAR(50))");
        /*db.execSQL("CREATE TABLE Inmueble(Id INTEGER NOT NULL PRIMARY KEY, Domicilio VARCHAR(200), " +
                "PrecioV FLOAT,PrecioR FLOAT, FechaT DATE, FOREIGN KEY (IdP) REFERENCES Propietario(Id) )");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
