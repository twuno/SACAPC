package com.example.print;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Printer;
import android.widget.Toast;

public class SQLite extends SQLiteOpenHelper{

	private static final int VERSION_BASEDATOS = 1;

    // Nombre de nuestro archivo de base de datos
    private static final String NOMBRE_BASEDATOS = "SAPC";
    
    // Sentencia SQL para la creación de una tabla
    
    private static final String Tabla_Facturas="CREATE TABLE `Facturas` ( " +
    											"`PK_FacturaId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
    											"`FK_CasaId` int(11) DEFAULT NULL, " +
    											"`MedicionActual` double(10,2) DEFAULT NULL, " +
    											"`Fecha` datetime DEFAULT NULL, "+
    											"`Retraso` double(8,2) DEFAULT NULL, "+
    											" `Alcantarillado` double(8,2) DEFAULT NULL, "+
    											"`ConsumoActual` double(8,2) DEFAULT NULL, "+
    											"`Total` double(8,2) DEFAULT NULL, "+
    											"`Latitud` double(8,2) DEFAULT NULL,"+
    											"`Longitud` double(8,2) DEFAULT NULL)";
    		
    		
    
    private static final String TABLA_INFO = "CREATE TABLE info " +  
            								 "(`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  ,"+
            								 " `cuenta` varchar(20) NOT NULL,"+
            								 " `FK_TipoTarifaId` int(3) NOT NULL,"+ //tipo de tarifa
            								 " `Descripcion` varchar(150) DEFAULT NULL,"+ //direccion
            								 " `Alcantarillado` int(1) NOT NULL,"+
            								 " `NombreCliente` varchar(70) DEFAULT NULL," +
            								 " `Serie` varchar(10) NOT NULL,"+ //mumero de medidor
            								 " `Modelo` varchar(5) DEFAULT NULL," + //tipo de medidor
            								 " `Fecha` DATETIME NOT NULL,"+ //fecha de ultima medicion
            								 " `MedicionActual` Double(5,4) NOT NULL,"+ //ultima medicion
            								 " `SaldoPendiente` Double(7,2) NOT NULL" //deuda
            								 + ")";

    
    private static final String Tabla_Tarifas="Create Table Tarifas"+
    		"(`PK_PrecioTarifasId` int(11) NOT NULL primary key,"+
    		" `FK_TipoTarifaId` int(11) DEFAULT NULL,"+
    	    "`Descripcion` varchar(100) DEFAULT NULL,"+
    		  "`ConsumoMinimo` int(11) DEFAULT NULL,"+
    		  "`ConsumoMaximo` int(11) DEFAULT NULL,"+
    		  "`Precio` double(7,3) DEFAULT NULL)";
    
	Context context;
	SQLiteDatabase db=null;
    
	private void drop()
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("drop table if exists info");
		db.execSQL("drop table if exists Tarifas");
		db.execSQL("drop table if exists Facturas");

