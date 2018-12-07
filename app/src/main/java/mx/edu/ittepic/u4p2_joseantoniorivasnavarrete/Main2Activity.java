package mx.edu.ittepic.u4p2_joseantoniorivasnavarrete;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {
    EditText id,domicilio,precioV,precioR,fechaT;
    Spinner idP;
    Calendar fecha = Calendar.getInstance();
    Button agregar,consultar,actualizar,eliminar;
    BD base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        base = new BD( this,"inmueble",null,1 );
        id=findViewById(R.id.id);
        domicilio=findViewById(R.id.domicilio);
        precioV=findViewById(R.id.precioV);
        precioR=findViewById(R.id.precioR);
        fechaT=findViewById(R.id.fechaT);
        fechaT.setEnabled( false );
        fechaT.setText(fecha.get( Calendar.DAY_OF_MONTH )+"-"+fecha.get( Calendar.MONTH )+"-"+fecha.get( Calendar.YEAR ));
        agregar=findViewById(R.id.agregar);
        consultar=findViewById(R.id.consultar);
        actualizar=findViewById(R.id.actualizar);
        eliminar=findViewById(R.id.eliminar);
        try{
            SQLiteDatabase bd = base.getWritableDatabase();
            String SQL = "SELECT Id, Nombre FROM Propietario ORDER BY Nombre";
            Cursor resultado = bd.rawQuery( SQL,null );
            String prop[]=new String[resultado.getCount()];
            if (resultado.moveToFirst())
            {
                for(int i=0; i<resultado.getCount();i++){
                    prop[i]=resultado.getString( 0 )+"   "+ resultado.getString( 1 );
                    resultado.moveToNext();
                }
                msj( prop[1] );
            }
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,prop);
            idP.setAdapter( adaptador );
        }catch (SQLiteException e){
            Toast.makeText( this,"No existen Propietarios",Toast.LENGTH_LONG).show();
        }
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
    }
    private void insertar(){
        try{
            SQLiteDatabase bd = base.getWritableDatabase();
            String SQL = "INSERT INTO Inmueble VALUES("+id.getText().toString()+ ",'"+domicilio.getText().toString()
                    +"',"+precioV.getText().toString()+","+precioR.getText().toString()+ ",'"+fechaT.getText().toString()
                    +"',"+idP.getSelectedItem().toString()+")";
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
                            String SQL = "SELECT *FROM Inmueble WHERE Id="+no.getText().toString();
                            String SQL1 = "SELECT ";
                            Cursor resultado = bd.rawQuery(SQL,null);
                            if (resultado.moveToFirst())
                            {
                                id.setText(resultado.getString(0));
                                domicilio.setText(resultado.getString(1));
                                precioV.setText(resultado.getString(2));
                                precioR.setText(resultado.getString(3));
                                fechaT.setText(resultado.getString(4));
                                idP.setSelection( Integer.parseInt( resultado.getString( 5 ) )-1 );
                            }
                            else {
                                msj( "No existe un inmueble con dicho ID" );
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
            String SQL = "UPDATE Propietario SET Domicilio="+domicilio.getText().toString()
                    +"',PrecioV="+precioV.getText().toString()+",PrecioR="+precioR.getText().toString()
                    + ",FechaT='"+fechaT.getText().toString() +"',idP="+idP.getSelectedItem().toString()
                    +" WHERE Id="+id.getText().toString();
            bd.execSQL(SQL);
            Toast.makeText(this, "Listo", Toast.LENGTH_LONG).show();
            bd.close();
        }catch (SQLiteException e){
            Toast.makeText(this,"No se encontro el registro",Toast.LENGTH_LONG).show();
        }
    }
    public void eliminar(){
        try{
            SQLiteDatabase bd = base.getWritableDatabase();
            String SQL = "DELETE FROM Inmueble WHERE Id="+id.getText().toString();
            bd.execSQL(SQL);
            Toast.makeText(this, "Listo", Toast.LENGTH_LONG).show();
            bd.close();
            id.setText("");
            domicilio.setText("");
            precioV.setText("");
            precioR.setText("");
            fechaT.setText("");
        }catch (SQLiteException e){
            Toast.makeText(this,"No se encontro el registro",Toast.LENGTH_LONG).show();
        }

    }
    public void msj(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }
}
