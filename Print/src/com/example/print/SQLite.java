package com.example.print;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
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
	
	public SQLite(Context context,MySql myinstancia) {
		super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
		this.context=context;
		// TODO Auto-generated constructor stub
		this.instancia=myinstancia;
		
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

		// TODO Auto-generated method stub
		
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

}
