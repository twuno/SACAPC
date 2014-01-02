package com.example.print;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class MySql {
	static Connection conexion;
	String ipdir = "190.185.116.14";
	String urlConexionMySQL = "jdbc:mysql://" + ipdir + ":" + 3306+ "/" + "dev";
	String Usuario = "root";
	String Contraseña = "Pr0b1t.2012!";
	Context c;
	public void Conectar(Context c)
	{
		
		try
		{
			Class.forName("com.mysl.jdbc.Driver");
			conexion = DriverManager.getConnection(urlConexionMySQL,Usuario,Contraseña);
			this.c=c;
			
		}
		catch( ClassNotFoundException e)
		{
			 Toast.makeText(c,
	                    "Error: " + e.getMessage(),
	                    Toast.LENGTH_SHORT).show();
			
		} catch (SQLException e) {
			Toast.makeText(c,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
				
		}
	
	
		}
		
	public static String ejecutarConsultaSQL(Boolean SQLModificacion,String SQLEjecutar, Context context)
    {
        try
		{
          String resultadoSQL = "";
          //ejecutamos consulta SQL de selección (devuelve datos)
      	  if (!SQLModificacion)
      	  {	            	    
      		  Statement st = conexion.createStatement();
      		  ResultSet rs = st.executeQuery(SQLEjecutar); 

			  Integer numColumnas = 0;
				  
          	  //número de columnas (campos) de la consula SQL            	  
          	  numColumnas = rs.getMetaData().getColumnCount();          	  

      		  //obtenemos el título de las columnas
      		  for (int i = 1; i <= numColumnas; i++)
      		  {
              	  if (resultadoSQL != "")
              		  if (i < numColumnas)
              			  resultadoSQL = resultadoSQL + 
              			  		rs.getMetaData().getColumnName(i).toString() + ";";
              		  else
              			  resultadoSQL = resultadoSQL + 
              			  		rs.getMetaData().getColumnName(i).toString();
              	  else
              		  if (i < numColumnas)
              			  resultadoSQL = 
              				  rs.getMetaData().getColumnName(i).toString() + ";";
              		  else
              			  resultadoSQL = 
              				  rs.getMetaData().getColumnName(i).toString();                  	  
      		  }

          	  
          	  //mostramos el resultado de la consulta SQL
          	  while (rs.next()) 
          	  {  
          		  resultadoSQL = resultadoSQL + "\n";
          		  
          		  //obtenemos los datos de cada columna
          		  for (int i = 1; i <= numColumnas; i++)
                  {                          
                        if (rs.getObject(i) != null)
                        {
                      	  if (resultadoSQL != "")
                      		  if (i < numColumnas)
                      			  resultadoSQL = resultadoSQL + 
                      			  		rs.getObject(i).toString() + ";";
                      		  else
                      			  resultadoSQL = resultadoSQL + 
                      			  		rs.getObject(i).toString();
                      	  else
                      		  if (i < numColumnas)
                      			  resultadoSQL = rs.getObject(i).toString() + ";";
                      		  else
                      			  resultadoSQL = rs.getObject(i).toString();
                        }
                        else
                        {
                      	  if (resultadoSQL != "")
                      		  resultadoSQL = resultadoSQL + "null;";
                      	  else
                      		  resultadoSQL = "null;";
                        }                           
                    }
                    resultadoSQL = resultadoSQL + "\n";
          	  }
      		  st.close();
      		  rs.close();        		  
		  }
      	  // consulta SQL de modificación de 
      	  // datos (CREATE, DROP, INSERT, UPDATE)
      	  else 
      	  {
      		  int numAfectados = 0; 
      		  Statement st = conexion.createStatement();
      		  numAfectados = st.executeUpdate(SQLEjecutar);
      		  resultadoSQL = "Registros afectados: " + String.valueOf(numAfectados);
      		  st.close();
      	  }
      	  return resultadoSQL;
		}
        
        catch (Exception e) 
        {  
      	  Toast.makeText(context,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
      	  return "";
        }
    }
	
	
	public void conectarBDMySQL (String usuario, String contrasena, 
    		String ip, String puerto, String catalogo,Context c)
    {
    	if (usuario == "" || puerto == "" || ip == "")
    	{
    		AlertDialog.Builder alertDialog = 
    			new AlertDialog.Builder(c);
    		alertDialog.setMessage("Antes de establecer la conexión " +
    				"con el servidor " +
    				"MySQL debe indicar los datos de conexión " +
    				"(IP, puerto, usuario y contraseña).");
    		alertDialog.setTitle("Datos conexión MySQL");
    		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
    		alertDialog.setCancelable(false);
    		alertDialog.setPositiveButton("Aceptar", null);
    		alertDialog.show();
    	}
    	else
    	{
    		String urlConexionMySQL = "";
    		if (catalogo != "")
    			urlConexionMySQL = "jdbc:mysql://" + ip + ":" +	
    			    puerto + "/" + catalogo;
    		else
    			urlConexionMySQL = "jdbc:mysql://" + ip + ":" + puerto;
    		if (usuario != "" & contrasena != "" & ip != "" & puerto != "")
    		{
    			try 
    			{
					Class.forName("com.mysql.jdbc.Driver");
	    			conexion =	DriverManager.getConnection(urlConexionMySQL, 
	    					usuario, contrasena);					
				} 
    			catch (ClassNotFoundException e) 
    			{
    		      	  Toast.makeText(c,
    		                    "Error: " + e.getMessage(),
    		                    Toast.LENGTH_SHORT).show();
    			} 
    			catch (SQLException e) 
    			{
			      	  Toast.makeText(c,
			                    "Error: " + e.getMessage(),
			                    Toast.LENGTH_SHORT).show();
				}
    		}
    	}
    }
	
}
