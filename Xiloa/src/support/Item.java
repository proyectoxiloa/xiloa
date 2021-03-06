package support;

/**
 * 
 * @author Denis Chavez
 * 
 * Esta clase es usada para manejar en los combos JSF el duo c�digo/descripci�n
 *
 */
public class Item {
	
	private Long id;	
	private String descripcion;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Item(){
		super();
	}
	public Item(Long id, String descripcion){
		super();
		this.id = id;
		this.descripcion= descripcion;
	}
}