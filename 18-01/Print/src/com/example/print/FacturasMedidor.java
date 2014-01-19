package com.example.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.example.print.R.drawable;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FacturasMedidor extends Activity {
	ProgressBar barra;
	String Incidencia;
	//para la foto
	 byte ByteArr[]=null;
	Context c = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facturas_medidor);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
		barra =(ProgressBar)findViewById(R.id.ProgressBar);
		
		final TextView txtMedidor = (TextView)findViewById(R.id.txtnumeroMedidor);
		final TextView txtlectura = (TextView)findViewById(R.id.TxtLectura);
	
		
		final Button pic = (Button)findViewById(R.id.pic);
			pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	   	            startActivityForResult(i,1 );
				}
			});
		
		
		

		String in[];
		in = getIncidencias();
		ArrayAdapter<String> adapt =new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, in);

		final Spinner spn = (Spinner)findViewById(R.id.spin);
		
		spn.setAdapter(adapt);
		
		Button btnFacturas = (Button)findViewById(R.id.brnImpFact);
		
		btnFacturas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			String med = txtMedidor.getText().toString();
			
				if(txtMedidor.getText().length()==0 || txtlectura.getText().length()==0)
			{
				Toast.makeText(c, "No se recibio la informacion Necesaria", 1000).show();
				
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
				params.add((String)spn.getSelectedItem());
				FacturasHilo f= new FacturasHilo();
				
				f.execute(params);
				
			}
		
			}
	
		});
		
	}

	private String[] getIncidencias()
	{
		SQLite sl = new SQLite(c);
		ArrayList<String> rawlist=sl.getIncidencias();
		String result[]= new String[rawlist.size()];
		for (String string : rawlist) {
			result[rawlist.indexOf(string)]=string;
		} 
		

		
		return result;
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		if (data !=null){
			if (requestCode==1)
			{
			
				
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				photo =Bitmap.createScaledBitmap(photo, 800,600, true);
				ImageView iv = (ImageView)findViewById(R.id.fotoMD);
				iv.setImageBitmap(photo);
				ByteArrayOutputStream out=new ByteArrayOutputStream();
	            photo.compress(Bitmap.CompressFormat.JPEG,30,out);
	            ByteArr=out.toByteArray();
	            
	            File f = new File(Environment.getExternalStorageDirectory()+ File.separator +"pic.jpg" );//new Date().toString()+"pic.jpg");
	            try {
					f.createNewFile();
					 FileOutputStream fo = new FileOutputStream(f);
			            fo.write(ByteArr);
			                   fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           
	            
			}
		}
	}
	
	private class FacturasHilo extends AsyncTask< ArrayList<Object>, Integer, Integer> {
		double latitud,longitud;
		String imei;
	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(c.TELEPHONY_SERVICE);
			imei=telephonyManager.getDeviceId();
			barra.setVisibility(View.VISIBLE);
			
			GPSTracker gps = new GPSTracker(c);
			if (gps.canGetLocation())
			{
				 latitud=gps.getLatitude();
				 longitud=gps.getLongitude();
				//Toast.makeText(c, latitud +""+longitud, Toast.LENGTH_LONG).show();
			}else
			{
				gps.showSettingsAlert();
			}
			
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
		String Observacion =(String) param.get(7);
		ArrayList toFact = new ArrayList<Object>();
		toFact.add(medidor);
		toFact.add(lectura);
		toFact.add(imei);
		toFact.add(latitud);
		toFact.add(longitud);
		toFact.add(ByteArr);
		toFact.add(Observacion);
		
		int resu =Factura.PrintReceipt(c, PortN, Portsett, area, res,toFact);
		return resu;
	
		}
	
	@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result==-1)
			{
				Toast.makeText(c, "Hubo un problema para imprimir la factura", 1000).show();
			}else if(result==-2)
			{
				Toast.makeText(c, "No puede imprimir a esa distancia del medidor", 1000).show();
			}
			
			barra.setVisibility(View.INVISIBLE);
			
		}
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facturas_medidor, menu);
		return true;
	}


	class incidencia
	{
		int id;
		String detalle;
		public incidencia(int a,String b)
		{
			a=id;
			b=detalle;
		}
		
		public int getid()
		{
			return id;
		}
		
		public String getdetalle()
		{
			return detalle;
		}
	}
	



}






