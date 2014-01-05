package com.example.print;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;


public class config extends Activity {
	
	Context c;
	String portname;
	String portSettings;
	int millisecs;
	
	public String getPortname() {
		return portname;
	}

	public void setPortname(String portname) {
		this.portname = portname;
	}

	public String getPortSettings() {
		return portSettings;
	}

	public void setPortSettings(String portSettings) {
		this.portSettings = portSettings;
	}

	public int getMillisecs() {
		return millisecs;
	}

	public void setMillisecs(int millisecs) {
		this.millisecs = millisecs;
	}

	public config(Context c) {
		this.millisecs=10000;
		this.portSettings="mini";
		this.c=c;
		this.portname="BT:";
		getPortDiscovery();
	}
	
	public void getPort(Context c)
	{
		StarIOPort puerto =null;
		
		
		try{
			puerto = StarIOPort.getPort(this.portname,this.portSettings ,this.millisecs );
			
		
			try
			{
				Thread.sleep(500);
			}
			catch(InterruptedException e) {}
			
			StarPrinterStatus status =  puerto.retreiveStatus();
			
			if(status.offline==false)
			{
				Builder dialog = new AlertDialog.Builder(c);
				dialog.setNegativeButton("Ok", null);
	    		AlertDialog alert = dialog.create();
	    		alert.setTitle("Printer");
	    		alert.setMessage("Printer is Online");
	    		alert.show();
			}else
			{
				String message = "Printer is offline";
				if(status.receiptPaperEmpty == true)
				{
					message += "\nPaper is Empty";
				}
				if(status.coverOpen == true)
				{
					message += "\nCover is Open";
				}
				Builder dialog = new AlertDialog.Builder(c);
	    		dialog.setNegativeButton("Ok", null);
	    		AlertDialog alert = dialog.create();
	    		alert.setTitle("Printer");
	    		alert.setMessage(message);
	    		alert.show();
			}
		}catch(Exception starIOPException)
		{
			Builder dialog = new AlertDialog.Builder(c);
    		dialog.setNegativeButton("Ok", null);
    		AlertDialog alert = dialog.create();
    		alert.setTitle("Failure");
    		alert.setMessage("Failed to connect to printer");
    		alert.show();
			
		}
		finally
		{
			if(puerto != null)
			{
				try {
					StarIOPort.releasePort(puerto);
				} catch (StarIOPortException e) {}
			}
		}
	}
	

	
	 public void getPortDiscovery() {
	    	String interfaceName="Bluetooth";
	    	List<PortInfo> BTPortList;
	    	List<PortInfo> TCPPortList;
	        final EditText editPortName;

	    	final ArrayList<PortInfo> arrayDiscovery;
	    	ArrayList<String> arrayPortName;

			arrayDiscovery = new ArrayList<PortInfo>();
			arrayPortName = new ArrayList<String>();
			
	    	try {
	    		if (true == interfaceName.equals("Bluetooth") || true == interfaceName.equals("All")) {
	    			BTPortList  = StarIOPort.searchPrinter("BT:");   
	    	    	
	    			for (PortInfo portInfo : BTPortList) {
	  	    		  arrayDiscovery.add(portInfo);
	  	    	    }
	    		}
	    		if (true == interfaceName.equals("LAN") || true == interfaceName.equals("All")) {
	    			TCPPortList = StarIOPort.searchPrinter("TCP:");
	    			
	    	    	for (PortInfo portInfo : TCPPortList) {
	    	    		arrayDiscovery.add(portInfo);
	    	    	}
	    		}
	    		
	    		arrayPortName = new ArrayList<String>();

				for(PortInfo discovery : arrayDiscovery)
				{
					String portName;

					portName = discovery.getPortName();

					if(discovery.getMacAddress().equals("") == false)
					{
						portName += "\n - " + discovery.getMacAddress();
						if(discovery.getModelName().equals("") == false)
						{
							portName += "\n - " + discovery.getModelName();
						}
					}

					arrayPortName.add(portName);
				}
			} catch (StarIOPortException e) {
				e.printStackTrace();
			}
	    	
	    	editPortName = new EditText(c);
	    	
	        new AlertDialog.Builder(c)
			.setIcon(android.R.drawable.checkbox_on_background) 
			.setView(editPortName) 
	
			.setItems(arrayPortName.toArray(new String[0]), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int select)
				{
					String portNameField,mac ;
					portNameField =(arrayDiscovery.get(select).getPortName());
	    			mac=arrayDiscovery.get(select).getMacAddress();
	    	        //SharedPreferences pref = getSharedPreferences("pref", MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
	    	        //Editor editor = pref.edit();
	    			//editor.putString("portName", portNameField);
	    			String devname = "BT:"+mac;
	    			setPortname(devname);
	    			singleton.getInstance().setString(devname); 
	    			getPort(c);
	    			//editor.commit();
				}
			})
			.show();
	       
	    }

	
}

