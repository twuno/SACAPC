package com.example.print;

import java.util.ArrayList;

import com.example.print.R.drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FacturasMedidor extends Activity {
	ProgressBar barra;
	
	Context c = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facturas_medidor);
		barra =(ProgressBar)findViewById(R.id.ProgressBar);
		
		final TextView txtMedidor = (TextView)findViewById(R.id.txtnumeroMedidor);
		final TextView txtlectura = (TextView)findViewById(R.id.TxtLectura);
		
		
		Button btnFacturas = (Button)findViewById(R.id.brnImpFact);
		btnFacturas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			String med = txtMedidor.getText().toString();
			
				if(txtMedidor.getText().length()==0 || txtlectura.getText().length()==0)
			{
				Toast.makeText(c, "No se recibio la informacion Necesaria", 1000);
				
			}else{
				getResources();
				String PortN=singleton.getInstance().getString();
				String Portsett="mini";
				String area="3inch (78mm)";
				ArrayList<Object> params = new ArrayList<Object>();
				params.add(c);
				params.add(PortN);
				params.add(Portsett);
				params.add(area);
				params.add(getResources());
				params.add(med);
				params.add(Double.valueOf(txtlectura.getText().toString()));
				
				FacturasHilo f= new FacturasHilo();
				
				f.execute(params);
			}
		
			}
	
		});
		
	}


	private class FacturasHilo extends AsyncTask< ArrayList<Object>, Integer, Integer> {
		
	
	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			barra.setVisibility(View.VISIBLE);
		
	}	
	
	@Override
	protected Integer doInBackground(ArrayList<Object>... params) {
		ArrayList<Object> param = params[0];
		Context c = (Context)param.get(0);
		String PortN=(String)param.get(1);
		String Portsett=(String)param.get(2);
		String area= (String)param.get(3);
		Resources res =(Resources)param.get(4);
		String medidor=(String) param.get(5);
		Double lectura=(Double) param.get(6);
		int resu =Factura.PrintSampleReceipt(c, PortN, Portsett, area, res,medidor,lectura);
		return resu;
	
		}
	
	@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			barra.setVisibility(View.INVISIBLE);
			
		}
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facturas_medidor, menu);
		return true;
	}

}





