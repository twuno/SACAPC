package com.example.print;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;



import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;

public class Factura {

	private static String dataFactura = "FK_CasaId;"+"MedicionActual;"+"Fecha;"+"Retraso;"+"Alcantarillado;"+"ConsumoActual;"+"Total;"+
	"Latitud;"+"Longitud;"+	"userId\n";	
	
	private   static String cuenta="";
	private   static	Double ValorporConsumo=0.0;
	private static Double ValorporAlcantarillado=0.0; 
	private   static	String Direccion ="" ,Nombre="";
	private static int Alcantarillado =0; 
	private static	String medidor="",tipomedidor="";
	private static Double lecturaAnt=0.0;	
	private static	Double adeuda=0.0;
	private static Double lecturaAct=0.0;
	private static	Double Consumo=0.0;
	private   static	String  tarifadesc="";
	private   static	Double tarifa=0.0;
	private static Double total=0.0;
	private   static	String Fecha_Lectura_Anterior ="",imei="";
	private   static	String Fecha_Lectura_Actual="";
	private static double latitud=0.0,longitud=0.0;
	private   static	String userid="";
	
	
	private static int getDatos(Context c,String Medidor,Double lectura,String imei,Double latitudp,Double longitudp)
	{
		SQLite sl = new SQLite(c);
		ArrayList<Object> datos;
		ArrayList<Object> tarifas;
		datos=sl.LastFactura(Medidor);
		if(datos==null)
		{
			return -1;
		}
		cuenta = (String)datos.get(1);
		Direccion=(String) datos.get(3);
		Alcantarillado=(Integer) datos.get(4);
		Nombre=(String) datos.get(5);
		medidor=Medidor;
		tipomedidor=(String) datos.get(7);
		
		Date last = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S",Locale.getDefault());
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
		try{
			last = sdf.parse((String)datos.get(10));
			
		}catch(Exception e )
		{return -1;}
		
		Fecha_Lectura_Actual=sdf2.format(new Date());
		Fecha_Lectura_Anterior=sdf2.format(last);
		lecturaAnt=(Double) datos.get(11);
		adeuda=(Double) datos.get(12);
		lecturaAct=lectura;
		Consumo=lecturaAct-lecturaAnt;
		
		tarifas=sl.Tarifa(Consumo, (Integer)datos.get(2));
		tarifadesc=(String) tarifas.get(2);
		tarifa =(Double) (tarifas.get(5));
		ValorporConsumo = (tarifa * Consumo + 1.5);
		Factura.imei=imei;
		if (Alcantarillado ==1)
		{
			ValorporAlcantarillado=ValorporConsumo*0.6;
		}else
		{
			ValorporAlcantarillado=0.0;
		}
		total=adeuda+ValorporAlcantarillado+ValorporConsumo;
		
			latitud=latitudp;
			longitud=longitudp;
			
			Double latitudmd=(Double) datos.get(8);
			Double longitudmd =(Double) datos.get(9);

			double dist= distancia(latitudp,longitudp,latitudmd,longitudmd);
			dist=dist/1000;
			if (dist < 5)
			{	dataFactura+=cuenta+";"+lecturaAct+";"+new Date().toString()+";"+Factura.adeuda+";"+Factura.ValorporAlcantarillado+";"+ValorporConsumo+";"+total+";"+latitud+";"+longitud+";"+Factura.imei;
		
				return sl.insertarDatos(dataFactura, "Facturas");
			}	
			
			return -2;
		
			
	}
	
	
	private static Double distancia (Double la1, Double lo1, Double la2, Double lo2)
	{
		
		double dist;
		
		Location loc1= new Location("Punto a");
		loc1.setLatitude(la1);
		loc1.setLongitude(lo1);
		
		Location loc2= new Location("Punto b");
		loc2.setLatitude(la2);
		loc2.setLongitude(lo2);
		dist=loc1.distanceTo(loc2);
		return dist;
			
	}
	
	private static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
    	for (int index = 0; index < cpyArray.length; index++) {
    		cpyArray[index] = srcArray[index];
    	}
    }
	
		
	private static void sendCommand(Context context, String portName, String portSettings, ArrayList<Byte> byteList) {
		StarIOPort port = null;
		try
		{
		
			port = StarIOPort.getPort(portName, portSettings, 20000, context);
		
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e) { }

		
			StarPrinterStatus status = port.beginCheckedBlock();

			if (true == status.offline)
			{
				throw new StarIOPortException("Al parecer la Impresora no tiene comunicacion");
			}

			byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
			port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

			status = port.endCheckedBlock();

			if (true == status.coverOpen)
			{
				throw new StarIOPortException("La tapadera de la impresora esta abierta");
			}
			else if (true == status.receiptPaperEmpty)
			{
				throw new StarIOPortException("No hay papel");
			}
			else if (true == status.offline)
			{
				throw new StarIOPortException("La impresora no tiene comunicaci�n");
			}
	
		}
		catch (StarIOPortException e)
		{
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.show();
		}
		finally
		{
			if (port != null)
			{
				try
				{
					StarIOPort.releasePort(port);
				}
				catch (StarIOPortException e) { }
			}
		}
	}


	private static byte[] convertFromListByteArrayTobyteArray(List<Byte> ByteArray)
	{
		byte[] byteArray = new byte[ByteArray.size()];
		for(int index = 0; index < byteArray.length; index++)
		{
			if (null == ByteArray.get(index)) {
				byteArray[index] = 0;
			}
			else
			{
			    byteArray[index] = ByteArray.get(index);
			}
		}
		
		return byteArray;
	}

