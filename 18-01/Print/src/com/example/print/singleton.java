package com.example.print;

public class singleton {
	private static singleton mInstance = null;
	 
    private String Impresora;
 
    private singleton(){
        Impresora = "BT:";
    }


    public static singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new singleton();
        }
        return mInstance;
    }
    
    public String getString(){
        return this.Impresora;
    }
 
    public void setString(String value){
        Impresora = value;
    }
}
