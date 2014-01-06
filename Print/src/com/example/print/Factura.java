package com.example.print;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;



import com.example.print.RasterDocument.RasPageEndMode;
import com.example.print.RasterDocument.RasSpeed;
import com.example.print.RasterDocument.RasTopMargin;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Factura {

	private   static String cuenta;
	private   static	Double ValorporConsumo;
	private static Double ValorporAlcantarillado; 
	private   static	String Direccion;
	private   static	String Nombre;
	private static int Alcantarillado ; 
	private static	String medidor;
	private static	String tipomedidor;
	private static Double lecturaAnt;	
	private static	Double adeuda;
	private static Double lecturaAct;
	private static	Double Consumo;
	private   static	String  tarifadesc;
	private   static	Double tarifa;
	private static Double total;
	private   static	String Fecha_Lectura_Anterior;
	private   static	String Fecha_Lectura_Actual;
	private static double latitud,longitud;
	
	
	private static int getDatos(Context c,String Medidor,Double lectura)
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
		Fecha_Lectura_Anterior=(String) datos.get(8);
		lecturaAnt=(Double) datos.get(9);
		adeuda=(Double) datos.get(10);
		lecturaAct=lectura;
		Consumo=lecturaAct-lecturaAnt;
		
		tarifas=sl.Tarifa(Consumo, (Integer)datos.get(2));
		tarifadesc=(String) tarifas.get(2);
		tarifa =(Double) (tarifas.get(5));
		ValorporConsumo = (tarifa * Consumo + 1.5);
		
		if (Alcantarillado ==1)
		{
			ValorporAlcantarillado=ValorporConsumo*0.6;
		}else
		{
			ValorporAlcantarillado=0.0;
		}
		total=adeuda+ValorporAlcantarillado+ValorporConsumo;
		GPSTracker gps = new GPSTracker(c);
		if (gps.canGetLocation())
		{
			latitud=gps.getLatitude();
			longitud=gps.getLongitude();
		}else
		{
			gps.showSettingsAlert();
		}
		return 0;
	}
	
	private static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
    	for (int index = 0; index < cpyArray.length; index++) {
    		cpyArray[index] = srcArray[index];
    	}
    }
	
	/*
	 * This function is used to print a java bitmap directly to a portable printer.
	 * @param context - Activity for displaying messages to the user
	 * @param portName - Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
	 * @param portSettings - Should be mini, the port settings mini is used for portable printers
	 * @param res - The resources object containing the image data
	 * @param source - The resource id of the image data
	 * @param maxWidth - The maximum width of the image to print.  This is usually the page width of the printer.  If the image exceeds the maximum width then the image is scaled down.  The ratio is maintained. 
	
	public static void PrintBitmapImage(Context context, String portName, String portSettings,  Resources res, int source, int maxWidth, boolean compressionEnable, boolean pageModeEnable)
	{
		
        ArrayList<Byte> commands = new ArrayList<Byte>();
        Byte[] tempList;
        
		Bitmap bm = BitmapFactory.decodeResource(res, source);
		StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);
		
		try {
			byte[] command = null;
			
			command = starbitmap.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable);
			
	        tempList = new Byte[command.length];
			CopyArray(command, tempList);
			commands.addAll(Arrays.asList(tempList));
			
			sendCommand(context, portName, portSettings, commands);
		} catch (StarIOPortException e) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.show();
		}
	}

	*/	
	private static void sendCommand(Context context, String portName, String portSettings, ArrayList<Byte> byteList) {
		StarIOPort port = null;
		try
		{
			/*
				using StarIOPort3.1.jar (support USB Port)
				Android OS Version: upper 2.2
			*/
			port = StarIOPort.getPort(portName, portSettings, 20000, context);
			/* 
				using StarIOPort.jar
				Android OS Version: under 2.1
				port = StarIOPort.getPort(portName, portSettings, 10000);
			*/
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e) { }

			/*
		    Portable Printer Firmware Version 2.4 later, SM-S220i(Firmware Version 2.0 later) 

            Using Begin / End Checked Block method for preventing "data detective".
            
            When sending large amounts of raster data,
            use Begin / End Checked Block method and adjust the value in the timeout in the "StarIOPort.getPort"
            in order to prevent "timeout" of the "endCheckedBlock method" while a printing.
            
            *If receipt print is success but timeout error occurs(Show message which is "There was no response of the printer within the timeout period."),
             need to change value of timeout more longer in "StarIOPort.getPort" method. (e.g.) 10000 -> 30000
            *When use "Begin / End Checked Block Sample Code", do comment out "query commands Sample code".
		    */

		    /* Start of Begin / End Checked Block Sample code */
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
				throw new StarIOPortException("La impresora no tiene comunicación");
			}
			/* End of Begin / End Checked Block Sample code*/



			/*
			    Portable Printer Firmware Version 2.3 earlier

                Using query commands for preventing "data detective".
                
				When sending large amounts of raster data,
				send query commands after writePort data for confirming the end of printing 
				and adjust the value in the timeout in the "checkPrinterSendToComplete" method
				in order to prevent "timeout" of the "sending query commands" while a printing.
                
				*If receipt print is success but timeout error occurs(Show message which is "There was no response of the printer within the timeout period."),
				 need to change value of timeout more longer in "checkPrinterSendToComplete" method. (e.g.) 10000 -> 30000
				*When use "query commands Sample code", do comment out "Begin / End Checked Block Sample Code".
			 */

			/* Start of query commands Sample code */
