package com.example.print;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class execdb extends AsyncTask<Context, Void, Integer> {
	Context c =null;
	@Override
	protected Integer doInBackground(Context... params) {
	//	MySql m = new MySql();
//		m.conectarBDMySQL("root", "Pr0b1t.2012!", "190.185.116.14", "3306","Walter", params[0]);
		SQLite sl = new SQLite(params[0]);
		int res=sl.actualizarinfo();
		this.c=params[0];
		return res;
		// TODO Auto-generated method stub
//		return null;
	
	}
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Toast.makeText(this.c,"Hola, soy un hilo", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onPreExecute() {
		
		// TODO Auto-generated method stub
		super.onPreExecute();
	
	}

	
	

}
