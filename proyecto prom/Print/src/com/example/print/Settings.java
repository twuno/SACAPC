package com.example.print;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Settings extends Activity {
	private ArrayList itemlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		itemlist=new ArrayList<item>();
		ListView lst = (ListView)findViewById(R.id.lstSettings);
		itemlist.add(new item("Impresora",R.drawable.printer));
		itemlist.add(new item("Base de Datos",R.drawable.db));
		
		itemArrayAdapter iaa = new itemArrayAdapter(getApplicationContext(), itemlist);
		
		
		lst.setAdapter(iaa);
	
		lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				final item itemc = (item) arg0.getItemAtPosition(arg2);
				if(itemc.getNombre().compareTo("Impresora")==0)
				{
					Intent i = new Intent(Settings.this,Impresora.class);
					startActivity(i);
					
				}else
				{
					//Llamar la de Base de datos;
					Intent i = new Intent(Settings.this,BaseDatos.class);
					startActivity(i);
					
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}



}