//	,,String imei
    public static int PrintReceipt(Context context, String portName, String portSettings, String strPrintArea, Resources res,ArrayList<Object> parame)
    {	
    	String Medidor=(String) parame.get(0);
    	Double lectura=(Double) parame.get(1);
    	String imei=(String) parame.get(2);
    	Double latitud=(Double) parame.get(3);
    	Double longitud=(Double) parame.get(4);
    	
    	int err=getDatos(context ,Medidor, lectura,imei,latitud,longitud);
    	if(err<0)
    	{
    		return err;
    	}
    	int paperWidth = 576;
		int source =R.drawable.apclogo;
    	Bitmap b=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/sacap"+File.separator+"apclogo.jpg");
    	
    	Bitmap bm = BitmapFactory.decodeResource(res, source);
		StarBitmap starbitmap = new StarBitmap(b, false, paperWidth);
		try {
			byte[] outputByteBuffer=starbitmap.getImageEscPosDataForPrinting(true, true);
			StarIOPort port = StarIOPort.getPort(portName, portSettings, 10000);
			port.writePort(outputByteBuffer, 0, outputByteBuffer.length);
	
			
		} catch (StarIOPortException e1) {
	
			e1.printStackTrace();
			return -1;
		}

			if (strPrintArea.equals("3inch (78mm)")) {
            ArrayList<Byte> list = new ArrayList<Byte>();
            Byte[] tempList;
			byte[] outputByteBuffer = null;
			
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x57, 0x40, 0x32}));           //Page Area Setting     <GS> <W> nL nH  (nL = 64, nH = 2)
			
			list.addAll(Arrays.asList(new Byte[]{0x1b, 0x61, 0x01}));                 //Center Justification  <ESC> a n       (0 Left, 1 Center, 2 Right)
			

			  			outputByteBuffer = ("Proporcionar Salud, Bienestar \n" +
			            "y Satisfaccion es nuestra responsabilidad\n\n").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
			
			list.addAll(Arrays.asList(new Byte[]{0x1b, 0x61, 0x00}));                 // Left Alignment
			
			list.addAll(Arrays.asList(new Byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00}));     //Setting Horizontal Tab
                                   
			Date now = new Date();
			SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
			String fecha="Fecha "+ Fecha_Lectura_Actual;
			outputByteBuffer = fecha.getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
			
//		    list.addAll(Arrays.asList(new Byte[]{0x08}));                             // Left Alignment"
		    sfd = new SimpleDateFormat("hh:mm:ss aa");
		    list.addAll(Arrays.asList(new Byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00}));     //Setting Horizontal Tab
		    String hora= "           Hora: "+sfd.format(now) + "\n------------------------------------------------ \n";
		    outputByteBuffer=hora.getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
			sfd=new SimpleDateFormat("yyyyMM");
			outputByteBuffer = ("Numero de Cuenta    "+cuenta+"\n" +
								"Nombre de Usuario   "+Nombre+"\n" +
								"Tarifa              "+tarifadesc+"\n" +
								"Fecha Lectura Anterior "+Fecha_Lectura_Anterior+"\n"+
								"Numero Medidor      "+Medidor+"\n" +
								"Tipo Medidor        "+tipomedidor+"\n" +
								"Lectura Actual      "+lecturaAct+"\n" +
								"Lectura Anterior    "+lecturaAnt+"\n" +
								"Consumo MTS. Cubicos "+Consumo+"\n" +
								"Direccion            \n"+Direccion+"\n" +
								"Mes Facturado        "+sfd.format(now)+"\n"
								
					).getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));

			list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45, 0x01}));                 //Set Emphasized Printing ON

			outputByteBuffer = ("Detalle\n").getBytes();	
			
			 list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45, 0x00}));                 //Set Emphasized Printing OFF (same command as on)
			 
			outputByteBuffer = ("Saldo Anterior      "+adeuda.floatValue()+"\n" +
                        		"Agua Potable        "+ValorporConsumo.floatValue()+"\n" +
                        		"Alcantarillado      "+ValorporAlcantarillado.floatValue()+"\n" +
                        		"\n" +
                        		"\n\n" +
                        
                        "Total   ").getBytes();
	tempList = new Byte[outputByteBuffer.length];
	CopyArray(outputByteBuffer, tempList);
	list.addAll(Arrays.asList(tempList));
	list.addAll(Arrays.asList(new Byte[]{0x1d, 0x21, 0x11}));                 //Width and Height Character Expansion  <GS>  !  n
	
	outputByteBuffer = ("         L."+total.floatValue()+ "\n").getBytes();
	tempList = new Byte[outputByteBuffer.length];
	CopyArray(outputByteBuffer, tempList);
	list.addAll(Arrays.asList(tempList));
    
    //list.addAll(Arrays.asList(new Byte[]{0x1d, 0x21, 0x00}));                 //Cancel Expansion - Reference Star Portable Printer Programming Manual

    list.addAll(Arrays.asList(new Byte[]{0x1d, 0x42, 0x01}));                 //Specify White-Black Invert
    
	outputByteBuffer = ("Feliz a�o nuevo"+"\n").getBytes();
	tempList = new Byte[outputByteBuffer.length];
	CopyArray(outputByteBuffer, tempList);
	list.addAll(Arrays.asList(tempList));
    
	list.addAll(Arrays.asList(new Byte[]{0x1d, 0x42, 0x00}));                 //Cancel White-Black Invert
			 
		try{

			sendCommand(context, portName, portSettings, list);
		}catch	(Exception e ){return -1;}
		}
		return 0;
	
    }

	
}
