package com.example.print;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper{

	private static final String NOMBRE_BASEDATOS = "mibasedatos.db";
	 private static final String TABLA = "CREATE TABLE Factura" +  
	            "(_id INT PRIMARY KEY, nombre TEXT, telefono INT, email TEXT)";
	 private static final int VERSION_BASEDATOS = 1;
	 
	 public BaseDatos(Context context) {
		super(context,NOMBRE_BASEDATOS,null,VERSION_BASEDATOS);
		// TODO Auto-generated constructor stub
	}

@Override
public void onCreate(SQLiteDatabase db) {
	db.execSQL(TABLA);

}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 db.execSQL("DROP TABLE IF EXISTS " + TABLA);
     onCreate(db);
	
}


public void insertarRegistros()
{
	
}

}
