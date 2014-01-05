	package com.example.print;

import java.util.ArrayList;

import com.example.print.R.drawable;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class Impresora extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impresora);
		
		ListView lst = (ListView)findViewById(R.id.lstDB);
		
		ArrayList itms = new ArrayList<item>();
		
		itms.add(new item("Seleccionar Impresora",drawable.bluetooth));

		itemArrayAdapter iaa = new itemArrayAdapter(getApplicationContext(), itms);
		
		
		lst.setAdapter(iaa);
		
		lst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				config c = new config(Impresora.this);
				
			}

		});
		
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.impresora, menu);
		return true;
	}

}

