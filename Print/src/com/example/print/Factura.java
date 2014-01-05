package com.example.print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

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
	
	
	/**
	 * This function is used to print a java bitmap directly to a portable printer.
	 * @param context - Activity for displaying messages to the user
	 * @param portName - Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
	 * @param portSettings - Should be mini, the port settings mini is used for portable printers
	 * @param source - The bitmap to convert to Star printer data for portable printers
	 * @param maxWidth - The maximum width of the image to print.  This is usually the page width of the printer.  If the image exceeds the maximum width then the image is scaled down.  The ratio is maintained. 
	 */
	public static void PrintBitmap(Context context, String portName, String portSettings, Bitmap source, int maxWidth, boolean compressionEnable, boolean pageModeEnable)
	{
        ArrayList<Byte> commands = new ArrayList<Byte>();
        Byte[] tempList;
		
		StarBitmap starbitmap = new StarBitmap(source, false, maxWidth);
		
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
				throw new StarIOPortException("A printer is offline");
			}

			byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
			port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

			status = port.endCheckedBlock();

			if (true == status.coverOpen)
			{
				throw new StarIOPortException("Printer cover is open");
			}
			else if (true == status.receiptPaperEmpty)
			{
				throw new StarIOPortException("Receipt paper is empty");
			}
			else if (true == status.offline)
			{
				throw new StarIOPortException("Printer is offline");
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

	
}
