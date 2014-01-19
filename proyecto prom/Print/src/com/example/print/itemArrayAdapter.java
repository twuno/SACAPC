package com.example.print;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;




public class itemArrayAdapter extends ArrayAdapter<item> {

	private Context context;
	private ArrayList<item> datos;
	
	public itemArrayAdapter(Context context, ArrayList<item> datos) {
		super(context, R.layout.activity_settings,datos);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.datos=datos;
	}

	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
	    View item2 = inflater.inflate(R.layout.activity_settings, null);
	    
	    ImageView imagen = (ImageView) item2.findViewById(R.id.imgitem);
	    imagen.setImageResource(datos.get(position).getDrawableImageID());
	 
	 
	    TextView nombre = (TextView) item2.findViewById(R.id.txtField);
	    nombre.setText(datos.get(position).getNombre());
	 
	  

	    return item2;
	}
}
