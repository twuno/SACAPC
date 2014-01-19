package com.example.print;

import java.util.ArrayList;

import com.example.print.R.drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BaseDatos extends Activity {
	Context con = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_datos);
		
		ListView lst = (ListView)findViewById(R.id.lstDB);
		
		ArrayList itms = new ArrayList<item>();
		
		itms.add(new item("Bajar Informacion a Dispositivo",drawable.ic_av_download));
		itms.add(new item("Subir Informacion al Servidor",drawable.ic_av_upload));
		

		itemArrayAdapter iaa = new itemArrayAdapter(getApplicationContext(), itms);
		
		
		lst.setAdapter(iaa);
		lst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final item itemc = (item) arg0.getItemAtPosition(arg2);
				if(itemc.getNombre().compareTo("Bajar Informacion a Dispositivo")==0)
				{
						execdb t= new execdb();
						Toast.makeText(con,"Comenzando Descarga", Toast.LENGTH_SHORT).show();
						t.execute(con);
			
						
				}else
				{
					// TODO guardar las facturas
					
				}
				
				
				
			}
		});
		
	}

	private class subirdatos extends AsyncTask<Void, Void, Integer>
	{
		
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base_datos, menu);
		return true;
	}

}
