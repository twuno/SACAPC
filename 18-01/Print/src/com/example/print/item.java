package com.example.print;

public class item {
    private String nombre;
    private int drawableImageID;
 
    public item(String nombre, int drawableImageID) {
        this.nombre = nombre;
        this.drawableImageID = drawableImageID;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public int getDrawableImageID() {
        return drawableImageID;
    }
 
    public void setDrawableImageID(int drawableImageID) {
        this.drawableImageID = drawableImageID;
    }
 
}