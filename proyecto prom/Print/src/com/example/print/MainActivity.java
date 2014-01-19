package com.example.print;




import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



@SuppressLint("NewApi")
public class MainActivity extends Activity {
	Context pc = this;
	
	CardUI mCardView;
	
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		 mCardView = (CardUI) findViewById(R.id.cardsview);
			
			mCardView.setSwipeable(true);

			// add AndroidViews Cards
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			String imei=telephonyManager.getDeviceId();
			
			MyPlayCard c = new MyPlayCard("123","Factura de Urbina Rosa Miriam","#ff33b6ea","#ff33b6ea",true,true);
			
			mCardView.addCard(new MyCard("Sistema de Aguas de Puerto Cortes"));
			mCardView.addCard(c);

		
			// create a stack
			CardStack stack = new CardStack();
			stack.setTitle("Facturas");
			stack.setPosition(3);
		
			mCardView.addStack(stack);

			// draw cards
			mCardView.refresh();


		 
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		CreaMenu(menu);
		
		return true;
	}
	
	private void CreaMenu (Menu menu){ 
	
		MenuItem item1 = menu.add(0,0,0,"Item1");
		item1.setIcon(R.drawable.settings);
		item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		MenuItem item2 = menu.add(0,1,1,"Item2");
		item2.setIcon(R.drawable.factura);
		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch	(item.getItemId())
		{
		
		case 0:
			 Intent i = new Intent(pc,Settings.class);
			 startActivity(i);
			 
			return true;
			
		case 1:
			Intent i2 = new Intent(pc,FacturasMedidor.class);
			startActivity(i2);
			return true;
		case android.R.id.home:
			Toast.makeText(pc, "Boton principal", 1000).show();
			
		}
		
		
		return false;
	}


}