//            byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
//			port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);
//			
//			checkPrinterSendToComplete(port);
			/* End of query commands Sample code */
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

	
    public static int PrintSampleReceipt(Context context, String portName, String portSettings, String strPrintArea, Resources res,String Medidor,Double lectura)
    {	
    	if(getDatos(context ,Medidor, lectura)==-1)
    	{
    		return -1;
    	}
    	int paperWidth = 576;
		int source =R.drawable.apclogo;
	    
    	Bitmap bm = BitmapFactory.decodeResource(res, source);
		StarBitmap starbitmap = new StarBitmap(bm, false, paperWidth);
		try {
			byte[] outputByteBuffer=starbitmap.getImageEscPosDataForPrinting(true, true);
			StarIOPort port = StarIOPort.getPort(portName, portSettings, 1000);
			port.writePort(outputByteBuffer, 0, outputByteBuffer.length);
	
			
		} catch (StarIOPortException e1) {
			// TODO Auto-generated catch block
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
			String fecha="Fecha "+ sfd.format(now);
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
    
	outputByteBuffer = ("Feliz año nuevo\n").getBytes();
	tempList = new Byte[outputByteBuffer.length];
	CopyArray(outputByteBuffer, tempList);
	list.addAll(Arrays.asList(tempList));
    
	list.addAll(Arrays.asList(new Byte[]{0x1d, 0x42, 0x00}));                 //Cancel White-Black Invert
			 
			/*			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
            
            list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45, 0x00}));                 //Set Emphasized Printing OFF (same command as on)
			
            
            
			outputByteBuffer = ("PLAIN T-SHIRT                 10.99\n" +
                                "300692003    BLACK DENIM                   29.99\n" +
                                "300651148    BLUE DENIM                    29.99\n" +
                                "300642980    STRIPED DRESS                 49.99\n" +
                                "300638471    BLACK BOOTS                   35.99\n\n" +
                                "Subtotal                                  156.95\n" +
                                "Tax                                         0.00\n" +
                                "------------------------------------------------ \n" +
                                "Total   ").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));

			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x21, 0x11}));                 //Width and Height Character Expansion  <GS>  !  n
			
			outputByteBuffer = ("             $156.95\n").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
            
            list.addAll(Arrays.asList(new Byte[]{0x1d, 0x21, 0x00}));                 //Cancel Expansion - Reference Star Portable Printer Programming Manual
            
            
			outputByteBuffer = ("------------------------------------------------ \n" +
					            "Charge\n" + "$156.95\n" +
                                "Visa XXXX-XXXX-XXXX-0123\n").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
            
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x77, 0x02}));                 //for 1D Code39 Barcode
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x68, 0x64}));                 //for 1D Code39 Barcode
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x48, 0x01}));                 //for 1D Code39 Barcode
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x6b, 0x41, 0x0b, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30}));           //for 1D Code39 Barcode
			
			outputByteBuffer = ("\n").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
			
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x42, 0x01}));                 //Specify White-Black Invert
            
			outputByteBuffer = ("Refunds and Exchanges\n").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
            
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x42, 0x00}));                 //Cancel White-Black Invert

			outputByteBuffer = ("Within ").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
            
			list.addAll(Arrays.asList(new Byte[]{0x1b, 0x2d, 0x01}));                 //Specify Underline Printing
			
			outputByteBuffer = ("30 days").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
			
			list.addAll(Arrays.asList(new Byte[]{0x1b, 0x2d, 0x00}));                 //Cancel Underline Printing
			
			outputByteBuffer = (" with receipt\n" +
                                "And tags attached\n" +
                                "------------- Card Holder's Signature ---------- \n\n\n" +
                                "------------------------------------------------ \n" +
                                "Thank you for buying Star!\n" +
                                "Scan QR code to visit our site!\n").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));
            
			list.addAll(Arrays.asList(new Byte[]{0x1d, 0x5a, 0x02}));                 //Cancel Underline Printing
			
			outputByteBuffer = ("\n\n\n\n").getBytes();
			tempList = new Byte[outputByteBuffer.length];
			CopyArray(outputByteBuffer, tempList);
			list.addAll(Arrays.asList(tempList));*/

			sendCommand(context, portName, portSettings, list);
			
		}
		return 0;
	
    }

	
}
