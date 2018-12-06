package mx.edu.ittepic.u4p2_joseantoniorivasnavarrete;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText id,nombre,domicilio,telefono;
    Button agregar,consultar,actualizar,eliminar;
    TextView inmueble;
    BD base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        id=findViewById(R.id.id);
        nombre=findViewById(R.id.nombre);
        domicilio=findViewById(R.id.domicilio);
        telefono=findViewById(R.id.telefono);

        agregar=findViewById(R.id.agregar);
        consultar=findViewById(R.id.consultar);
        actualizar=findViewById(R.id.actualizar);
        eliminar=findViewById(R.id.eliminar);

        inmueble=findViewById( R.id.inmueble );

        base = new BD(this,"primera",null,1);
        agregar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertar();
            }
        } );
        consultar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultar();
            }
        } );
        actualizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
            }
        } );
        eliminar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        } );
        inmueble.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inmueble = new Intent( MainActivity.this, Main2Activity.class );
                startActivity( inmueble );
            }
        } );

    }
    private void insertar(){
        try{
            SQLiteDatabase bd = base.getWritableDatabase();
            String SQL = "INSERT INTO Propietario VALUES("+id.getText().toString()+ ",'"+nombre.getText().toString()
                    +"','"+domicilio.getText().toString()+"',"+telefono.getText().toString()+")";
            bd.execSQL(SQL);
            Toast.makeText(this, "Listo", Toast.LENGTH_LONG).show();
            bd.close();
        }catch (SQLiteException e){
            Toast.makeText(this,"No se pudo",Toast.LENGTH_LONG).show();
        }
    }
    public void consultar(){
        final String valor;
        final EditText no = new EditText( this );
        no.setInputType( InputType.TYPE_CLASS_NUMBER );
        no.setHint( "Mayor a cero" );

        AlertDialog.Builder alerta = new AlertDialog.Builder( this );
        alerta.setTitle( "Consultar" ).setMessage( "Ingrese el ID" )
                .setView( no )
                .setPositiveButton( "Buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            SQLiteDatabase bd = base.getReadableDatabase();
                            String SQL = "SELECT *FROM Propietario WHERE Id="+no.getText().toString();
                            Cursor resultado = bd.rawQuery(SQL,null);
                            if (resultado.moveToFirst())
                            {
                                id.setText(resultado.getString(0));
                                nombre.setText(resultado.getString(1));
                                domicilio.setText(resultado.getString(2));
                                telefono.setText(resultado.getString(3));
                            }
                            else {
                                msj( "No existe un propietario con dicho ID" );
                            }
                            bd.close();
                        }catch (SQLiteException e){
                            msj( "Ingrese un valor" );
                        }
                        return;
                    }
                } )
                .setNegativeButton( "Cancelar",null ).show();
    }
    public  void actualizar(){
        try{
            SQLiteDatabase bd = base.getWritableDatabase();
            String SQL = "UPDATE Propietario SET Nombre='"+nombre.getText().toString()
                    +"',Domicilio='"+domicilio.getText().toString()+"',Telefono="+telefono.getText().toString()
                    +" WHERE Id="+id.getText().toString();
            bd.execSQL(SQL);
            Toast.makeText(this, "Listo", Toast.LENGTH_LONG).show();
            bd.close();
        }catch (SQLiteException e){
            Toast.makeText(this,"No se el registro",Toast.LENGTH_LONG).show();
        }
    }
    public void eliminar(){
        try{
            SQLiteDatabase bd = base.getWritableDatabase();
            String SQL = "DELETE FROM Propietario WHERE Id="+id.getText().toString();
            bd.execSQL(SQL);
            Toast.makeText(this, "Listo", Toast.LENGTH_LONG).show();
            bd.close();
            id.setText("");
            nombre.setText("");
            domicilio.setText("");
            telefono.setText("");
        }catch (SQLiteException e){
            Toast.makeText(this,"No se encontro el registro",Toast.LENGTH_LONG).show();
        }

    }
    public void msj(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }
}
