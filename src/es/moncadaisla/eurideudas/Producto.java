package es.moncadaisla.eurideudas;

public class Producto {
	private String nombre;
	private String precio;
	private int cid;
	
	public Producto(String n, String precio, int cid){
		this.nombre = n;
		this.precio = precio;
		this.cid = cid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}
}
