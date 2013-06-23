package es.moncadaisla.eurideudas;

public class Datos {
	
	private String name;
	private String surname;
	private String deuda;
	
	public Datos(){
		name = "Nombre";
		surname = "Apellidos";
		deuda = "0";
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getDeuda() {
		return deuda;
	}
	public void setDeuda(String deuda) {
		this.deuda = deuda;
	}

	
	
	
	

}
