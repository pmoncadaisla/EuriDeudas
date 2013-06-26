package es.moncadaisla.eurideudas;

import java.util.ArrayList;

public class Categoria {
	private String nombre;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private ArrayList<Producto> productos;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public Categoria(int id, String nombre){
		this.nombre = nombre;
		this.id = id;
	}
	
	public void put(Producto p){
		this.productos.add(p);
	}
	
	


}
