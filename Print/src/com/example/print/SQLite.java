package com.example.print;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLite extends SQLiteOpenHelper{

	private static final int VERSION_BASEDATOS = 1;

    // Nombre de nuestro archivo de base de datos
    private static final String NOMBRE_BASEDATOS = "facturas.db";
    
    // Sentencia SQL para la creación de una tabla
    private static final String TABLA_CONTACTOS = "CREATE TABLE contactos" +  
            "(_id INT PRIMARY KEY, nombre TEXT, telefono INT, email TEXT)";

    private static final String Tabla_Tarifas="Create Table Tarifas"+
    		"(`PK_PrecioTarifasId` int(11) NOT NULL primary key,"+
    		" `FK_TipoTarifaId` int(11) DEFAULT NULL,"+
    	    "`Descripcion` varchar(100) DEFAULT NULL,"+
    		  "`ConsumoMinimo` int(11) DEFAULT NULL,"+
    		  "`ConsumoMaximo` int(11) DEFAULT NULL,"+
    		  "`Precio` double(7,3) DEFAULT NULL)";
    
	Context context;
    
	public SQLite(Context context) {
		super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Tabla_Tarifas);
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void insertarDatos(String value)
	{
		String lineas[]=value.split("\n");
		int leng=lineas.length;
		for( int i=1;i<leng;i++)
		{
			String head[]=lineas[0].split(";");
			if (lineas[i].length()>0)
			{
					
				
				String values[]=lineas[i].split(";");
				SQLiteDatabase db =getWritableDatabase();
				if(db !=null)
				{
					ContentValues val = new ContentValues();
					
					for(int j=0;j<head.length;j++)
					{
						val.put(head[j], values[j]);
					}
					db.insert("Tarifas", null, val);
				}
		    	db.close();	
			}
			
		}
		Builder dialog = new AlertDialog.Builder(context);
		dialog.setNegativeButton("Ok", null);
		AlertDialog alert = dialog.create();
		alert.setTitle("Base de Datos");
		
		alert.setMessage("Actualizacion completada con Exito");
		
		alert.show();

		
		
	}

}
