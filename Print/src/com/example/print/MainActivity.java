package com.example.print;

import java.util.ArrayList;
import java.util.List;


import com.example.print.MyLocation;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends Activity {
	Context pc = this;

	@SuppressLint("NewApi")
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		final ImageButton btnsettings = (ImageButton)findViewById(R.id.btnsettings);
		btnsettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			 Intent i = new Intent(pc,Settings.class);
			 startActivity(i);
				
			}
		});		
		
		
		final Button btnPrinter = (Button) findViewById(R.id.brnImpFact);
		btnPrinter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,Impresora.class);
				startActivity(i);																																																																																																																																																																																																																								
				
			}
		});
		
		final ImageButton btnFactura = (ImageButton) findViewById(R.id.btnGenFactura);
		btnFactura.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(pc,FacturasMedidor.class);
				startActivity(i);
				
			}
		});
		
		
		/*		
		btngetPort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				config c = new config(pc);
				c.setPortname("BT:");
				c.setPortSettings("mini");
				c.setMillisecs(10000);
				//c.getPort(pc); 
			}
		});
		
		final Button btnMysql = (Button) findViewById(R.id.button2);
		btnMysql.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MySql m = new MySql();
				//m.Conectar(pc);
				m.conectarBDMySQL("root", "Pr0b1t.2012!", "190.185.116.14", "3306","Walter", pc);
				String consulta= "Select * from PreciosTarifas ;";
				String res = "";
				res=m.ejecutarConsultaSQL(false, consulta, pc);
				SQLite lite = new SQLite(pc);
				lite.insertarDatosTarifas(res);	
				Double p1=0.0 ,p2=0.0;	
				MyLocation lo = new MyLocation(pc);
				lo.Posicion(pc);
				Location pos =lo.loca;
				p1=pos.getLatitude();
				p2=pos.getLongitude();
				Builder dialog = new AlertDialog.Builder(pc);
				dialog.setNegativeButton("Ok", null);
	    		AlertDialog alert = dialog.create();
	    		alert.setTitle("Mysql");
	    		res="position "+" "+p1+" "+p2+" " + res;
	    		alert.setMessage(res);
	    		//alert.show();
			}
		});
		
		final Button gpstrack = (Button) findViewById(R.id.btngps);
		gpstrack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,AndroidGPSTrackingActivity.class);
				startActivity(intent);
			
			}
		});
*/
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		
		return true;
	}
	
   	

}
