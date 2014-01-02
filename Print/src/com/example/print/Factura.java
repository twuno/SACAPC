package com.example.print;

import java.util.ArrayList;
import java.util.Arrays;

import android.R.integer;
import android.database.sqlite.SQLiteDatabase;

public class Factura {

	SQLiteDatabase bd;
	Boolean Alcantarillado = false;
	int consumoAct;
	int consumoAnt;
	int adeuda;
	String medidor;
    String cuenta;
	String Nombre;
	int tasa;
	
	
	public ArrayList<Byte> Factura()
	{
		ArrayList<Byte> Factura = new ArrayList<Byte>();
		byte[] buffer=null;
		Byte[] templist;
		
		
		Factura.addAll(Arrays.asList(new Byte[]{0x1d, 0x57, 0x40, 0x32}));           //Page Area Setting     <GS> <W> nL nH  (nL = 64, nH = 2)
		
		Factura.addAll(Arrays.asList(new Byte[]{0x1b, 0x61, 0x01}));                 //Center Justification  <ESC> a n       (0 Left, 1 Center, 2 Right)
		

		
		buffer= ("\n Aguas De Puerto Cortes \n" + " ").getBytes();
		templist = new Byte[buffer.length];
		CopyArray(buffer,templist);
		Factura.addAll(Arrays.asList(templist));
		
		
		
		return Factura;
	}
	
	
	private static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
    	for (int index = 0; index < cpyArray.length; index++) {
    		cpyArray[index] = srcArray[index];
    	}
    }
	
}