		onCreate(db);
	}
	
	private MySql instancia =null;
	
	public SQLite(Context context) {
		super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
		this.context=context;
		
		
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
			
			
			int a =0;
					a=a+8;
			
					db.execSQL(TABLA_INFO);
					db.execSQL(Tabla_Tarifas);
					db.execSQL(Tabla_Facturas);
		}catch(Exception e)
		{
			System.out.print(e);
		}		
		int a=8;

	
		
	}
	
	private void crearInstanciaMysql()
	{
		MySql m = new MySql();
		m.conectarBDMySQL("root", "Pr0b1t.2012!", "190.185.116.14", "3306","Walter",this.context);
		this.instancia=m; 
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists info");
		db.execSQL("drop table if exists Tarifas");
		onCreate(db);
		
	}
	
	public int actualizarinfo()
	{
		crearInstanciaMysql();
		drop();
		try{
		String query="Select * from PreciosTarifas ;";
		String value1=instancia.ejecutarConsultaSQL(false, query, context);
		
		
		
		String Query="select "+
							    " ca.Cuenta,ca.FK_TipoTarifaId ,ca.Descripcion as Direccion,"+
								" ca.Alcantarillado,CONCAT(cl.Apellidos,' ',cl.Nombres)NombreCliente,"+
								" md.Serie Medidor,md.Modelo TipoMedidor,F.Fecha FechaAnterior,F.MedicionActual ,"+
								" D.SaldoPendiente Deuda "+" from Casas ca "+
					" INNER JOIN Clientes cl on ca.FK_ClienteId = cl.PK_ClienteId"+
					" INNER JOIN Medidores md on ca.FK_MedidorId = md.PK_MedidorId"+
					" INNER JOIN Deuda D on D.CasaId=ca.PK_CasaId"+
					" INNER JOIN "+
					" (SELECT f1.* from Facturas f1 LEFT JOIN Facturas f2  on "+ 
					" (f1.FK_CasaId=f2.FK_CasaId and f1.Fecha< f2.Fecha) where f2.Fecha is NULL) F "+ 
					" on F.FK_CasaId=ca.Pk_CasaID ";
	 String value=instancia.ejecutarConsultaSQL(false, Query, context);
	  int a;
	a = this.droptabla("info");
	  int m;
	m = this.insertarDatos(value,"info");
	   a = this.droptabla("Tarifas");
	  int e;
	e = this.insertarDatos(value1,"Tarifas");
	}catch(Exception e){return -1;}
	return 0;
	}

	
	public int insertDatos(String value)
	{
		return 0;
	}
	
	public int droptabla(String Nombretabla)
	{
		SQLiteDatabase db = getWritableDatabase();
	
		try
		{
			
			db.delete(Nombretabla, null, null);
			
		}catch(Exception e ){
			return -1;
		}
		return 0;
	}
	
	
	public int insertarDatos(String value,String Tabla)
	{
		
				
		String lineas[]=value.split("\n");
		int leng=lineas.length;
		try{
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
						db.insert(Tabla, null, val);
					}
			    	db.close();	
				}
				
			}
		}catch (Exception e) {
			Log.e("ERROR","Error encontrado "+e.getMessage());
			return -1;
		}
		return 0;
		}

	
	public ArrayList<Object> LastFactura(String Medidor)
	{
		ArrayList<Object> data = new ArrayList<Object>();
		SQLiteDatabase db = getReadableDatabase();
		String[] valores_recuperar={"_id","cuenta","FK_TipoTarifaId","Descripcion","Alcantarillado","NombreCliente","Serie","Modelo","Fecha","MedicionActual","SaldoPendiente"};
		Cursor c =db.query("info", valores_recuperar, "Serie="+Medidor, null, null, null, null);
		if (c!=null)
		{
			c.moveToFirst();
			try{
				data.add(c.getInt(0));
				data.add(c.getString(1));
				data.add(c.getInt(2));
				data.add(c.getString(3));
				data.add(c.getInt(4));
				data.add(c.getString(5));
				data.add(c.getString(6));
				data.add(c.getString(7));
				data.add(c.getString(8));
				data.add(c.getDouble(9));
				data.add(c.getDouble(10));
				return data;
			}catch(Exception e)
			{
		//		Toast.makeText(context, "El numero de medidor no existe", 10000).show();
				return null;
			}
			}else
		{
		//	Toast.makeText(context, "El numero de medidor no existe", 10000).show();
			return null;
		}
				

	}
	
	public ArrayList<Object> Tarifa(Double consumo, int TipoTarifa)
	{
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Object> data = new ArrayList<Object>();
		Cursor c = db.query("Tarifas",null,"FK_TipoTarifaId="+TipoTarifa+" and ConsumoMinimo<="+consumo+" and ConsumoMaximo>"+consumo, null, null, null, null);
		if(c !=null)
		{
			c.moveToFirst();
		}
		
		data.add(c.getInt(0));
		data.add(c.getInt(1));
		data.add(c.getString(2));
		data.add(c.getInt(3));
		data.add(c.getInt(4));
		data.add(c.getDouble(5));
		return data;
	}
}
